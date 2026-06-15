#!/usr/bin/env powershell
# Standalone OpenSSH setup for Windows - no external downloads required
# Run as Administrator on the remote server

$ErrorActionPreference = 'Stop'
$ProgressPreference = 'SilentlyContinue'

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "OpenSSH Setup - Windows Server" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if running as admin
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Host "ERROR: This script must run as Administrator" -ForegroundColor Red
    exit 1
}

# Method 1: Try built-in Windows capability (fastest if available)
Write-Host "[1/3] Checking for built-in OpenSSH capability..." -ForegroundColor Green
try {
    $cap = Get-WindowsCapability -Online | Where-Object {$_.Name -like '*OpenSSH.Server*'} | Select-Object -First 1
    
    if ($cap) {
        Write-Host "Found: $($cap.Name)" -ForegroundColor Green
        
        if ($cap.State -eq 'NotPresent') {
            Write-Host "Installing OpenSSH Server..." -ForegroundColor Green
            $cap | Add-WindowsCapability -Online
            Write-Host "Installation complete" -ForegroundColor Green
        } else {
            Write-Host "OpenSSH already installed (State: $($cap.State))" -ForegroundColor Green
        }
        
        $methodUsed = "Windows Capability"
    } else {
        throw "OpenSSH capability not found"
    }
} catch {
    Write-Host "Windows capability method failed: $_" -ForegroundColor Yellow
    Write-Host "Checking for manual installation..." -ForegroundColor Yellow
    
    $methodUsed = $null
    
    # Check if sshd.exe exists on system
    $sshdPath = Get-Command sshd.exe -ErrorAction SilentlyContinue | Select-Object -ExpandProperty Source
    if ($sshdPath) {
        Write-Host "Found existing sshd at: $sshdPath" -ForegroundColor Green
        $methodUsed = "Existing Installation"
    }
}

# Method 2: Try to start service
Write-Host "[2/3] Setting up SSH service..." -ForegroundColor Green
try {
    # Stop any existing service
    Stop-Service sshd -ErrorAction SilentlyContinue
    Start-Sleep -Milliseconds 500
    
    # Try to start the service
    Start-Service sshd -ErrorAction SilentlyContinue
    
    # Set to auto-start
    Set-Service -Name sshd -StartupType Automatic
    
    Write-Host "SSH service configured" -ForegroundColor Green
} catch {
    Write-Host "Note: Service configuration had issues, but SSH may still work" -ForegroundColor Yellow
}

# Method 3: Configure firewall
Write-Host "[3/3] Configuring Windows Firewall..." -ForegroundColor Green
try {
    # Remove old rule if exists
    Remove-NetFirewallRule -Name sshd -ErrorAction SilentlyContinue
    
    # Create new rule
    New-NetFirewallRule `
        -Name sshd `
        -DisplayName "OpenSSH Server (sshd)" `
        -Enabled True `
        -Direction Inbound `
        -Protocol TCP `
        -Action Allow `
        -LocalPort 22 | Out-Null
    
    Write-Host "Firewall rule created" -ForegroundColor Green
} catch {
    Write-Host "WARNING: Could not create firewall rule: $_" -ForegroundColor Yellow
}

# Validation
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Validation Results" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "SSH Service Status:" -ForegroundColor Green
try {
    Get-Service sshd -ErrorAction Stop
} catch {
    Write-Host "Service not found" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Port 22 Status:" -ForegroundColor Green
$port22 = netstat -ano -p TCP 2>$null | Select-String ":22 "
if ($port22) {
    $port22
} else {
    Write-Host "Port 22 not yet listening (service may need restart)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Setup Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Method used: $methodUsed" -ForegroundColor Green
Write-Host "You can now deploy from your local machine." -ForegroundColor Green
Write-Host ""

# If service didn't start, try manual start
if (-not ($port22)) {
    Write-Host "Attempting manual service start..." -ForegroundColor Yellow
    try {
        Start-Service sshd
        Start-Sleep -Seconds 2
        $port22 = netstat -ano -p TCP 2>$null | Select-String ":22 "
        if ($port22) {
            Write-Host "Service started successfully!" -ForegroundColor Green
            $port22
        }
    } catch {
        Write-Host "Could not start service: $_" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Press Enter to exit..." -ForegroundColor Gray
Read-Host | Out-Null
