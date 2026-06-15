@echo off
REM Quick OpenSSH setup script for Windows Server/Desktop
REM Run this ON the remote machine (192.168.68.103) as Administrator

setlocal enabledelayedexpansion

echo.
echo ====== OpenSSH Installation Script ======
echo Run this as Administrator on the remote machine
echo.

powershell -NoProfile -ExecutionPolicy Bypass -Command ^
"$ErrorActionPreference = 'Stop'; ^
Write-Host 'Checking for OpenSSH...' -ForegroundColor Green; ^
$cap = Get-WindowsCapability -Online | Where-Object {$_.Name -like '*OpenSSH.Server*'}; ^
if ($cap) { ^
  Write-Host 'Found: '$cap.Name; ^
  if ($cap.State -eq 'NotPresent') { ^
    Write-Host 'Installing OpenSSH Server...' -ForegroundColor Green; ^
    Add-WindowsCapability -Online -Name $cap.Name; ^
    Write-Host 'Installed.' -ForegroundColor Green; ^
  } else { ^
    Write-Host 'OpenSSH already installed.' -ForegroundColor Green; ^
  } ^
} else { ^
  Write-Host 'OpenSSH not found in capabilities.' -ForegroundColor Yellow; ^
  Write-Host 'Trying alternative installation method...' -ForegroundColor Yellow; ^
  exit 1; ^
}; ^
Write-Host 'Starting sshd service...' -ForegroundColor Green; ^
Start-Service sshd -ErrorAction SilentlyContinue; ^
Set-Service -Name sshd -StartupType Automatic; ^
Write-Host 'Configuring firewall...' -ForegroundColor Green; ^
if (-not (Get-NetFirewallRule -Name sshd -ErrorAction SilentlyContinue)) { ^
  New-NetFirewallRule -Name sshd -DisplayName 'SSH' -Direction Inbound -Action Allow -Protocol TCP -LocalPort 22; ^
} ^
Write-Host 'Validating...' -ForegroundColor Green; ^
Get-Service sshd; ^
Write-Host ''; ^
netstat -ano | findstr :22; ^
Write-Host ''; ^
Write-Host 'SSH Setup Complete!' -ForegroundColor Green; ^
Write-Host 'You can now run the deploy script from your local machine.' -ForegroundColor Green; ^
pause;"

pause
