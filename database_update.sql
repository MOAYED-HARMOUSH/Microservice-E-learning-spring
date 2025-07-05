-- تحديث قاعدة البيانات لإضافة حقل degree للأسئلة
-- قم بتشغيل هذا الملف على قاعدة بيانات courseManagement

USE courseManagement;

-- إضافة عمود degree إلى جدول questions إذا لم يكن موجوداً
ALTER TABLE questions ADD COLUMN IF NOT EXISTS degree DOUBLE DEFAULT 1.0;

-- تحديث الأسئلة الموجودة لتكون لها درجة افتراضية
UPDATE questions SET degree = 1.0 WHERE degree IS NULL;

-- عرض الأسئلة للتأكد من التحديث
SELECT id, question, degree FROM questions LIMIT 10; 