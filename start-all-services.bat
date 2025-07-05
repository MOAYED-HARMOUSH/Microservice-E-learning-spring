@echo off
echo Starting All E-Learning Services...

echo.
echo 1. Starting Config Server (Port 8080)...
start "Config Server" cmd /k "cd configServer && mvnw spring-boot:run"

timeout /t 10 /nobreak > nul

echo.
echo 2. Starting Eureka Server (Port 8761)...
start "Eureka Server" cmd /k "cd discovery && mvnw spring-boot:run"

timeout /t 15 /nobreak > nul

echo.
echo 3. Starting Gateway (Port 8010)...
start "Gateway" cmd /k "cd gateway && mvnw spring-boot:run"

timeout /t 10 /nobreak > nul

echo.
echo 4. Starting User Service (Port 8081)...
start "User Service" cmd /k "cd user && mvnw spring-boot:run"

timeout /t 10 /nobreak > nul

echo.
echo 5. Starting Course Service (Port 8082)...
start "Course Service" cmd /k "cd courseManagement && mvnw spring-boot:run"

timeout /t 10 /nobreak > nul

echo.
echo 6. Starting Payment Service (Port 8050)...
start "Payment Service" cmd /k "cd payment && mvnw spring-boot:run"

timeout /t 10 /nobreak > nul

echo.
echo 7. Starting Exam Service Instance 1 (Port 8040)...
start "Exam Service 1" cmd /k "cd Exam && set SERVER_PORT=8040 && set SPRING_PROFILES_ACTIVE=instance1 && mvnw spring-boot:run"

timeout /t 5 /nobreak > nul

echo.
echo 8. Starting Exam Service Instance 2 (Port 8041)...
start "Exam Service 2" cmd /k "cd Exam && set SERVER_PORT=8041 && set SPRING_PROFILES_ACTIVE=instance2 && mvnw spring-boot:run"

echo.
echo All services are starting...
echo.
echo Services URLs:
echo - Config Server: http://localhost:8080
echo - Eureka Server: http://localhost:8761
echo - Gateway: http://localhost:8010
echo - User Service: http://localhost:8081
echo - Course Service: http://localhost:8082
echo - Payment Service: http://localhost:8050
echo - Exam Service 1: http://localhost:8040
echo - Exam Service 2: http://localhost:8041
echo.
echo Load Balancer will distribute requests between Exam Service instances
echo.
pause 