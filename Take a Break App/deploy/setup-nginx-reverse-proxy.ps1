# Configure Nginx reverse proxy for Take a Break Webapp
# Point takeabreak.pt:80 -> localhost:8081

param(
    [string]$Domain = "takeabreak.pt",
    [string]$LocalPort = "8081"
)

$ErrorActionPreference = 'Stop'

Write-Output "=== Nginx Configuration for Take a Break Webapp ==="
Write-Output "Domain: $Domain"
Write-Output "Backend: localhost:$LocalPort"
Write-Output ""

# Check if Nginx is installed
$nginxPath = 'C:\nginx\nginx.exe'
if (!(Test-Path $nginxPath)) {
    Write-Output "[1/4] Downloading Nginx for Windows..."
    $downloadUrl = "http://nginx.org/download/nginx-1.26.1.zip"
    $zipPath = "C:\nginx-1.26.1.zip"
    $extractPath = "C:\"
    
    try {
        (New-Object System.Net.WebClient).DownloadFile($downloadUrl, $zipPath)
        Write-Output "[2/4] Extracting Nginx..."
        Expand-Archive -Path $zipPath -DestinationPath $extractPath -Force
        Remove-Item $zipPath
    } catch {
        Write-Output "ERROR: Could not download Nginx automatically"
        Write-Output "Please download from: http://nginx.org/download/nginx-1.26.1.zip"
        Write-Output "Extract to: C:\nginx"
        exit 1
    }
} else {
    Write-Output "[2/4] Nginx already installed"
}

# Create Nginx config
$nginxConfDir = "C:\nginx\conf"
$nginxConfFile = "$nginxConfDir\nginx.conf"

Write-Output "[3/4] Creating Nginx configuration..."

# Ensure directory exists
New-Item -ItemType Directory -Path $nginxConfDir -Force | Out-Null

# Backup original if exists
if (Test-Path $nginxConfFile) {
    Copy-Item $nginxConfFile "$nginxConfFile.backup.$(Get-Date -Format 'yyyyMMddHHmmss')"
}

$nginxConfig = @"
#user  nobody;
worker_processes  1;

error_log  logs/error.log;
pid        logs/nginx.pid;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  logs/access.log  main;

    sendfile        on;
    tcp_nopush      on;
    tcp_nodelay     on;
    keepalive_timeout  65;
    types_hash_max_size 2048;

    # Reverse Proxy for Take a Break Webapp
    upstream webapp_backend {
        server localhost:$LocalPort max_fails=3 fail_timeout=30s;
    }

    server {
        listen       80 default_server;
        listen       [::]:80 default_server;
        server_name  $Domain *.takeabreak.pt localhost 127.0.0.1;

        location / {
            proxy_pass http://webapp_backend;
            proxy_set_header Host `$host;
            proxy_set_header X-Real-IP `$remote_addr;
            proxy_set_header X-Forwarded-For `$proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto `$scheme;
            proxy_set_header Connection "";
            proxy_http_version 1.1;
            proxy_buffering off;
            proxy_redirect off;
        }

        # Health check endpoint
        location /health {
            access_log off;
            return 200 "healthy\n";
            add_header Content-Type text/plain;
        }
    }

    # Default server for unmapped hosts (redirect to webapp)
    server {
        listen       80;
        listen       [::]:80;
        server_name  _;

        location / {
            proxy_pass http://webapp_backend;
            proxy_set_header Host $Domain;
            proxy_set_header X-Real-IP `$remote_addr;
            proxy_set_header X-Forwarded-For `$proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto `$scheme;
        }
    }
}
"@

Set-Content -Path $nginxConfFile -Value $nginxConfig -Encoding UTF8
Write-Output "✅ Nginx configuration created"

# Test configuration
Write-Output ""
Write-Output "Testing Nginx configuration..."
& "$nginxPath" -t -c "$nginxConfFile" 2>&1 | ForEach-Object { Write-Output $_ }

# Stop any running Nginx
Write-Output ""
Write-Output "[4/4] Restarting Nginx..."
Stop-Process -Name nginx -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 1

# Start Nginx
& "$nginxPath" -c "$nginxConfFile"
Start-Sleep -Seconds 2

# Verify Nginx is running
$nginxProc = Get-Process -Name nginx -ErrorAction SilentlyContinue
if ($nginxProc) {
    Write-Output "✅ Nginx started successfully (PID: $($nginxProc.Id))"
} else {
    Write-Output "❌ Failed to start Nginx"
    exit 1
}

# Verify port 80 is listening
Write-Output ""
Write-Output "Port 80 Status:"
netstat -ano | Select-String ":80 " | Select-String "LISTENING"

Write-Output ""
Write-Output "=== Configuration Complete ==="
Write-Output ""
Write-Output "✅ Reverse proxy configured: $Domain -> localhost:$LocalPort"
Write-Output "✅ Nginx listening on port 80"
Write-Output ""
Write-Output "Access the application at:"
Write-Output "  http://$Domain"
Write-Output "  http://$Domain:80"
Write-Output ""
Write-Output "Nginx Logs:"
Write-Output "  Error Log:  C:\nginx\logs\error.log"
Write-Output "  Access Log: C:\nginx\logs\access.log"
Write-Output ""
