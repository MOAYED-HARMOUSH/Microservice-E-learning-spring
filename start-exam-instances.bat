@echo off
echo Starting Multiple Exam Service Instances for Load Balancing...
echo.

REM Set Java options for better performance
set JAVA_OPTS=-Xmx512m -Xms256m

REM Navigate to Exam directory
cd Exam

REM Start Exam Service Instance 1 (Port 8040)
echo Starting Exam Service Instance 1 on port 8040...
start "Exam Service 1" cmd /k "set SERVER_PORT=8040 && mvn spring-boot:run -Dspring-boot.run.jvmArguments=%JAVA_OPTS%"

REM Wait 10 seconds
timeout /t 10 /nobreak > nul

REM Start Exam Service Instance 2 (Port 8041)
echo Starting Exam Service Instance 2 on port 8041...
start "Exam Service 2" cmd /k "set SERVER_PORT=8041 && mvn spring-boot:run -Dspring-boot.run.jvmArguments=%JAVA_OPTS%"

REM Wait 10 seconds
timeout /t 10 /nobreak > nul

REM Start Exam Service Instance 3 (Port 8042)
echo Starting Exam Service Instance 3 on port 8042...
start "Exam Service 3" cmd /k "set SERVER_PORT=8042 && mvn spring-boot:run -Dspring-boot.run.jvmArguments=%JAVA_OPTS%"

echo.
echo All Exam Service instances are starting...
echo.
echo Instance 1: http://localhost:8040
echo Instance 2: http://localhost:8041
echo Instance 3: http://localhost:8042
echo.
echo Load Balancer will distribute requests across these instances.
echo.
echo Press any key to continue...
pause > nul 