package com.microServices.courseManagement.repositories;

import com.microServices.courseManagement.entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    List<Exam> findByCourseId(Long courseId);
} 