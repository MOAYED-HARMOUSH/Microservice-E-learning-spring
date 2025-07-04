package com.microServices.courseManagement.dto;

import com.microServices.courseManagement.entities.Question;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class QuestionDTO {
    
    private Long id;
    
    @NotNull(message = "Exam ID is required")
    private Long examId;
    
    @NotBlank(message = "Question text is required")
    private String question;
    
    @NotBlank(message = "Answer 1 is required")
    private String answer1;
    
    @NotBlank(message = "Answer 2 is required")
    private String answer2;
    
    @NotBlank(message = "Answer 3 is required")
    private String answer3;
    
    @NotBlank(message = "Answer 4 is required")
    private String answer4;
    
    @NotNull(message = "Correct answer is required")
    private Question.CorrectAnswer correctAnswer;
    
    // Constructors
    public QuestionDTO() {}
    
    public QuestionDTO(Long id, Long examId, String question, String answer1, String answer2, String answer3, String answer4, Question.CorrectAnswer correctAnswer) {
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
    
    public Question.CorrectAnswer getCorrectAnswer() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(Question.CorrectAnswer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
} 