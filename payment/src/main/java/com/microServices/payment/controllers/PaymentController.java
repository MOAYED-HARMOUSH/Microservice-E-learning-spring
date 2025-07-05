package com.microServices.payment.controllers;

import com.microServices.payment.dto.CourseDTO;
import com.microServices.payment.dto.EnrollmentDTO;
import com.microServices.payment.services.PaymentService;
import com.microServices.payment.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * الحصول على جميع الكورسات المتاحة (المعتمدة)
     */
    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getAvailableCourses() {
        List<CourseDTO> courses = paymentService.getAvailableCourses();
        return ResponseEntity.ok(courses);
    }
    
    /**
     * الحصول على كورسات الطالب المشترك بها
     */
    @GetMapping("/enrollments")
    public ResponseEntity<List<EnrollmentDTO>> getStudentEnrollments(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isStudent(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Long studentId = jwtUtil.getStudentId(token);
        if (studentId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<EnrollmentDTO> enrollments = paymentService.getStudentEnrollments(studentId);
        return ResponseEntity.ok(enrollments);
    }
    
    /**
     * الاشتراك في كورس معين
     */
    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<EnrollmentDTO> enrollInCourse(@PathVariable Long courseId, HttpServletRequest request) {
        logger.info("Received enrollment request for courseId: {}", courseId);
        
        String token = extractToken(request);
        logger.info("Extracted token: {}", token != null ? "Token exists" : "No token");
        
        if (token == null || !jwtUtil.validateToken(token)) {
            logger.error("Invalid or missing token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isStudent(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Long studentId = jwtUtil.getStudentId(token);
        if (studentId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        try {
            EnrollmentDTO enrollment = paymentService.enrollInCourse(studentId, courseId, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (RuntimeException e) {
            logger.error("Enrollment failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error during enrollment: {}", e.getMessage(), e);
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
        info.append("Is Student: ").append(jwtUtil.isStudent(token)).append("\n");
        info.append("Token Valid: ").append(jwtUtil.validateToken(token)).append("\n");
        
        return ResponseEntity.ok(info.toString());
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Payment Service is running!");
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