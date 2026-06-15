@echo off
setlocal

set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

set DB_USER=anokas
set DB_PASS=Informatica_1706869
set DB_URL=jdbc:mysql://localhost:3306/take_a_break_web?createDatabaseIfNotExist=true^&useSSL=false^&allowPublicKeyRetrieval=true^&serverTimezone=UTC
set DATASOURCE_DRIVER=com.mysql.cj.jdbc.Driver

cd /d "%~dp0webapp"

echo Compilando...
call mvn -q -DskipTests compile

if errorlevel 1 (
    echo ERRO: Falha na compilacao. Verifique o output acima.
    pause
    exit /b 1
)

echo A obter classpath...
call mvn -q dependency:build-classpath -Dmdep.outputFile=cp.txt

set /p CP=<cp.txt

echo.
echo A iniciar Take a Break Webapp na porta 8081...
echo URL: http://localhost:8081/index.html
echo.

java --enable-native-access=ALL-UNNAMED ^
     -cp "target\classes;%CP%" ^
     com.takeabreak.web.TakeABreakWebApplication

endlocal
