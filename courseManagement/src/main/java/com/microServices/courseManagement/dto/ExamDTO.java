package com.microServices.courseManagement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ExamDTO {
    
    private Long id;
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
    @NotBlank(message = "Exam name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Exam degree is required")
    @Positive(message = "Exam degree must be positive")
    private Double degree;
    
    @NotNull(message = "Success degree is required")
    @Positive(message = "Success degree must be positive")
    private Double successDegree;
    
    // Constructors
    public ExamDTO() {}
    
    public ExamDTO(Long id, Long courseId, String name, String description, Double degree, Double successDegree) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.degree = degree;
        this.successDegree = successDegree;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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
    
    public Double getDegree() {
        return degree;
    }
    
    public void setDegree(Double degree) {
        this.degree = degree;
    }
    
    public Double getSuccessDegree() {
        return successDegree;
    }
    
    public void setSuccessDegree(Double successDegree) {
        this.successDegree = successDegree;
    }
} 