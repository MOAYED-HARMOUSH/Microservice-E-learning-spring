package com.microServices.Exam.controllers;

import com.microServices.Exam.dto.ExamDTO;
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
    
    @Autowired
    private com.microServices.Exam.clients.CourseServiceClient courseServiceClient;
    
    @Autowired
    private com.microServices.Exam.clients.PaymentServiceClient paymentServiceClient;
    
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
        return ResponseEntity.ok("Exam Service is running on port: " + 
                                System.getProperty("server.port", "8040"));
    }
    
    /**
     * Load Balancer Health Check
     */
    @GetMapping("/actuator/health")
    public ResponseEntity<String> actuatorHealth() {
        return ResponseEntity.ok("{\"status\":\"UP\",\"port\":\"" + 
                                System.getProperty("server.port", "8040") + "\"}");
    }
    
    /**
     * API تجريبي لاختبار الاتصال مع خدمة إدارة الكورسات
     */
    @GetMapping("/test/course-service-connection")
    public ResponseEntity<String> testCourseServiceConnection(HttpServletRequest request) {
        logger.info("Testing connection with Course Management Service");
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        StringBuilder result = new StringBuilder();
        result.append("=== اختبار الاتصال مع خدمة إدارة الكورسات ===\n");
        
        try {
            // اختبار الحصول على امتحان
            result.append("1. اختبار الحصول على امتحان ID = 1:\n");
            ExamDTO exam = courseServiceClient.getExamById(1L, token);
            if (exam != null) {
                result.append("   ✓ تم الحصول على الامتحان بنجاح\n");
                result.append("   - اسم الامتحان: ").append(exam.getName()).append("\n");
                result.append("   - عدد الأسئلة: ").append(exam.getQuestions() != null ? exam.getQuestions().size() : 0).append("\n");
                result.append("   - الدرجة الكلية: ").append(exam.getTotalDegree()).append("\n");
                result.append("   - درجة النجاح: ").append(exam.getPassingDegree()).append("\n");
            } else {
                result.append("   ✗ فشل في الحصول على الامتحان\n");
            }
            
            // اختبار التحقق من وجود امتحان
            result.append("\n2. اختبار التحقق من وجود امتحان ID = 1:\n");
            boolean examExists = courseServiceClient.examExists(1L, token);
            result.append("   امتحان ID = 1 موجود: ").append(examExists ? "نعم" : "لا").append("\n");
            
            // اختبار التحقق من وجود امتحان غير موجود
            result.append("\n3. اختبار التحقق من وجود امتحان ID = 999:\n");
            boolean nonExistentExam = courseServiceClient.examExists(999L, token);
            result.append("   امتحان ID = 999 موجود: ").append(nonExistentExam ? "نعم" : "لا").append("\n");
            
        } catch (Exception e) {
            result.append("   ✗ خطأ في الاتصال: ").append(e.getMessage()).append("\n");
            logger.error("Error testing course service connection: {}", e.getMessage(), e);
        }
        
        return ResponseEntity.ok(result.toString());
    }
    
    /**
     * API تجريبي لاختبار الاتصال مع خدمة الدفع
     */
    @GetMapping("/test/payment-service-connection")
    public ResponseEntity<String> testPaymentServiceConnection(HttpServletRequest request) {
        logger.info("Testing connection with Payment Service");
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        
        StringBuilder result = new StringBuilder();
        result.append("=== اختبار الاتصال مع خدمة الدفع ===\n");
        
        try {
            Long studentId = jwtUtil.getStudentId(token);
            result.append("Student ID from token: ").append(studentId).append("\n");
            
            // اختبار التحقق من اشتراك الطالب في كورس
            result.append("\n1. اختبار التحقق من اشتراك الطالب في كورس ID = 1:\n");
            boolean isEnrolled = paymentServiceClient.isStudentEnrolledInCourse(studentId, 1L, token);
            result.append("   الطالب مشترك في الكورس ID = 1: ").append(isEnrolled ? "نعم" : "لا").append("\n");
            
        } catch (Exception e) {
            result.append("   ✗ خطأ في الاتصال: ").append(e.getMessage()).append("\n");
            logger.error("Error testing payment service connection: {}", e.getMessage(), e);
        }
        
        return ResponseEntity.ok(result.toString());
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