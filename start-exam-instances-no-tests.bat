@echo off
echo ========================================
echo Starting Multiple Exam Service Instances
echo ========================================
echo.

REM Get the current directory
set CURRENT_DIR=%CD%

REM Clean and compile first (skip tests)
echo Step 1: Cleaning and compiling Exam service (skipping tests)...
cd /d %CURRENT_DIR%\Exam
call mvn clean compile -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo Step 2: Starting Exam Service instances...
echo.

REM Start Exam Service Instance 1 (Port 8040)
echo Starting Exam Service Instance 1 on port 8040...
start "Exam Service 1" cmd /k "cd /d %CURRENT_DIR%\Exam && mvn spring-boot:run -DskipTests -Dspring-boot.run.arguments=--server.port=8040 --spring.jpa.hibernate.ddl-auto=create-drop"

REM Wait for first instance to start
echo Waiting 20 seconds for first instance to start...
timeout /t 20 /nobreak > nul

REM Start Exam Service Instance 2 (Port 8041)
echo Starting Exam Service Instance 2 on port 8041...
start "Exam Service 2" cmd /k "cd /d %CURRENT_DIR%\Exam && mvn spring-boot:run -DskipTests -Dspring-boot.run.arguments=--server.port=8041 --spring.jpa.hibernate.ddl-auto=create-drop"

REM Wait for second instance to start
echo Waiting 20 seconds for second instance to start...
timeout /t 20 /nobreak > nul

REM Start Exam Service Instance 3 (Port 8042)
echo Starting Exam Service Instance 3 on port 8042...
start "Exam Service 3" cmd /k "cd /d %CURRENT_DIR%\Exam && mvn spring-boot:run -DskipTests -Dspring-boot.run.arguments=--server.port=8042 --spring.jpa.hibernate.ddl-auto=create-drop"

echo.
echo ========================================
echo All Exam Service instances are starting...
echo ========================================
echo.
echo Instance 1: http://localhost:8040
echo Instance 2: http://localhost:8041
echo Instance 3: http://localhost:8042
echo.
echo Load Balancer will distribute requests across these instances.
echo.
echo To test load balancing, run: .\test-load-balancer.bat
echo.
echo Press any key to continue...
pause > nul 