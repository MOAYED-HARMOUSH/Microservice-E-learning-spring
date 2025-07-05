@echo off
echo Testing Load Balancer for Exam Service...
echo.

REM Set the gateway URL
set GATEWAY_URL=http://localhost:8010

echo Testing through API Gateway: %GATEWAY_URL%
echo.

REM Test 1: Health Check
echo Test 1: Health Check
echo GET %GATEWAY_URL%/api/exams/loadbalancer/health
curl -X GET "%GATEWAY_URL%/api/exams/loadbalancer/health" -H "Content-Type: application/json"
echo.
echo.

REM Test 2: Get Instances
echo Test 2: Get Instances
echo GET %GATEWAY_URL%/api/exams/loadbalancer/instances
curl -X GET "%GATEWAY_URL%/api/exams/loadbalancer/instances" -H "Content-Type: application/json"
echo.
echo.

REM Test 3: Get Load Balancer Stats
echo Test 3: Get Load Balancer Stats
echo GET %GATEWAY_URL%/api/exams/loadbalancer/stats
curl -X GET "%GATEWAY_URL%/api/exams/loadbalancer/stats" -H "Content-Type: application/json"
echo.
echo.

REM Test 4: Multiple Requests to Test Load Distribution
echo Test 4: Testing Load Distribution (10 requests)...
echo.

for /L %%i in (1,1,10) do (
    echo Request %%i:
    curl -X GET "%GATEWAY_URL%/api/exams/loadbalancer/health" -H "Content-Type: application/json" -s | findstr "port"
    timeout /t 1 /nobreak > nul
)

echo.
echo Load balancing test completed!
echo.
echo To monitor in real-time, you can:
echo 1. Check Eureka Dashboard: http://localhost:8761
echo 2. Check individual instances:
echo    - Instance 1: http://localhost:8040/actuator/health
echo    - Instance 2: http://localhost:8041/actuator/health
echo    - Instance 3: http://localhost:8042/actuator/health
echo.
echo Press any key to continue...
pause > nul 