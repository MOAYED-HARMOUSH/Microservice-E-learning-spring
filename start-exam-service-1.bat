@echo off
echo Starting Exam Service Instance 1 on Port 8040...
cd Exam
set SERVER_PORT=8040
set SPRING_PROFILES_ACTIVE=instance1
mvnw spring-boot:run
pause 