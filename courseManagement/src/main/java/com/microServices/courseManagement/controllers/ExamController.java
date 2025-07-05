package com.microServices.courseManagement.controllers;

import com.microServices.courseManagement.dto.ExamDTO;
import com.microServices.courseManagement.dto.ExamWithQuestionsDTO;
import com.microServices.courseManagement.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/courses/exams")
public class ExamController {
    
    @Autowired
    private ExamService examService;
    
    // Public endpoints - accessible by everyone
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ExamDTO>> getExamsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(examService.getExamsByCourse(courseId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> getExamById(@PathVariable Long id) {
        return ResponseEntity.ok(examService.getExamById(id));
    }
    
    @GetMapping("/{id}/with-questions")
    public ResponseEntity<ExamWithQuestionsDTO> getExamWithQuestions(@PathVariable Long id) {
        return ResponseEntity.ok(examService.getExamWithQuestions(id));
    }
    
    // Instructor endpoints - only instructors can access
    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<ExamDTO>> getAllExams() {
        return ResponseEntity.ok(examService.getAllExams());
    }
    
    @PostMapping
//    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ExamDTO> createExam(@Valid @RequestBody ExamDTO examDTO) {
        return ResponseEntity.ok(examService.createExam(examDTO));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ExamDTO> updateExam(@PathVariable Long id, @Valid @RequestBody ExamDTO examDTO) {
        return ResponseEntity.ok(examService.updateExam(id, examDTO));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.ok().build();
    }
} 