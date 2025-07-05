-- فحص وإصلاح قاعدة البيانات
USE user_microservice;

-- عرض جميع المستخدمين
SELECT '=== جميع المستخدمين ===' as info;
SELECT id, email, role, created_at FROM users;

-- عرض جميع الطلاب
SELECT '=== جميع الطلاب ===' as info;
SELECT s.id, s.status, s.wallet_balance, s.user_id, u.email 
FROM students s 
LEFT JOIN users u ON s.user_id = u.id;

-- البحث عن الطالب مع user_id = 4 (من الـ token)
SELECT '=== البحث عن الطالب مع user_id = 4 ===' as info;
SELECT s.id, s.status, s.wallet_balance, s.user_id, u.email 
FROM students s 
LEFT JOIN users u ON s.user_id = u.id 
WHERE u.id = 4;

-- البحث عن الطالب مع id = 2
SELECT '=== البحث عن الطالب مع id = 2 ===' as info;
SELECT s.id, s.status, s.wallet_balance, s.user_id, u.email 
FROM students s 
LEFT JOIN users u ON s.user_id = u.id 
WHERE s.id = 2;

-- إنشاء طالب جديد إذا لم يكن موجود
INSERT IGNORE INTO students (status, user_id, wallet_balance) 
SELECT 'ACTIVE', id, 1000.0 
FROM users 
WHERE id = 4 AND id NOT IN (SELECT user_id FROM students WHERE user_id IS NOT NULL);

-- إضافة رصيد للطالب الموجود
UPDATE students 
SET wallet_balance = 1000.0 
WHERE user_id = 4;

-- عرض النتيجة النهائية
SELECT '=== النتيجة النهائية ===' as info;
SELECT s.id, s.status, s.wallet_balance, s.user_id, u.email 
FROM students s 
LEFT JOIN users u ON s.user_id = u.id 
WHERE u.id = 4; 