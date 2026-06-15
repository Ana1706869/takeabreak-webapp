$ErrorActionPreference = 'Stop'

# Stop only existing Take a Break webapp java processes
Get-CimInstance Win32_Process -Filter "name = 'java.exe'" |
  Where-Object { $_.CommandLine -like '*take-a-break-web-1.0.0.jar*' } |
  ForEach-Object {
    try { Stop-Process -Id $_.ProcessId -Force -ErrorAction Stop } catch {}
  }

# Ensure log directory exists
New-Item -ItemType Directory -Path 'C:\takeabreak\logs' -Force | Out-Null

$java = 'C:\Program Files\Java\jdk-24\bin\java.exe'
$jar = 'C:\Users\anasi\take-a-break-web-1.0.0.jar'
$outLog = 'C:\takeabreak\logs\app.log'
$errLog = 'C:\takeabreak\logs\error.log'
$pidFile = 'C:\takeabreak\logs\webapp.pid'

if (!(Test-Path $java)) {
  throw "Java not found at $java"
}
if (!(Test-Path $jar)) {
  throw "Jar not found at $jar"
}

# Start detached with explicit Spring datasource args (no env dependency).
$proc = Start-Process -FilePath $java -ArgumentList @(
  '--enable-native-access=ALL-UNNAMED',
  '-jar',
  $jar,
  '--server.port=8081',
  '--spring.datasource.url=jdbc:mysql://localhost:3306/take_a_break_web?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC',
  '--spring.datasource.username=anokas',
  '--spring.datasource.password=Informatica_1706869',
  '--spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver'
) -WindowStyle Hidden -RedirectStandardOutput $outLog -RedirectStandardError $errLog -PassThru
Set-Content -Path $pidFile -Value $proc.Id

Start-Sleep -Seconds 8

$alive = Get-Process -Id $proc.Id -ErrorAction SilentlyContinue
Write-Output ("Started process PID=" + $proc.Id)
if ($alive) {
  Write-Output 'Process is alive.'
} else {
  Write-Output 'Process exited shortly after start.'
}

Write-Output 'Current listeners on 8081:'
netstat -ano | Select-String ':8081'

Write-Output 'Last app.log lines:'
if (Test-Path $outLog) { Get-Content $outLog -Tail 40 }

Write-Output 'Last error.log lines:'
if (Test-Path $errLog) { Get-Content $errLog -Tail 40 }
