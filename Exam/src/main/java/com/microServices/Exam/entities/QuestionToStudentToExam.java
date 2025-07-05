package com.microServices.Exam.entities;

import javax.persistence.*;

@Entity
@Table(name = "question_to_student_to_exam")
public class QuestionToStudentToExam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long questionId;
    
    @Column(nullable = false)
    private Long studentToExamId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentAnswer studentAnswer;
    
    // Enum for student answers
    public enum StudentAnswer {
        ANSWER1, ANSWER2, ANSWER3, ANSWER4
    }
    
    // Constructors
    public QuestionToStudentToExam() {}
    
    public QuestionToStudentToExam(Long questionId, Long studentToExamId, StudentAnswer studentAnswer) {
        this.questionId = questionId;
        this.studentToExamId = studentToExamId;
        this.studentAnswer = studentAnswer;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    
    public Long getStudentToExamId() {
        return studentToExamId;
    }
    
    public void setStudentToExamId(Long studentToExamId) {
        this.studentToExamId = studentToExamId;
    }
    
    public StudentAnswer getStudentAnswer() {
        return studentAnswer;
    }
    
    public void setStudentAnswer(StudentAnswer studentAnswer) {
        this.studentAnswer = studentAnswer;
    }
} 