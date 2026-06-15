@echo off
REM Build script for Take a Break Web Application
REM This ensures JAVA_HOME is always set correctly before building

setlocal enabledelayedexpansion

set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo ==================================================
echo Take a Break Web - Build Script
echo ==================================================
echo JAVA_HOME: %JAVA_HOME%
echo Java Version:
java -version
echo.
echo Building...
echo ==================================================
echo.

REM Navigate to webapp directory
cd /d "%~dp0webapp"

REM Run Maven
mvn -DskipTests clean package spring-boot:repackage

if %errorlevel% equ 0 (
    echo.
    echo ==================================================
    echo Build SUCCESSFUL!
    echo JAR file: webapp\target\take-a-break-web-1.0.0.jar
    echo ==================================================
) else (
    echo.
    echo ==================================================
    echo Build FAILED!
    echo ==================================================
    exit /b 1
)
