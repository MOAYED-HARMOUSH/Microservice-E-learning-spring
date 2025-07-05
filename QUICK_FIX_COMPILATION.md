# حل سريع لمشكلة الترجمة

## المشكلة
```
[ERROR] cannot find symbol
  symbol:   class ExamDTO
  location: class com.microServices.Exam.controllers.ExamController
```

## الحل
تم إضافة import statement مفقود في `ExamController.java`:

```java
import com.microServices.Exam.dto.ExamDTO;
```

## خطوات الإصلاح

### الخطوة 1: اختبار الترجمة
```bash
.\test-compilation.bat
```

### الخطوة 2: إذا نجحت الترجمة، شغل الخدمة
```bash
.\restart-exam-simple.bat
```

### الخطوة 3: اختبار الاتصال
```powershell
.\test-exam-connections.ps1
```

## إذا استمرت المشكلة

1. **تأكد من وجود الملف:**
   ```
   Exam/src/main/java/com/microServices/Exam/dto/ExamDTO.java
   ```

2. **تأكد من package declaration:**
   ```java
   package com.microServices.Exam.dto;
   ```

3. **تأكد من import statement:**
   ```java
   import com.microServices.Exam.dto.ExamDTO;
   ```

## السكريبتات المتاحة

- `test-compilation.bat` - اختبار الترجمة فقط
- `restart-exam-simple.bat` - إعادة تشغيل الخدمة
- `test-exam-connections.ps1` - اختبار الاتصال 