# اختبار الاتصال بين خدمة الامتحانات والخدمات الأخرى
Write-Host "=== اختبار الاتصال بين خدمة الامتحانات والخدمات الأخرى ===" -ForegroundColor Green

# تسجيل الدخول كطالب
Write-Host "`n1. تسجيل الدخول كطالب:" -ForegroundColor Yellow
try {
    $loginData = @{
        email = "STUDENT3@2.c"
        password = "password123"
    } | ConvertTo-Json
    
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8010/api/users/login" -Method POST -Body $loginData -ContentType "application/json"
    $token = $loginResponse.token
    
    Write-Host "تم تسجيل الدخول بنجاح" -ForegroundColor Green
    Write-Host "Token: $($token.Substring(0, 20))..." -ForegroundColor Cyan
    
} catch {
    Write-Host "خطأ في تسجيل الدخول: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# اختبار الاتصال مع خدمة إدارة الكورسات
Write-Host "`n2. اختبار الاتصال مع خدمة إدارة الكورسات:" -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    $courseConnectionResponse = Invoke-RestMethod -Uri "http://localhost:8010/api/exams/test/course-service-connection" -Method GET -Headers $headers
    Write-Host $courseConnectionResponse -ForegroundColor Cyan
    
} catch {
    Write-Host "خطأ في اختبار الاتصال مع خدمة إدارة الكورسات: $($_.Exception.Message)" -ForegroundColor Red
}

# اختبار الاتصال مع خدمة الدفع
Write-Host "`n3. اختبار الاتصال مع خدمة الدفع:" -ForegroundColor Yellow
try {
    $paymentConnectionResponse = Invoke-RestMethod -Uri "http://localhost:8010/api/exams/test/payment-service-connection" -Method GET -Headers $headers
    Write-Host $paymentConnectionResponse -ForegroundColor Cyan
    
} catch {
    Write-Host "خطأ في اختبار الاتصال مع خدمة الدفع: $($_.Exception.Message)" -ForegroundColor Red
}

# اختبار تقديم امتحان
Write-Host "`n4. اختبار تقديم امتحان:" -ForegroundColor Yellow
try {
    $examSubmissionData = @{
        examId = 1
        answers = @{
            "1" = "ANSWER1"
            "2" = "ANSWER1"
            "3" = "ANSWER2"
        }
    } | ConvertTo-Json -Depth 3
    
    $examSubmissionResponse = Invoke-RestMethod -Uri "http://localhost:8010/api/exams/submit" -Method POST -Body $examSubmissionData -Headers $headers -ContentType "application/json"
    Write-Host "تم تقديم الامتحان بنجاح!" -ForegroundColor Green
    Write-Host "معرف النتيجة: $($examSubmissionResponse.id)" -ForegroundColor Cyan
    Write-Host "الدرجة: $($examSubmissionResponse.studentDegree)" -ForegroundColor Cyan
    Write-Host "الحالة: $($examSubmissionResponse.status)" -ForegroundColor Cyan
    
} catch {
    Write-Host "خطأ في تقديم الامتحان: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== انتهى اختبار الاتصال ===" -ForegroundColor Green
