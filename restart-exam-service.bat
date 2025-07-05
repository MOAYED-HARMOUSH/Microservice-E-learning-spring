@echo off
echo ========================================
echo إعادة تشغيل خدمة الامتحانات
echo ========================================
echo.

:: إيقاف خدمة الامتحانات
echo إيقاف خدمة الامتحانات...
taskkill /f /im java.exe 2>nul
timeout /t 3 /nobreak >nul

:: بناء المشروع
echo.
echo بناء مشروع الامتحانات...
cd Exam
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo خطأ في بناء المشروع
    pause
    exit /b 1
)

:: بدء خدمة الامتحانات
echo.
echo بدء خدمة الامتحانات...
start "Exam Service" cmd /k "mvn spring-boot:run"

echo.
echo انتظر 15 ثانية حتى تبدأ الخدمة...
timeout /t 15 /nobreak >nul

:: اختبار الخدمة
echo.
echo اختبار خدمة الامتحانات...
curl -s http://localhost:8040/actuator/health >nul
if %errorlevel% equ 0 (
    echo ✓ خدمة الامتحانات تعمل بشكل صحيح
) else (
    echo ✗ مشكلة في خدمة الامتحانات
)

echo.
echo ========================================
echo تم إعادة تشغيل خدمة الامتحانات!
echo ========================================
echo.
echo للاختبار:
echo 1. شغل سكريبت اختبار الاتصال: .\test-exam-connections.ps1
echo 2. اختبر تقديم امتحان مباشرة
echo.
pause
