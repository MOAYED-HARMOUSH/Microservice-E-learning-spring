@echo off
echo ========================================
echo اختبار الترجمة (Compilation Test)
echo ========================================
echo.

:: الانتقال إلى مجلد الامتحانات
cd Exam

:: اختبار الترجمة
echo اختبار الترجمة...
call mvn clean compile

if %errorlevel% equ 0 (
    echo.
    echo ✓ الترجمة نجحت!
    echo يمكنك الآن تشغيل الخدمة
) else (
    echo.
    echo ✗ فشل في الترجمة
    echo راجع الأخطاء أعلاه
)

echo.
pause 