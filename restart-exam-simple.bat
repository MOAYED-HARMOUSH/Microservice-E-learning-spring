@echo off
echo ========================================
echo إعادة تشغيل خدمة الامتحانات (مبسط)
echo ========================================
echo.

:: إيقاف خدمة الامتحانات
echo إيقاف خدمة الامتحانات...
taskkill /f /im java.exe 2>nul
timeout /t 3 /nobreak >nul

:: الانتقال إلى مجلد الامتحانات
cd Exam

:: بدء خدمة الامتحانات
echo.
echo بدء خدمة الامتحانات...
start "Exam Service" cmd /k "mvn spring-boot:run"

echo.
echo انتظر 20 ثانية حتى تبدأ الخدمة...
timeout /t 20 /nobreak >nul

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