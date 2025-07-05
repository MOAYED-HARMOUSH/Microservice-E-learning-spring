@echo off
echo ========================================
echo Restarting Exam Services with Updates
echo ========================================
echo.

REM Get the current directory
set CURRENT_DIR=%CD%

REM Kill existing Java processes (Exam services only)
echo Stopping existing Exam services...
taskkill /F /IM java.exe /FI "WINDOWTITLE eq Exam Service*" 2>nul
timeout /t 3 /nobreak > nul

REM Clean and build
echo Building with updated configuration...
cd /d %CURRENT_DIR%\Exam
call mvn clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo Starting Exam Service instances with updated security...
echo.

REM Start Exam Service Instance 1 (Port 8040)
echo Starting Exam Service Instance 1 on port 8040...
start "Exam Service 1" cmd /k "cd /d %CURRENT_DIR%\Exam && mvn spring-boot:run -DskipTests -Dspring-boot.run.arguments=--server.port=8040"

REM Wait for first instance to start
echo Waiting 20 seconds for first instance to start...
timeout /t 20 /nobreak > nul

REM Start Exam Service Instance 2 (Port 8041)
echo Starting Exam Service Instance 2 on port 8041...
start "Exam Service 2" cmd /k "cd /d %CURRENT_DIR%\Exam && mvn spring-boot:run -DskipTests -Dspring-boot.run.arguments=--server.port=8041"

REM Wait for second instance to start
echo Waiting 20 seconds for second instance to start...
timeout /t 20 /nobreak > nul

REM Start Exam Service Instance 3 (Port 8042)
echo Starting Exam Service Instance 3 on port 8042...
start "Exam Service 3" cmd /k "cd /d %CURRENT_DIR%\Exam && mvn spring-boot:run -DskipTests -Dspring-boot.run.arguments=--server.port=8042"

echo.
echo ========================================
echo All Exam Service instances restarted!
echo ========================================
echo.
echo Instance 1: http://localhost:8040
echo Instance 2: http://localhost:8041
echo Instance 3: http://localhost:8042
echo.
echo Security updated - loadbalancer endpoints are now accessible!
echo.
echo To test load balancing, run: .\quick-test.ps1
echo.
echo Press any key to continue...
pause > nul 