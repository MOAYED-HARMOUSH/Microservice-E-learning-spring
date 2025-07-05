package com.microServices.payment.dto;

import java.time.LocalDateTime;

public class EnrollmentDTO {
    
    private Long id;
    private Long studentId;
    private Long courseId;
    private Boolean isCompleted;
    private LocalDateTime enrollmentDate;
    private Double amountPaid;
    private String paymentStatus;
    
    // Course details (from Course Service)
    private String courseName;
    private String courseDescription;
    private Double courseCost;
    
    // Constructors
    public EnrollmentDTO() {}
    
    public EnrollmentDTO(Long id, Long studentId, Long courseId, Boolean isCompleted, 
                        LocalDateTime enrollmentDate, Double amountPaid, String paymentStatus) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.isCompleted = isCompleted;
        this.enrollmentDate = enrollmentDate;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    
    public Boolean getIsCompleted() {
        return isCompleted;
    }
    
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    
    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public Double getAmountPaid() {
        return amountPaid;
    }
    
    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getCourseDescription() {
        return courseDescription;
    }
    
    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
    
    public Double getCourseCost() {
        return courseCost;
    }
    
    public void setCourseCost(Double courseCost) {
        this.courseCost = courseCost;
    }
} 