package com.microServices.Exam.dto;

import java.util.List;

public class ExamDTO {
    
    private Long id;
    private String name;
    private String description;
    private Long courseId;
    private Double totalDegree;
    private Double passingDegree;
    private List<QuestionDTO> questions;
    
    // Constructors
    public ExamDTO() {}
    
    public ExamDTO(Long id, String name, String description, Long courseId, 
                   Double totalDegree, Double passingDegree) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseId = courseId;
        this.totalDegree = totalDegree;
        this.passingDegree = passingDegree;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    
    public Double getTotalDegree() {
        return totalDegree;
    }
    
    public void setTotalDegree(Double totalDegree) {
        this.totalDegree = totalDegree;
    }
    
    public Double getPassingDegree() {
        return passingDegree;
    }
    
    public void setPassingDegree(Double passingDegree) {
        this.passingDegree = passingDegree;
    }
    
    public List<QuestionDTO> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
} 