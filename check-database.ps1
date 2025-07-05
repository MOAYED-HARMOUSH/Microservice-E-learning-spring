# فحص قاعدة البيانات وإصلاح مشكلة الطالب
Write-Host "=== فحص قاعدة البيانات وإصلاح مشكلة الطالب ===" -ForegroundColor Green

# تسجيل الدخول كطالب
Write-Host "`n1. تسجيل الدخول كطالب:" -ForegroundColor Yellow
try {
    $loginData = @{
        email = "STUDENT@2.c"
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

# اختبار الحصول على معلومات المستخدم
Write-Host "`n2. اختبار الحصول على معلومات المستخدم:" -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    # اختبار الحصول على معلومات المستخدم
    $userInfoResponse = Invoke-RestMethod -Uri "http://localhost:8010/api/users/profile" -Method GET -Headers $headers
    Write-Host "معلومات المستخدم:" -ForegroundColor Cyan
    Write-Host "  - User ID: $($userInfoResponse.id)" -ForegroundColor White
    Write-Host "  - Email: $($userInfoResponse.email)" -ForegroundColor White
    Write-Host "  - Role: $($userInfoResponse.role)" -ForegroundColor White
    
} catch {
    Write-Host "خطأ في الحصول على معلومات المستخدم: $($_.Exception.Message)" -ForegroundColor Red
}

# اختبار الحصول على رصيد المحفظة
Write-Host "`n3. اختبار الحصول على رصيد المحفظة:" -ForegroundColor Yellow
try {
    # اختبار مع studentId = 2 (من الـ token)
    $walletResponse = Invoke-RestMethod -Uri "http://localhost:8010/api/users/wallet/2" -Method GET -Headers $headers
    Write-Host "رصيد المحفظة للطالب 2: $($walletResponse.balance)" -ForegroundColor Green
    
} catch {
    Write-Host "خطأ في الحصول على رصيد المحفظة للطالب 2: $($_.Exception.Message)" -ForegroundColor Red
    
    # محاولة مع user ID من الـ token
    try {
        $userInfoResponse = Invoke-RestMethod -Uri "http://localhost:8010/api/users/profile" -Method GET -Headers $headers
        $userId = $userInfoResponse.id
        
        Write-Host "محاولة مع User ID: $userId" -ForegroundColor Yellow
        $walletResponse2 = Invoke-RestMethod -Uri "http://localhost:8010/api/users/wallet/$userId" -Method GET -Headers $headers
        Write-Host "رصيد المحفظة للـ User $userId: $($walletResponse2.balance)" -ForegroundColor Green
        
    } catch {
        Write-Host "خطأ في الحصول على رصيد المحفظة للـ User: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# اختبار إضافة رصيد للمحفظة
Write-Host "`n4. اختبار إضافة رصيد للمحفظة:" -ForegroundColor Yellow
try {
    $amountToAdd = 1000.0
    
    # محاولة مع studentId = 2
    $addMoneyResponse = Invoke-RestMethod -Uri "http://localhost:8010/api/users/wallet/2/add" -Method POST -Body $amountToAdd -Headers $headers -ContentType "application/json"
    Write-Host "تم إضافة $amountToAdd للمحفظة" -ForegroundColor Green
    Write-Host "الرصيد الجديد: $($addMoneyResponse.balance)" -ForegroundColor Green
    
} catch {
    Write-Host "خطأ في إضافة الرصيد للطالب 2: $($_.Exception.Message)" -ForegroundColor Red
    
    # محاولة مع user ID
    try {
        $userInfoResponse = Invoke-RestMethod -Uri "http://localhost:8010/api/users/profile" -Method GET -Headers $headers
        $userId = $userInfoResponse.id
        
        Write-Host "محاولة إضافة رصيد للـ User ID: $userId" -ForegroundColor Yellow
        $addMoneyResponse2 = Invoke-RestMethod -Uri "http://localhost:8010/api/users/wallet/$userId/add" -Method POST -Body $amountToAdd -Headers $headers -ContentType "application/json"
        Write-Host "تم إضافة $amountToAdd للمحفظة" -ForegroundColor Green
        Write-Host "الرصيد الجديد: $($addMoneyResponse2.balance)" -ForegroundColor Green
        
    } catch {
        Write-Host "خطأ في إضافة الرصيد للـ User: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# اختبار الاشتراك في كورس
Write-Host "`n5. اختبار الاشتراك في كورس:" -ForegroundColor Yellow
try {
    # الحصول على الكورسات المتاحة
    $coursesResponse = Invoke-RestMethod -Uri "http://localhost:8010/api/payments/courses" -Method GET -Headers $headers
    Write-Host "عدد الكورسات المتاحة: $($coursesResponse.Count)" -ForegroundColor Cyan
    
    if ($coursesResponse.Count -gt 0) {
        $firstCourse = $coursesResponse[0]
        Write-Host "اختبار الاشتراك في كورس: $($firstCourse.name) - السعر: $($firstCourse.cost)" -ForegroundColor Cyan
        
        # الاشتراك في الكورس
        $enrollmentResponse = Invoke-RestMethod -Uri "http://localhost:8010/api/payments/enroll/$($firstCourse.id)" -Method POST -Headers $headers
        Write-Host "تم الاشتراك بنجاح في الكورس!" -ForegroundColor Green
        Write-Host "معرف الاشتراك: $($enrollmentResponse.id)" -ForegroundColor Cyan
        
    } else {
        Write-Host "لا توجد كورسات متاحة للاشتراك" -ForegroundColor Yellow
    }
    
} catch {
    Write-Host "خطأ في الاشتراك في الكورس: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== انتهى فحص قاعدة البيانات ===" -ForegroundColor Green 