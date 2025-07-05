package com.microServices.payment.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long studentId;
    
    @Column(nullable = false)
    private Long courseId;
    
    @Column(nullable = false)
    private Boolean isCompleted = false;
    
    @Column(nullable = false)
    private LocalDateTime enrollmentDate;
    
    @Column(nullable = false)
    private Double amountPaid;
    
    @Column(nullable = false)
    private String paymentStatus = "COMPLETED"; // COMPLETED, PENDING, FAILED
    
    // Constructors
    public Enrollment() {
        this.enrollmentDate = LocalDateTime.now();
    }
    
    public Enrollment(Long studentId, Long courseId, Double amountPaid) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.amountPaid = amountPaid;
        this.enrollmentDate = LocalDateTime.now();
        this.isCompleted = false;
        this.paymentStatus = "COMPLETED";
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
} 