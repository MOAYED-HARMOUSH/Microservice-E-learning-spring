package com.microServices.courseManagement.repositories;

import com.microServices.courseManagement.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByInstructorId(Long instructorId);
    
    List<Course> findByStatus(Course.CourseStatus status);
    
    List<Course> findByInstructorIdAndStatus(Long instructorId, Course.CourseStatus status);
} 