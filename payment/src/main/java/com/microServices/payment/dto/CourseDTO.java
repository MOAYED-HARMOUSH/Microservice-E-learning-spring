package com.microServices.payment.dto;

public class CourseDTO {
    
    private Long id;
    private String name;
    private String description;
    private Double cost;
    private Long instructorId;
    private String status; // PENDING, APPROVED, REJECTED
    private String errorMessage;
    
    // Constructors
    public CourseDTO() {}
    
    public CourseDTO(Long id, String name, String description, Double cost, Long instructorId, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.instructorId = instructorId;
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
    
    public Double getCost() {
        return cost;
    }
    
    public void setCost(Double cost) {
        this.cost = cost;
    }
    
    public Long getInstructorId() {
        return instructorId;
    }
    
    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    // Helper method for backward compatibility
    public String getTitle() {
        return name;
    }
    
    public void setTitle(String title) {
        this.name = title;
    }
} 