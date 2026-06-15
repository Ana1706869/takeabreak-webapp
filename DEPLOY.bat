@echo off
REM Deploy automático - detecta SO e executa script apropriado

cls
color 0a

echo.
echo =========================================
echo    DEPLOY TAKEABREAK.PT - CLOUD
echo =========================================
echo.

REM Verificar se é Admin
net session >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Este script precisa correr como Administrador!
    echo.
    echo Direito do rato na janela de CMD e escolha "Run as Administrator"
    pause
    exit /b 1
)

echo Detectando ambiente...
echo.

REM Verificar se Node.js está instalado
where node >nul 2>&1
if %errorlevel% neq 0 (
    echo Instalando Node.js (requerido para Railway)...
    echo.
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
    choco install nodejs -y
)

REM Executar PowerShell deploy script
echo Iniciando deploy para Railway.app...
echo.
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0deploy-railway.ps1"

pause
