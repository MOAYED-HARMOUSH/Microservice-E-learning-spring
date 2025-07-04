package com.microServices.courseManagement.entities;

import javax.persistence.*;

@Entity
@Table(name = "questions")
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long examId;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;
    
    @Column(nullable = false)
    private String answer1;
    
    @Column(nullable = false)
    private String answer2;
    
    @Column(nullable = false)
    private String answer3;
    
    @Column(nullable = false)
    private String answer4;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CorrectAnswer correctAnswer;
    
    public enum CorrectAnswer {
        ANSWER1,
        ANSWER2,
        ANSWER3,
        ANSWER4
    }
    
    // Constructors
    public Question() {}
    
    public Question(Long id, Long examId, String question, String answer1, String answer2, String answer3, String answer4, CorrectAnswer correctAnswer) {
        this.id = id;
        this.examId = examId;
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correctAnswer;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getExamId() {
        return examId;
    }
    
    public void setExamId(Long examId) {
        this.examId = examId;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getAnswer1() {
        return answer1;
    }
    
    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }
    
    public String getAnswer2() {
        return answer2;
    }
    
    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }
    
    public String getAnswer3() {
        return answer3;
    }
    
    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }
    
    public String getAnswer4() {
        return answer4;
    }
    
    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }
    
    public CorrectAnswer getCorrectAnswer() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(CorrectAnswer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
} 