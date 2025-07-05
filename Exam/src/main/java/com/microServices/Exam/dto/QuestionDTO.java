package com.microServices.Exam.dto;

public class QuestionDTO {
    
    private Long id;
    private String questionText;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String correctAnswer; // ANSWER1, ANSWER2, ANSWER3, ANSWER4
    private Double degree;
    private Long examId;
    
    // Constructors
    public QuestionDTO() {}
    
    public QuestionDTO(Long id, String questionText, String answer1, String answer2, 
                      String answer3, String answer4, String correctAnswer, Double degree, Long examId) {
        this.id = id;
        this.questionText = questionText;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correctAnswer;
        this.degree = degree;
        this.examId = examId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getQuestionText() {
        return questionText;
    }
    
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
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
    
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    public Double getDegree() {
        return degree;
    }
    
    public void setDegree(Double degree) {
        this.degree = degree;
    }
    
    public Long getExamId() {
        return examId;
    }
    
    public void setExamId(Long examId) {
        this.examId = examId;
    }
} 