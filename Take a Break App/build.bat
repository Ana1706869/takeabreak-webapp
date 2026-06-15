@echo off
REM Script para compilar o Take a Break App
REM Autor: Recuperação de Bytecode

echo ========================================
echo Compilando Take a Break App...
echo ========================================
echo.

REM Criar directórios necessários
if not exist bin mkdir bin
if not exist data mkdir data

REM Compilar todos os ficheiros Java
echo Compilando ficheiros Java...
javac -cp "dist/lib/sqlite-jdbc-3.23.1.jar;dist/lib/jcalendar-1.4.jar" -d bin src/RegistoFolgas/*.java

REM Verificar se compilou com sucesso
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo COMPILACAO CONCLUIDA COM SUCESSO
    echo ========================================
    echo.
    echo Para executar a aplicação, use: run.bat
    echo Ou execute: java --enable-native-access=ALL-UNNAMED -cp "bin;dist/lib/sqlite-jdbc-3.23.1.jar;dist/lib/jcalendar-1.4.jar" RegistoFolgas.Login
) else (
    echo.
    echo ERRO NA COMPILACAO!
    echo Verifique os erros acima.
)

pause
