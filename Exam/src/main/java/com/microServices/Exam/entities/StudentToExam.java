package com.microServices.Exam.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_to_exam")
public class StudentToExam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long studentId;
    
    @Column(nullable = false)
    private Long examId;
    
    @Column(nullable = false)
    private Double studentDegree = 0.0;
    
    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, PASSED, FAILED
    
    @Column(nullable = false)
    private LocalDateTime submissionDate;
    
    @Column(nullable = false)
    private Long courseId;
    
    // Constructors
    public StudentToExam() {
        this.submissionDate = LocalDateTime.now();
    }
    
    public StudentToExam(Long studentId, Long examId, Long courseId) {
        this.studentId = studentId;
        this.examId = examId;
        this.courseId = courseId;
        this.submissionDate = LocalDateTime.now();
        this.studentDegree = 0.0;
        this.status = "PENDING";
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
    
    public Long getExamId() {
        return examId;
    }
    
    public void setExamId(Long examId) {
        this.examId = examId;
    }
    
    public Double getStudentDegree() {
        return studentDegree;
    }
    
    public void setStudentDegree(Double studentDegree) {
        this.studentDegree = studentDegree;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }
    
    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
} 