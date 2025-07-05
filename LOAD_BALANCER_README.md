# Load Balancer Implementation for Exam Service

## نظرة عامة
تم تنفيذ نظام موازنة التحميل (Load Balancing) لخدمة الامتحانات باستخدام `spring-cloud-starter-loadbalancer` الجديد بدلاً من Netflix Ribbon المهمل.

## المكونات الرئيسية

### 1. التبعيات (Dependencies)
```xml
<!-- Spring Cloud LoadBalancer (Modern Alternative to Ribbon) -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

### 2. ملفات التكوين

#### Exam Service Configuration (`application.yml`)
```yaml
spring:
  cloud:
    loadbalancer:
      cache:
        enabled: true
        ttl: 35s
      health-check:
        initial-delay: 0
        interval: 25s
      configurations: default
      enabled: true
      ribbon:
        enabled: false  # تعطيل Ribbon

# External Services URLs with Load Balancing
course:
  service:
    url: lb://COURSEMANAGEMENT

payment:
  service:
    url: lb://PAYMENT
```

#### Gateway Configuration (`application.properties`)
```properties
# Load Balancer Configuration
spring.cloud.loadbalancer.cache.enabled=true
spring.cloud.loadbalancer.cache.ttl=35s
spring.cloud.loadbalancer.health-check.initial-delay=0
spring.cloud.loadbalancer.health-check.interval=25s
spring.cloud.loadbalancer.configurations=default
spring.cloud.loadbalancer.enabled=true

# Gateway Routes with Load Balancing
spring.cloud.gateway.server.webmvc.routes[3].id=exam-service
spring.cloud.gateway.server.webmvc.routes[3].uri=lb://EXAM
spring.cloud.gateway.server.webmvc.routes[3].predicates[0]=Path=/api/exams/**
```

### 3. فئات التكوين

#### LoadBalancerConfig.java
```java
@Configuration
@LoadBalancerClient(value = "EXAM", configuration = LoadBalancerConfig.class)
public class LoadBalancerConfig {
    
    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(
            Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory) {
        
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        
        return new RoundRobinLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
                name
        );
    }
}
```

#### RestTemplateConfig.java
```java
@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### 4. نقاط النهاية للرصد

#### LoadBalancerHealthController.java
- `/api/loadbalancer/health` - فحص صحة الخدمة
- `/api/loadbalancer/instances` - الحصول على جميع النسخ
- `/api/loadbalancer/stats` - إحصائيات اللود بالانس
- `/api/loadbalancer/services` - جميع الخدمات المسجلة

## كيفية التشغيل

### 1. تشغيل الخدمات الأساسية
```bash
# 1. Config Server
cd configServer
mvn spring-boot:run

# 2. Discovery Server (Eureka)
cd discovery
mvn spring-boot:run

# 3. Gateway
cd gateway
mvn spring-boot:run
```

### 2. تشغيل نسخ متعددة من Exam Service
```bash
# استخدام السكريبت الجاهز
start-exam-instances.bat

# أو يدوياً
cd Exam
# Terminal 1
set SERVER_PORT=8040 && mvn spring-boot:run

# Terminal 2
set SERVER_PORT=8041 && mvn spring-boot:run

# Terminal 3
set SERVER_PORT=8042 && mvn spring-boot:run
```

## اختبار اللود بالانس

### 1. باستخدام السكريبت
```bash
test-load-balancer.bat
```

### 2. باستخدام Postman
- استيراد ملف `LoadBalancer-Tests.postman_collection.json`
- تشغيل الطلبات المتعددة لمراقبة التوزيع

### 3. باستخدام cURL
```bash
# فحص صحة اللود بالانس
curl -X GET "http://localhost:8010/api/exams/loadbalancer/health"

# الحصول على النسخ
curl -X GET "http://localhost:8010/api/exams/loadbalancer/instances"

# اختبار التوزيع (10 طلبات)
for i in {1..10}; do
    curl -X GET "http://localhost:8010/api/exams/loadbalancer/health" | grep "port"
    sleep 1
done
```

## مراقبة النظام

### 1. Eureka Dashboard
- URL: http://localhost:8761
- مراقبة الخدمات المسجلة والنسخ النشطة

### 2. Actuator Endpoints
- Instance 1: http://localhost:8040/actuator/health
- Instance 2: http://localhost:8041/actuator/health
- Instance 3: http://localhost:8042/actuator/health

### 3. Load Balancer Endpoints
- Health: http://localhost:8010/api/exams/loadbalancer/health
- Stats: http://localhost:8010/api/exams/loadbalancer/stats
- Instances: http://localhost:8010/api/exams/loadbalancer/instances

## استراتيجيات اللود بالانس

### 1. Round Robin (المستخدمة حالياً)
- توزيع الطلبات بالتساوي بين النسخ
- مناسبة للأنظمة المتوازنة

### 2. Random (بديل)
```java
return new RandomLoadBalancer(
    loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
    name
);
```

### 3. Weighted Round Robin (متقدم)
- يمكن إضافة أوزان مختلفة للنسخ
- يتطلب تكوين إضافي

## المميزات

### 1. الكاش (Caching)
- تخزين مؤقت لمعلومات النسخ لمدة 35 ثانية
- تقليل الطلبات إلى Eureka

### 2. فحص الصحة (Health Check)
- فحص صحة النسخ كل 25 ثانية
- إزالة النسخ غير المتاحة تلقائياً

### 3. التكيف (Adaptive)
- إضافة/إزالة نسخ ديناميكياً
- لا حاجة لإعادة تشغيل النظام

### 4. المراقبة (Monitoring)
- نقاط نهاية شاملة للمراقبة
- إحصائيات مفصلة عن الأداء

## استكشاف الأخطاء

### 1. مشاكل شائعة
```bash
# فحص حالة الخدمات
curl http://localhost:8761/eureka/apps

# فحص سجلات الخدمة
tail -f Exam/logs/application.log

# فحص استخدام الذاكرة
jstat -gc <pid>
```

### 2. إعادة تشغيل الخدمات
```bash
# إيقاف جميع النسخ
taskkill /F /IM java.exe

# إعادة تشغيل
start-exam-instances.bat
```

## الأداء والتحسين

### 1. إعدادات JVM
```bash
set JAVA_OPTS=-Xmx512m -Xms256m -XX:+UseG1GC
```

### 2. إعدادات قاعدة البيانات
```yaml
spring:
  jpa:
    hibernate:
      jdbc:
        batch_size: 20
    properties:
      hibernate:
        order_inserts: true
        order_updates: true
```

### 3. إعدادات الكاش
```yaml
spring:
  cloud:
    loadbalancer:
      cache:
        ttl: 60s  # زيادة مدة الكاش
```

## الخلاصة

تم تنفيذ نظام لود بالانس متكامل ومتطور لخدمة الامتحانات مع:

✅ استخدام `spring-cloud-starter-loadbalancer` الجديد  
✅ استراتيجية Round Robin للتوزيع  
✅ نظام مراقبة شامل  
✅ أدوات اختبار جاهزة  
✅ تكوين مرن وقابل للتخصيص  
✅ دعم للتشغيل المتعدد النسخ  

النظام جاهز للاستخدام في بيئة الإنتاج مع إمكانية التوسع والتحسين المستمر. 