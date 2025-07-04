package com.microServices.courseManagement.dto;

import com.microServices.courseManagement.entities.Course;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CourseDTO {
    
    private Long id;
    
    @NotBlank(message = "Course name is required")
    private String name;
    
    private String description;
    
    private Long instructorId;
    
    @NotNull(message = "Course cost is required")
    @Positive(message = "Course cost must be positive")
    private Double cost;
    
    private Course.CourseStatus status;
    
    // Constructors
    public CourseDTO() {}
    
    public CourseDTO(Long id, String name, String description, Long instructorId, Double cost, Course.CourseStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.instructorId = instructorId;
        this.cost = cost;
        this.status = status;
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
    
    public Long getInstructorId() {
        return instructorId;
    }
    
    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }
    
    public Double getCost() {
        return cost;
    }
    
    public void setCost(Double cost) {
        this.cost = cost;
    }
    
    public Course.CourseStatus getStatus() {
        return status;
    }
    
    public void setStatus(Course.CourseStatus status) {
        this.status = status;
    }
} 