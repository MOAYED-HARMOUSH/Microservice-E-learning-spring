package com.microServices.Exam.repositories;

import com.microServices.Exam.entities.QuestionToStudentToExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionToStudentToExamRepository extends JpaRepository<QuestionToStudentToExam, Long> {
    
    // البحث عن إجابات طالب في امتحان محدد
    List<QuestionToStudentToExam> findByStudentToExamId(Long studentToExamId);
    
    // البحث عن إجابة سؤال محدد في امتحان طالب محدد
    QuestionToStudentToExam findByQuestionIdAndStudentToExamId(Long questionId, Long studentToExamId);
    
    // البحث عن جميع إجابات سؤال محدد
    List<QuestionToStudentToExam> findByQuestionId(Long questionId);
} 