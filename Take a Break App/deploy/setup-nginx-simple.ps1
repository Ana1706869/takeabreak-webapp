# Simpler Nginx setup - download and extract directly
$ErrorActionPreference = 'Stop'

Write-Output "=== Setting up Nginx for Take a Break ==="

# Download Nginx
$url = "http://nginx.org/download/nginx-1.26.1.zip"
$zipPath = "C:\Users\anasi\nginx-1.26.1.zip"
$extractPath = "C:\"

Write-Output "[1/3] Downloading Nginx..."
try {
    $webClient = New-Object System.Net.WebClient
    $webClient.DownloadFile($url, $zipPath)
    Write-Output "✅ Downloaded"
} catch {
    Write-Output "❌ Download failed: $_"
    exit 1
}

Write-Output "[2/3] Extracting..."
try {
    if (Test-Path "C:\nginx-1.26.1") { Remove-Item "C:\nginx-1.26.1" -Recurse -Force }
    if (Test-Path "C:\nginx") { Remove-Item "C:\nginx" -Recurse -Force }
    
    Add-Type -AssemblyName System.IO.Compression.FileSystem
    [System.IO.Compression.ZipFile]::ExtractToDirectory($zipPath, $extractPath)
    
    # Rename folder
    Rename-Item "C:\nginx-1.26.1" "C:\nginx" -Force
    Remove-Item $zipPath
    Write-Output "✅ Extracted to C:\nginx"
} catch {
    Write-Output "❌ Extraction failed: $_"
    exit 1
}

# Create nginx.conf
Write-Output "[3/3] Configuring Nginx..."

# Create required directories
@("C:\nginx\logs", "C:\nginx\conf", "C:\nginx\html") | ForEach-Object {
    New-Item -ItemType Directory -Path $_ -Force | Out-Null
}

$nginxConf = @'
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
    keepalive_timeout  65;

    upstream webapp {
        server 127.0.0.1:8081;
    }

    server {
        listen       80;
        listen       [::]:80;
        server_name  takeabreak.pt *.takeabreak.pt localhost;

        location / {
            proxy_pass http://webapp;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_http_version 1.1;
        }

        location /health {
            access_log off;
            return 200 "OK";
        }
    }
}
'@

Set-Content -Path "C:\nginx\conf\nginx.conf" -Value $nginxConf -Encoding UTF8
Write-Output "✅ Config created"

# Start Nginx
Write-Output ""
Write-Output "Starting Nginx..."
Stop-Process -Name nginx -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 1

# Change to nginx directory and start
Set-Location "C:\nginx"
& ".\nginx.exe" -c "C:\nginx\conf\nginx.conf"
Start-Sleep -Seconds 2

$proc = Get-Process -Name nginx -ErrorAction SilentlyContinue
if ($proc) {
    Write-Output "✅ Nginx started (PID: $($proc.Id))"
    Write-Output ""
    Write-Output "=== SUCCESS ==="
    Write-Output "Application accessible at: http://takeabreak.pt"
    Write-Output "Nginx config: C:\nginx\conf\nginx.conf"
    Write-Output "Logs: C:\nginx\logs\"
    
    Start-Sleep -Seconds 3
    netstat -ano | Select-String ":80 " | Select-String "LISTENING"
} else {
    Write-Output "❌ Failed to start Nginx"
    Get-Content "C:\nginx\logs\error.log" -Tail 50 -ErrorAction SilentlyContinue
    exit 1
}
