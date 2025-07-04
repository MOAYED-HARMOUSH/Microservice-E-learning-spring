package com.microServices.courseManagement.dto;

import javax.validation.constraints.NotNull;

public class CourseApprovalDTO {
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
    @NotNull(message = "Approval action is required")
    private Boolean approved;
    
    // Constructors
    public CourseApprovalDTO() {}
    
    public CourseApprovalDTO(Long courseId, Boolean approved) {
        this.courseId = courseId;
        this.approved = approved;
    }
    
    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    
    public Boolean getApproved() {
        return approved;
    }
    
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}