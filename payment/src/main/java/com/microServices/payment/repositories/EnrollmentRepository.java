package com.microServices.payment.repositories;

import com.microServices.payment.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    // البحث عن اشتراكات الطالب
    List<Enrollment> findByStudentId(Long studentId);
    
    // البحث عن اشتراك محدد للطالب في كورس محدد
    Enrollment findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    // التحقق من وجود اشتراك
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    
    // البحث عن جميع اشتراكات كورس محدد
    List<Enrollment> findByCourseId(Long courseId);
} 