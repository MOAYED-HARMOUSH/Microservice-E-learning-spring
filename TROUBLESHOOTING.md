# دليل استكشاف الأخطاء - Exam Service

## 🔧 المشاكل الشائعة والحلول

### 1. مشكلة "Not a managed type"

**الخطأ:**
```
Not a managed type: class com.microServices.Exam.entities.StudentToExam
```

**الحل:**
1. تأكد من وجود `@Entity` annotation في أعلى الكلاس
2. تأكد من أن الكلاس في الباكيج الصحيح
3. أضف `@EntityScan` في Application class

**تم إصلاحه في:**
- `ExamApplication.java` - أضفنا `@EntityScan` و `@EnableJpaRepositories`
- `application.yml` - أضفنا `packages-to-scan`

### 2. مشكلة Compilation

**الحل:**
```bash
# تنظيف وإعادة بناء
.\clean-and-build.bat

# أو يدوياً
cd Exam
mvn clean compile
```

### 3. مشكلة Database Connection

**تأكد من:**
- تشغيل MySQL
- صحة بيانات الاتصال في `application.yml`
- وجود قاعدة البيانات

### 4. مشكلة Port Already in Use

**الحل:**
```bash
# إيقاف العمليات على المنافذ
netstat -ano | findstr :8040
taskkill /PID <PID> /F
```

### 5. مشكلة Eureka Registration

**تأكد من:**
- تشغيل Discovery Server
- صحة إعدادات Eureka في `application.yml`
- انتظار 30-60 ثانية للتسجيل

## 🚀 خطوات التشغيل الصحيحة

### 1. تشغيل الخدمات الأساسية
```bash
# Terminal 1 - Config Server
cd configServer
mvn spring-boot:run

# Terminal 2 - Discovery Server
cd discovery
mvn spring-boot:run

# Terminal 3 - Gateway
cd gateway
mvn spring-boot:run
```

### 2. تنظيف وإعادة بناء Exam Service
```bash
.\clean-and-build.bat
```

### 3. تشغيل نسخ متعددة
```bash
.\start-exam-instances-enhanced.bat
```

### 4. اختبار اللود بالانس
```bash
.\test-load-balancer.bat
```

## 📊 مراقبة النظام

### 1. Eureka Dashboard
- URL: http://localhost:8761
- تحقق من تسجيل الخدمات

### 2. Health Checks
- Instance 1: http://localhost:8040/actuator/health
- Instance 2: http://localhost:8041/actuator/health
- Instance 3: http://localhost:8042/actuator/health

### 3. Load Balancer Endpoints
- Health: http://localhost:8010/api/exams/loadbalancer/health
- Stats: http://localhost:8010/api/exams/loadbalancer/stats

## 🔍 فحص السجلات

### 1. Maven Logs
```bash
cd Exam
mvn spring-boot:run -X
```

### 2. Application Logs
```bash
# في كل terminal من الخدمات
tail -f logs/application.log
```

## ⚡ حلول سريعة

### إذا لم تعمل الخدمة:
1. `.\clean-and-build.bat`
2. `.\start-exam-instances-enhanced.bat`

### إذا لم يظهر في Eureka:
1. انتظر 60 ثانية
2. تحقق من إعدادات Eureka
3. أعد تشغيل Discovery Server

### إذا لم يعمل اللود بالانس:
1. تحقق من تشغيل Gateway
2. تحقق من إعدادات Load Balancer
3. أعد تشغيل جميع الخدمات

## 📞 للحصول على مساعدة إضافية

إذا استمرت المشكلة:
1. انسخ رسالة الخطأ كاملة
2. تحقق من إصدارات Java و Maven
3. تأكد من تشغيل جميع الخدمات الأساسية 