$nginxPath = "C:\nginx\nginx.exe"
$nginxConf = "C:\nginx\conf\nginx.conf"

# Kill existing
Get-Process nginx -ErrorAction SilentlyContinue | Stop-Process -Force
Start-Sleep -Seconds 2

# Start new - with absolute path
$pinfo = New-Object System.Diagnostics.ProcessStartInfo
$pinfo.FileName = $nginxPath
$pinfo.Arguments = "-c $nginxConf"
$pinfo.UseShellExecute = $false
$pinfo.RedirectStandardOutput = $false
$pinfo.CreateNoWindow = $true

$p = [System.Diagnostics.Process]::Start($pinfo)
Start-Sleep -Seconds 2

# Check port
$netstat = cmd /c "netstat -ano"| Select-String ":80"
Write-Output "Port 80 status:"
Write-Output $netstat

# Check if running
$proc = Get-Process nginx -ErrorAction SilentlyContinue
if ($proc) {
    Write-Output "Nginx running (PIDs: $($proc.Id -join ', '))"
} else {
    Write-Output "Nginx not found"
}
