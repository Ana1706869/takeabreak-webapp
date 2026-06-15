# Deploy para Railway.app - PowerShell
# Executar como Administrador

Write-Host "========================================" -ForegroundColor Green
Write-Host "  Deploy Webapp para Railway.app" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Verificar se é Admin
if (-NOT ([Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    Write-Host "ERRO: Este script precisa de correr como Administrador!" -ForegroundColor Red
    exit 1
}

# [1] Instalar Node.js se não tiver
Write-Host "[1/5] Verificando Node.js..." -ForegroundColor Yellow
$nodejs = Get-Command node -ErrorAction SilentlyContinue
if (-NOT $nodejs) {
    Write-Host "Instalando Node.js via Chocolatey..." -ForegroundColor Yellow
    if (-NOT (Get-Command choco -ErrorAction SilentlyContinue)) {
        Write-Host "Instalando Chocolatey..." -ForegroundColor Yellow
        Set-ExecutionPolicy Bypass -Scope Process -Force
        [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
        iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
    }
    choco install nodejs -y
}
Write-Host "[OK] Node.js pronto" -ForegroundColor Green
Write-Host ""

# [2] Instalar Railway CLI
Write-Host "[2/5] Instalando Railway CLI..." -ForegroundColor Yellow
$railway = Get-Command railway -ErrorAction SilentlyContinue
if (-NOT $railway) {
    npm install -g @railway/cli
}
Write-Host "[OK] Railway CLI instalado" -ForegroundColor Green
Write-Host ""

# [3] Compilar Webapp
Write-Host "[3/5] Compilando webapp..." -ForegroundColor Yellow
Push-Location (Join-Path $PSScriptRoot "webapp")
mvn -DskipTests clean package spring-boot:repackage
if (-NOT (Test-Path "target\take-a-break-web-1.0.0.jar")) {
    Write-Host "ERRO: JAR não encontrado" -ForegroundColor Red
    Pop-Location
    exit 1
}
Write-Host "[OK] Webapp compilado" -ForegroundColor Green
Pop-Location
Write-Host ""

# [4] Fazer Deploy
Write-Host "[4/5] Fazendo deploy para Railway..." -ForegroundColor Yellow
Write-Host "Nota: Pode pedir para fazer login no Railway (GitHub ou Email)" -ForegroundColor Cyan
Write-Host ""

# Verificar se tem projeto Railway
$railwayConfig = Join-Path $PSScriptRoot ".railway\config.json"
if (-NOT (Test-Path $railwayConfig)) {
    Write-Host "Inicializando projeto Railway..." -ForegroundColor Yellow
    railway init --name "takeabreak-webapp"
}

# Deploy
railway deploy --service web
Write-Host "[OK] Deploy completo" -ForegroundColor Green
Write-Host ""

# [5] Obter URL
Write-Host "[5/5] Obtendo informações de acesso..." -ForegroundColor Yellow
$railwayUrl = railway domain
Write-Host ""
Write-Host "==========================================" -ForegroundColor Green
Write-Host "[SUCESSO] WEBAPP ONLINE NA CLOUD!" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Green
Write-Host ""
Write-Host "URL Railway: $railwayUrl" -ForegroundColor Cyan
Write-Host ""
Write-Host "Proximos passos:" -ForegroundColor Yellow
Write-Host "1. Cria DNS A em host-redirect.com:" -ForegroundColor White
Write-Host "   @ | A | <IP-da-Railway> | TTL 300" -ForegroundColor White
Write-Host ""
Write-Host "2. Adiciona dominio a Railway:" -ForegroundColor White
Write-Host "   railway link takeabreak.pt" -ForegroundColor White
Write-Host ""
Write-Host "3. Valida:" -ForegroundColor White
Write-Host "   https://takeabreak.pt" -ForegroundColor Cyan
Write-Host ""
Write-Host "Credenciais:" -ForegroundColor Yellow
Write-Host "Email: anasilva_pinhel@hotmail.com" -ForegroundColor White
Write-Host "Password: informatica" -ForegroundColor White
Write-Host ""

pause
