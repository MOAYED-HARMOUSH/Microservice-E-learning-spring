package com.microServices.Exam.dto;

import com.microServices.Exam.entities.QuestionToStudentToExam.StudentAnswer;
import java.util.List;
import java.util.Map;

public class ExamSubmissionDTO {
    
    private Long examId;
    private Long studentId;
    private Map<Long, StudentAnswer> answers; // questionId -> studentAnswer
    
    // Constructors
    public ExamSubmissionDTO() {}
    
    public ExamSubmissionDTO(Long examId, Long studentId, Map<Long, StudentAnswer> answers) {
        this.examId = examId;
        this.studentId = studentId;
        this.answers = answers;
    }
    
    // Getters and Setters
    public Long getExamId() {
        return examId;
    }
    
    public void setExamId(Long examId) {
        this.examId = examId;
    }
    
    public Long getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    
    public Map<Long, StudentAnswer> getAnswers() {
        return answers;
    }
    
    public void setAnswers(Map<Long, StudentAnswer> answers) {
        this.answers = answers;
    }
} 