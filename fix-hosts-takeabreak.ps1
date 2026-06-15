$hostsPath = 'C:\Windows\System32\drivers\etc\hosts'
$lines = @(
    '127.0.0.1 takeabreak.pt',
    '127.0.0.1 www.takeabreak.pt'
)

$current = Get-Content -Path $hostsPath -ErrorAction Stop
foreach ($line in $lines) {
    if (-not ($current -contains $line)) {
        Add-Content -Path $hostsPath -Value $line
    }
}

ipconfig /flushdns | Out-Null
Write-Host 'HOSTS_OK'
