@echo off
REM Copy SSH setup script to remote server via SMB and execute
REM Usage: run this in the project root

setlocal enabledelayedexpansion

set REMOTE_IP=192.168.68.103
set REMOTE_USER=anasilva
set REMOTE_SHARE=\\%REMOTE_IP%\c$
set SCRIPT_PATH=deploy\setup-ssh-windows-standalone.ps1
set REMOTE_DESKTOP=Users\anasilva\Desktop
set REMOTE_SCRIPT=!REMOTE_DESKTOP!\setup-ssh-windows-standalone.ps1

echo.
echo ====================================
echo SSH Setup - Automated Installation
echo ====================================
echo.

REM Step 1: Check connectivity
echo [1/4] Testing network connectivity to %REMOTE_IP%...
ping -n 1 %REMOTE_IP% >nul 2>&1
if errorlevel 1 (
    echo ERROR: Cannot reach %REMOTE_IP%
    pause
    exit /b 1
)
echo OK - Host reachable

REM Step 2: Map network drive
echo [2/4] Connecting to remote share...
net use %REMOTE_SHARE% /user:%REMOTE_USER% /persistent:no >nul 2>&1
if errorlevel 1 (
    echo WARNING: Could not auto-connect. You may need to enter password.
    net use %REMOTE_SHARE% /user:%REMOTE_USER%
)

REM Step 3: Copy script
echo [3/4] Copying SSH setup script...
if not exist "%SCRIPT_PATH%" (
    echo ERROR: Script not found at %SCRIPT_PATH%
    pause
    exit /b 1
)

copy "%SCRIPT_PATH%" "%REMOTE_SHARE%\!REMOTE_SCRIPT!" /Y
if errorlevel 1 (
    echo ERROR: Could not copy script to remote desktop
    pause
    exit /b 1
)
echo OK - Script copied

REM Step 4: Execute script remotely
echo [4/4] Executing SSH setup on remote machine...
echo.
echo IMPORTANT: 
echo - Go to the remote machine (192.168.68.103)
echo - Open PowerShell as Administrator
echo - Run this command:
echo.
echo   Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force; ^& "C:\Users\anasilva\Desktop\setup-ssh-windows-standalone.ps1"
echo.
echo Press any key when you've completed the setup on the remote machine...
pause

REM Step 5: Verify SSH is now working
echo [5/5] Verifying SSH connection...
powershell -NoProfile -Command "Test-NetConnection -ComputerName %REMOTE_IP% -Port 22 -WarningAction SilentlyContinue | Select-Object TcpTestSucceeded"

echo.
echo ====================================
echo Setup Complete!
echo ====================================
echo.
echo You can now run the deployment:
echo.
echo   cd webapp
echo   chmod +x deploy/publish-remote.sh deploy/server-bootstrap.sh
echo   ./deploy/publish-remote.sh ^
echo     --host %REMOTE_IP% ^
echo     --user %REMOTE_USER% ^
echo     --port 22 ^
echo     --domain %REMOTE_IP% ^
echo     --db-name take_a_break_web ^
echo     --db-user takeabreak ^
echo     --db-pass 'Informatica_1706869'
echo.

net use %REMOTE_SHARE% /delete /yes >nul 2>&1

pause
