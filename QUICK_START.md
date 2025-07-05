# Quick Start Guide - Load Balancer

## 🚀 تشغيل سريع للود بالانس

### 1. تشغيل الخدمات الأساسية أولاً

```bash
# Terminal 1 - Config Server
cd configServer
mvn spring-boot:run

# Terminal 2 - Discovery Server (Eureka)
cd discovery
mvn spring-boot:run

# Terminal 3 - Gateway
cd gateway
mvn spring-boot:run
```

### 2. تشغيل نسخ متعددة من Exam Service

#### الخيار الأول: PowerShell (موصى به)
```powershell
.\start-exam-instances.ps1
```

#### الخيار الثاني: Batch File
```cmd
.\start-exam-instances-fixed.bat
```

#### الخيار الثالث: يدوياً
```bash
# Terminal 4
cd Exam
set SERVER_PORT=8040
mvn spring-boot:run

# Terminal 5
cd Exam
set SERVER_PORT=8041
mvn spring-boot:run

# Terminal 6
cd Exam
set SERVER_PORT=8042
mvn spring-boot:run
```

### 3. اختبار اللود بالانس

#### الخيار الأول: PowerShell
```powershell
.\test-load-balancer.ps1
```

#### الخيار الثاني: Batch File
```cmd
.\test-load-balancer.bat
```

#### الخيار الثالث: cURL
```bash
# فحص صحة اللود بالانس
curl -X GET "http://localhost:8010/api/exams/loadbalancer/health"

# اختبار التوزيع (10 طلبات)
for i in {1..10}; do
    curl -X GET "http://localhost:8010/api/exams/loadbalancer/health" | grep "port"
    sleep 1
done
```

### 4. مراقبة النظام

- **Eureka Dashboard**: http://localhost:8761
- **Gateway Health**: http://localhost:8010/actuator/health
- **Instance 1**: http://localhost:8040/actuator/health
- **Instance 2**: http://localhost:8041/actuator/health
- **Instance 3**: http://localhost:8042/actuator/health

### 5. نقاط النهاية للود بالانس

- **Health Check**: http://localhost:8010/api/exams/loadbalancer/health
- **Instances**: http://localhost:8010/api/exams/loadbalancer/instances
- **Stats**: http://localhost:8010/api/exams/loadbalancer/stats
- **Services**: http://localhost:8010/api/exams/loadbalancer/services

## 🔧 استكشاف الأخطاء

### إذا لم تعمل السكريبتات:

1. **تأكد من تشغيل PowerShell كمسؤول**
2. **تحقق من سياسة التنفيذ**:
   ```powershell
   Get-ExecutionPolicy
   Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
   ```

3. **تشغيل يدوي**:
   ```bash
   cd Exam
   set SERVER_PORT=8040 && mvn spring-boot:run
   ```

### إذا لم تظهر النسخ في Eureka:

1. **تحقق من تشغيل Discovery Server**
2. **انتظر 30-60 ثانية للتسجيل**
3. **تحقق من السجلات**:
   ```bash
   tail -f Exam/logs/application.log
   ```

## 📊 التحقق من عمل اللود بالانس

### 1. في Eureka Dashboard:
- يجب أن ترى 3 نسخ من خدمة EXAM
- كل نسخة على منفذ مختلف (8040, 8041, 8042)

### 2. في اختبار التوزيع:
- يجب أن ترى الطلبات تتوزع بين النسخ الثلاث
- كل طلب يجب أن يعطي منفذ مختلف

### 3. في Load Balancer Stats:
- يجب أن يظهر عدد النسخ النشطة = 3
- نوع اللود بالانس = Round Robin

## 🎯 النتيجة المتوقعة

عند تشغيل الاختبار، يجب أن ترى:
```
Request 1: Port: 8040
Request 2: Port: 8041
Request 3: Port: 8042
Request 4: Port: 8040
Request 5: Port: 8041
...
```

هذا يؤكد أن اللود بالانس يعمل بشكل صحيح ويوزع الطلبات بالتساوي بين النسخ الثلاث. 