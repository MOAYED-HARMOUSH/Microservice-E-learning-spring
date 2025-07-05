package com.microServices.Exam.repositories;

import com.microServices.Exam.entities.StudentToExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentToExamRepository extends JpaRepository<StudentToExam, Long> {
    
    // البحث عن امتحانات الطالب
    List<StudentToExam> findByStudentId(Long studentId);
    
    // البحث عن امتحان محدد للطالب
    Optional<StudentToExam> findByStudentIdAndExamId(Long studentId, Long examId);
    
    // التحقق من وجود امتحان للطالب
    boolean existsByStudentIdAndExamId(Long studentId, Long examId);
    
    // البحث عن جميع نتائج امتحان محدد
    List<StudentToExam> findByExamId(Long examId);
    
    // البحث عن نتائج الطلاب في كورس محدد
    List<StudentToExam> findByCourseId(Long courseId);
    
    // البحث عن نتائج طالب محدد في كورس محدد
    List<StudentToExam> findByStudentIdAndCourseId(Long studentId, Long courseId);
} 