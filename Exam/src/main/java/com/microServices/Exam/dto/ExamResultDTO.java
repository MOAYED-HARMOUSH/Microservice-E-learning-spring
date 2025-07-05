package com.microServices.Exam.dto;

import java.time.LocalDateTime;

public class ExamResultDTO {
    
    private Long id;
    private Long studentId;
    private Long examId;
    private Long courseId;
    private Double studentDegree;
    private String status;
    private LocalDateTime submissionDate;
    
    // Course and Exam details
    private String courseName;
    private String examName;
    private Double examTotalDegree;
    private Double passingDegree;
    
    // Constructors
    public ExamResultDTO() {}
    
    public ExamResultDTO(Long id, Long studentId, Long examId, Long courseId, 
                        Double studentDegree, String status, LocalDateTime submissionDate) {
        this.id = id;
        this.studentId = studentId;
        this.examId = examId;
        this.courseId = courseId;
        this.studentDegree = studentDegree;
        this.status = status;
        this.submissionDate = submissionDate;
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
    
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getExamName() {
        return examName;
    }
    
    public void setExamName(String examName) {
        this.examName = examName;
    }
    
    public Double getExamTotalDegree() {
        return examTotalDegree;
    }
    
    public void setExamTotalDegree(Double examTotalDegree) {
        this.examTotalDegree = examTotalDegree;
    }
    
    public Double getPassingDegree() {
        return passingDegree;
    }
    
    public void setPassingDegree(Double passingDegree) {
        this.passingDegree = passingDegree;
    }
} 