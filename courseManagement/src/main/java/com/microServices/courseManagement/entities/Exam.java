package com.microServices.courseManagement.entities;

import javax.persistence.*;

@Entity
@Table(name = "exams")
public class Exam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long courseId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private Double degree;
    
    @Column(nullable = false)
    private Double successDegree;
    
    // Constructors
    public Exam() {}
    
    public Exam(Long id, Long courseId, String name, String description, Double degree, Double successDegree) {
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