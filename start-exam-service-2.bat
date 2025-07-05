@echo off
echo Starting Exam Service Instance 2 on Port 8041...
cd Exam
set SERVER_PORT=8041
set SPRING_PROFILES_ACTIVE=instance2
mvnw spring-boot:run
pause 