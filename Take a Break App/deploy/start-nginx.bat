@echo off
echo Killing existing Nginx processes...
taskkill /IM nginx.exe /F 2>nul
timeout /t 2 /nobreak

echo Starting Nginx from C:\nginx...
cd /d C:\nginx
start nginx.exe -c C:\nginx\conf\nginx.conf
timeout /t 3 /nobreak

echo Checking if Nginx is running...
tasklist | find /i nginx

echo Done!
