# إصلاح مشاكل خدمة الدفع

## المشاكل المكتشفة

### 1. مشكلة "Insufficient balance"
**السبب:** الطالب لا يوجد في قاعدة البيانات أو لا يحتوي على رصيد كافي.

**الحل:**
1. تشغيل سكريبت إصلاح قاعدة البيانات:
   ```bash
   .\fix-database.bat
   ```

2. تشغيل سكريبت فحص قاعدة البيانات:
   ```powershell
   .\check-database.ps1
   ```

### 2. مشكلة الاتصال بين الخدمات
**السبب:** الخدمات تحاول الاتصال مباشرة بدلاً من استخدام Gateway.

**الحل:**
- تم تحديث إعدادات الخدمات لاستخدام Gateway على المنفذ 8010

### 3. مشكلة في استعلام قاعدة البيانات
**السبب:** العلاقة بين جداول `students` و `users` غير صحيحة.

**الحل:**
- تشغيل سكريبت SQL لإصلاح البيانات

## خطوات الإصلاح

### الخطوة 1: إصلاح قاعدة البيانات
```bash
# تشغيل سكريبت إصلاح قاعدة البيانات
.\fix-database.bat
```

### الخطوة 2: فحص قاعدة البيانات
```powershell
# تشغيل سكريبت فحص قاعدة البيانات
.\check-database.ps1
```

### الخطوة 3: اختبار الاشتراك في كورس
```bash
# تسجيل الدخول
curl -X POST http://localhost:8010/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"STUDENT@2.c","password":"password123"}'

# الاشتراك في كورس
curl -X POST http://localhost:8010/api/payments/enroll/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## السكريبتات المتاحة

1. **`fix-database.bat`** - إصلاح قاعدة البيانات
2. **`check-database.ps1`** - فحص قاعدة البيانات
3. **`diagnose-services.ps1`** - تشخيص مشاكل الاتصال
4. **`start-all-services-fixed.bat`** - بدء جميع الخدمات

## المنافذ المستخدمة

- Config Server: 8080
- Discovery Server (Eureka): 8761
- User Service: 8090
- Course Management: 8082
- Payment Service: 8050
- Gateway: 8010

## ملاحظات مهمة

1. تأكد من تشغيل جميع الخدمات بالترتيب الصحيح
2. انتظر 30 ثانية بعد بدء الخدمات حتى تتسجل في Eureka
3. تأكد من وجود رصيد كافي في محفظة الطالب
4. تأكد من وجود كورسات معتمدة في قاعدة البيانات 