@echo off
title Take a Break App
echo ========================================
echo   Take a Break App
echo ========================================
echo.


REM Compilar
echo A compilar...
javac -cp "dist/lib/sqlite-jdbc-3.23.1.jar;dist/lib/jcalendar-1.4.jar" -d bin src/RegistoFolgas/*.java 2>nul
if errorlevel 1 (
    echo ERRO: Falha na compilacao. Certifique-se que o Java JDK esta instalado.
    pause
    exit /b 1
)

REM Criar pasta de dados se nao existir
if not exist data\ mkdir data
if not exist data\AgendamentoFolgas.db (
    copy dist\data\AgendamentoFolgas.db data\ >nul
)

REM Executar
echo A iniciar aplicacao...
java --enable-native-access=ALL-UNNAMED -cp "bin;dist/lib/sqlite-jdbc-3.23.1.jar;dist/lib/jcalendar-1.4.jar" RegistoFolgas.Login
