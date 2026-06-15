# Setup Nginx Reverse Proxy para takeabreak.pt
# Executar como Administrador no servidor Windows 192.168.68.103

Write-Host "=== Setup Nginx Reverse Proxy ===" -ForegroundColor Green

# Verificar se corre como Admin
if (-NOT ([Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    Write-Host "ERRO: Este script precisa de correr como Administrador!" -ForegroundColor Red
    exit 1
}

# Passo 1: Instalar Nginx via Chocolatey
Write-Host "`n[1/5] Instalando Nginx..." -ForegroundColor Cyan
if (-NOT (Get-Command choco -ErrorAction SilentlyContinue)) {
    Write-Host "Chocolatey não está instalado. Instalando..." -ForegroundColor Yellow
    Set-ExecutionPolicy Bypass -Scope Process -Force
    [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
    iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
}

choco install nginx -y --force 2>$null
Write-Host "Nginx instalado." -ForegroundColor Green

# Passo 2: Parar Nginx se estiver a correr
Write-Host "`n[2/5] Parando Nginx (se estiver ativo)..." -ForegroundColor Cyan
Stop-Service nginx -ErrorAction SilentlyContinue
Start-Sleep -Seconds 2

# Passo 3: Criar diretório de SSL
Write-Host "`n[3/5] Criando diretório SSL..." -ForegroundColor Cyan
$sslDir = "C:\nginx\conf\ssl"
New-Item -ItemType Directory -Path $sslDir -Force | Out-Null
Write-Host "SSL dir criado: $sslDir" -ForegroundColor Green

# Passo 4: Gerar chave privada
Write-Host "`n[4/5] Gerando certificado TLS auto-assinado (365 dias)..." -ForegroundColor Cyan

# Garantir que OpenSSL está disponível
$openssl = "C:\Program Files\Git\usr\bin\openssl.exe"
if (-NOT (Test-Path $openssl)) {
    $openssl = (Get-Command openssl -ErrorAction SilentlyContinue).Source
}

if (-NOT (Test-Path $openssl)) {
    Write-Host "OpenSSL não encontrado. Instalando via Chocolatey..." -ForegroundColor Yellow
    choco install openssl -y --force 2>$null
    $openssl = "C:\Program Files\Git\usr\bin\openssl.exe"
}

# Gerar chave privada (2048 RSA)
& $openssl genrsa -out "$sslDir\takeabreak.key" 2048 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO ao gerar chave privada" -ForegroundColor Red
    exit 1
}
Write-Host "Chave privada criada: $sslDir\takeabreak.key" -ForegroundColor Green

# Gerar certificado auto-assinado (365 dias)
& $openssl req -new -x509 `
    -key "$sslDir\takeabreak.key" `
    -out "$sslDir\takeabreak.crt" `
    -days 365 `
    -subj "/CN=takeabreak.pt/O=Take a Break/C=PT" 2>$null

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO ao gerar certificado" -ForegroundColor Red
    exit 1
}
Write-Host "Certificado criado: $sslDir\takeabreak.crt" -ForegroundColor Green

# Passo 5: Criar ficheiro nginx.conf
Write-Host "`n[5/5] Criando configuração Nginx..." -ForegroundColor Cyan

$nginxConf = @'
worker_processes auto;
error_log C:/nginx/logs/error.log warn;
pid C:/nginx/logs/nginx.pid;

events {
    worker_connections 1024;
}

http {
    include mime.types;
    default_type application/octet-stream;

    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';

    access_log C:/nginx/logs/access.log main;

    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    keepalive_timeout 65;
    types_hash_max_size 2048;

    # Redirect HTTP to HTTPS
    server {
        listen 80;
        server_name takeabreak.pt www.takeabreak.pt 192.168.68.103;
        return 301 https://$host$request_uri;
    }

    # HTTPS com reverse proxy
    server {
        listen 443 ssl http2;
        server_name takeabreak.pt www.takeabreak.pt 192.168.68.103;

        ssl_certificate C:/nginx/conf/ssl/takeabreak.crt;
        ssl_certificate_key C:/nginx/conf/ssl/takeabreak.key;

        ssl_protocols TLSv1.2 TLSv1.3;
        ssl_ciphers HIGH:!aNULL:!MD5;
        ssl_prefer_server_ciphers on;

        location / {
            proxy_pass http://127.0.0.1:8081;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_buffering off;
            proxy_request_buffering off;
        }
    }
}
'@

$nginxConfPath = "C:\nginx\conf\nginx.conf"
Set-Content -Path $nginxConfPath -Value $nginxConf -Encoding UTF8
Write-Host "Nginx.conf criado: $nginxConfPath" -ForegroundColor Green

# Validar sintaxe
Write-Host "`nValidando configuração Nginx..." -ForegroundColor Cyan
& "C:\nginx\nginx.exe" -t 2>&1 | ForEach-Object { Write-Host $_ }

# Iniciar Nginx
Write-Host "`n[✓] Iniciando Nginx..." -ForegroundColor Cyan
Start-Service nginx
Start-Sleep -Seconds 2

# Validar se está a correr
$svc = Get-Service nginx
if ($svc.Status -eq "Running") {
    Write-Host "✓ Nginx está ATIVO e a escutar em 80 e 443" -ForegroundColor Green
} else {
    Write-Host "✗ ERRO: Nginx não iniciou!" -ForegroundColor Red
    exit 1
}

# Testar conectividade local
Write-Host "`n[Teste] Validando reverse proxy local..." -ForegroundColor Cyan
$response = $null
try {
    $response = Invoke-WebRequest -Uri "http://127.0.0.1" -TimeoutSec 5 -SkipCertificateCheck -ErrorAction SilentlyContinue
    if ($response.StatusCode -eq 302 -or $response.StatusCode -eq 200) {
        Write-Host "✓ HTTP redirect/proxy funciona!" -ForegroundColor Green
    }
} catch {
    Write-Host "⚠ Aviso: Não conseguiu testar localmente. Isto é normal se a app estiver parada." -ForegroundColor Yellow
}

Write-Host "`n" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host "✓ SETUP COMPLETO!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host "`nPróximos passos:" -ForegroundColor Yellow
Write-Host "1. Criar registo DNS em host-redirect.com:" -ForegroundColor White
Write-Host "   - Nome: @" -ForegroundColor White
Write-Host "   - Tipo: A" -ForegroundColor White
Write-Host "   - Valor: 188.250.157.96" -ForegroundColor White
Write-Host "   - TTL: 300" -ForegroundColor White
Write-Host "`n2. Port forward no router:" -ForegroundColor White
Write-Host "   - TCP 80 -> 192.168.68.103:80" -ForegroundColor White
Write-Host "   - TCP 443 -> 192.168.68.103:443" -ForegroundColor White
Write-Host "`n3. Depois de criar registos e port forward, testa:" -ForegroundColor White
Write-Host "   - https://takeabreak.pt" -ForegroundColor White
Write-Host "`nCertificado auto-assinado válido até: " (Get-Date).AddYears(1) -ForegroundColor Cyan
Write-Host "`n"
