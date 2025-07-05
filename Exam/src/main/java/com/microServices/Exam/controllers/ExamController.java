package com.microServices.Exam.controllers;

import com.microServices.Exam.dto.ExamResultDTO;
import com.microServices.Exam.dto.ExamSubmissionDTO;
import com.microServices.Exam.services.ExamService;
import com.microServices.Exam.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    
    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);
    
    @Autowired
    private ExamService examService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * تقديم امتحان من قبل الطالب
     */
    @PostMapping("/submit")
    public ResponseEntity<ExamResultDTO> submitExam(@RequestBody ExamSubmissionDTO submissionDTO, 
                                                   HttpServletRequest request) {
        logger.info("Received exam submission request for examId: {}", submissionDTO.getExamId());
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            logger.error("Invalid or missing token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isStudent(token)) {
            logger.error("User is not a student");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Long studentId = jwtUtil.getStudentId(token);
        if (studentId == null) {
            logger.error("Student ID not found in token");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // تعيين studentId من التوكن
        submissionDTO.setStudentId(studentId);
        
        try {
            ExamResultDTO result = examService.submitExam(submissionDTO, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException e) {
            logger.error("Exam submission failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error during exam submission: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * الحصول على نتيجة امتحان طالب محدد
     */
    @GetMapping("/result/{examId}")
    public ResponseEntity<ExamResultDTO> getStudentExamResult(@PathVariable Long examId, 
                                                             HttpServletRequest request) {
        logger.info("Received exam result request for examId: {}", examId);
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            logger.error("Invalid or missing token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isStudent(token)) {
            logger.error("User is not a student");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Long studentId = jwtUtil.getStudentId(token);
        if (studentId == null) {
            logger.error("Student ID not found in token");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        try {
            ExamResultDTO result = examService.getStudentExamResult(studentId, examId, token);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            logger.error("Failed to get exam result: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error getting exam result: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * الحصول على نتائج جميع الطلاب في امتحان محدد (للمدرس)
     */
    @GetMapping("/instructor/results/{examId}")
    public ResponseEntity<List<ExamResultDTO>> getExamResultsForInstructor(@PathVariable Long examId, 
                                                                          HttpServletRequest request) {
        logger.info("Received instructor exam results request for examId: {}", examId);
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            logger.error("Invalid or missing token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isInstructor(token)) {
            logger.error("User is not an instructor");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        try {
            List<ExamResultDTO> results = examService.getExamResultsForInstructor(examId, token);
            return ResponseEntity.ok(results);
        } catch (RuntimeException e) {
            logger.error("Failed to get exam results: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error getting exam results: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * اختبار معلومات الـ token
     */
    @GetMapping("/test/token-info")
    public ResponseEntity<String> getTokenInfo(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        StringBuilder info = new StringBuilder();
        info.append("=== JWT Token Info ===\n");
        info.append("User ID: ").append(jwtUtil.extractUserId(token)).append("\n");
        info.append("Email: ").append(jwtUtil.extractEmail(token)).append("\n");
        info.append("Role: ").append(jwtUtil.extractRole(token)).append("\n");
        info.append("Student ID: ").append(jwtUtil.getStudentId(token)).append("\n");
        info.append("Instructor ID: ").append(jwtUtil.getInstructorId(token)).append("\n");
        info.append("Is Student: ").append(jwtUtil.isStudent(token)).append("\n");
        info.append("Is Instructor: ").append(jwtUtil.isInstructor(token)).append("\n");
        info.append("Token Valid: ").append(jwtUtil.validateToken(token)).append("\n");
        
        return ResponseEntity.ok(info.toString());
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Exam Service is running!");
    }
    
    // Helper method to extract token from request
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 