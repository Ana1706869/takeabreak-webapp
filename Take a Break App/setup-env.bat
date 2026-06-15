@echo off
REM Configuração de ambiente para o projeto Take a Break

setlocal enabledelayedexpansion

set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo ✓ JAVA_HOME configurado para: %JAVA_HOME%
echo ✓ Java version:
java -version

REM Se foram passados argumentos, executar comando
if not "%~1"=="" (
    %*
)
