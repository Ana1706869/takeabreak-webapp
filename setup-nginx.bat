@echo off
REM ===========================================
REM Setup Nginx Reverse Proxy - takeabreak.pt
REM Executar como Administrador
REM ===========================================

setlocal enabledelayedexpansion

echo.
echo ===== SETUP NGINX - TAKEABREAK.PT =====
echo.

REM Verificar se é Admin
net session >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Este script precisa de correr como Administrador!
    echo Direito do rato sobre .bat e escolhe "Run as administrator"
    pause
    exit /b 1
)

REM [1] Instalar Chocolatey
echo [1/6] Verificando Chocolatey...
where choco >nul 2>&1
if %errorlevel% neq 0 (
    echo Instalando Chocolatey...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
    REM Adicionar Chocolatey ao PATH temporariamente
    set PATH=C:\ProgramData\chocolatey\bin;!PATH!
)
echo [OK] Chocolatey pronto
echo.

REM [2] Instalar Nginx
echo [2/6] Instalando Nginx via Chocolatey...
call choco install nginx -y --force 2>nul
if %errorlevel% neq 0 (
    echo ERRO ao instalar Nginx
    pause
    exit /b 1
)
echo [OK] Nginx instalado
echo.

REM [3] Parar Nginx
echo [3/6] Parando Nginx (se estiver ativo)...
net stop nginx 2>nul
timeout /t 2 /nobreak >nul
echo [OK] Nginx parado
echo.

REM [4] Criar diretório SSL
echo [4/6] Criando diretório SSL...
if not exist "C:\nginx\conf\ssl" mkdir C:\nginx\conf\ssl
echo [OK] Diretório criado: C:\nginx\conf\ssl
echo.

REM [5] Gerar certificado (precisa OpenSSL no PATH)
echo [5/6] Gerando certificado TLS auto-assinado...
where openssl >nul 2>&1
if %errorlevel% neq 0 (
    echo Instalando OpenSSL...
    call choco install openssl -y --force 2>nul
)

REM Gerar chave privada
openssl genrsa -out C:\nginx\conf\ssl\takeabreak.key 2048 2>nul
if %errorlevel% neq 0 (
    echo ERRO ao gerar chave privada
    pause
    exit /b 1
)

REM Gerar certificado
openssl req -new -x509 -key C:\nginx\conf\ssl\takeabreak.key -out C:\nginx\conf\ssl\takeabreak.crt -days 365 -subj "/CN=takeabreak.pt/O=Take a Break/C=PT" 2>nul
if %errorlevel% neq 0 (
    echo ERRO ao gerar certificado
    pause
    exit /b 1
)
echo [OK] Certificado criado (valido 365 dias)
echo.

REM [6] Copiar nginx.conf (assume que está no mesmo dir que este .bat)
echo [6/6] Configurando Nginx...
if not exist "%~dp0nginx.conf" (
    echo ERRO: nginx.conf não encontrado em %~dp0
    echo Copia nginx.conf para a mesma pasta que este .bat
    pause
    exit /b 1
)
copy /Y "%~dp0nginx.conf" "C:\nginx\conf\nginx.conf" >nul
echo [OK] nginx.conf copiado
echo.

REM [7] Validar configuração
echo Validando configuração Nginx...
"C:\nginx\nginx.exe" -t
if %errorlevel% neq 0 (
    echo ERRO na configuração!
    pause
    exit /b 1
)
echo [OK] Configuração válida
echo.

REM [8] Iniciar Nginx
echo Iniciando Nginx...
net start nginx
if %errorlevel% neq 0 (
    echo ERRO ao iniciar Nginx
    pause
    exit /b 1
)
echo [OK] Nginx iniciado
echo.

REM Verificar portas
echo Verificando portas em escuta...
netstat -ano | findstr ":80 " >nul
if %errorlevel% equ 0 echo [OK] Porta 80 LISTENING
netstat -ano | findstr ":443 " >nul
if %errorlevel% equ 0 echo [OK] Porta 443 LISTENING
echo.

echo ========================================
echo [SUCESSO] NGINX CONFIGURADO E ATIVO!
echo ========================================
echo.
echo Proximos passos:
echo 1. Criar DNS A em host-redirect:
echo    @ ^| A ^| 188.250.157.96 ^| TTL 300
echo.
echo 2. Port forward no router:
echo    TCP 80 -^> 192.168.68.103:80
echo    TCP 443 -^> 192.168.68.103:443
echo.
echo 3. Validar:
echo    https://takeabreak.pt
echo.
pause
