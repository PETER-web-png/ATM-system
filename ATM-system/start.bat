@echo off
setlocal enabledelayedexpansion

echo ========================================
echo   ATM System - Maven Wrapper Setup
echo ========================================
echo.

set "MAVEN_VERSION=3.9.6"
set "MAVEN_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip"
set "MAVEN_DIR=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%"
set "MAVEN_ZIP=%MAVEN_DIR%\apache-maven-%MAVEN_VERSION%-bin.zip"
set "MAVEN_BIN=%MAVEN_DIR%\apache-maven-%MAVEN_VERSION%\bin\mvn.cmd"

REM Check if Maven is already downloaded
if exist "%MAVEN_BIN%" (
    echo Maven %MAVEN_VERSION% already downloaded.
    goto :run
)

echo Downloading Maven %MAVEN_VERSION%...
mkdir "%MAVEN_DIR%" 2>nul

REM Try using PowerShell to download
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '%MAVEN_URL%' -OutFile '%MAVEN_ZIP%'}" 2>nul

if not exist "%MAVEN_ZIP%" (
    echo.
    echo ERROR: Failed to download Maven.
    echo Please download manually from: https://maven.apache.org/download.cgi
    echo Extract to: %MAVEN_DIR%
    echo.
    pause
    exit /b 1
)

echo Extracting Maven...
powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%MAVEN_DIR%' -Force"

if not exist "%MAVEN_BIN%" (
    echo.
    echo ERROR: Failed to extract Maven.
    echo.
    pause
    exit /b 1
)

echo Maven downloaded successfully!

:run
echo.
echo Starting ATM System...
echo.

set "JAVA_HOME=C:\Program Files\Java\latest\jdk-26\JDK"
set "PATH=%JAVA_HOME%\bin;%PATH%"

java -version
if errorlevel 1 (
    echo ERROR: Java not working!
    pause
    exit /b 1
)

cd /d "%~dp0"

echo Running: "%MAVEN_BIN%" spring-boot:run
"%MAVEN_BIN%" spring-boot:run
echo.
echo Exit code: %errorlevel%
pause
