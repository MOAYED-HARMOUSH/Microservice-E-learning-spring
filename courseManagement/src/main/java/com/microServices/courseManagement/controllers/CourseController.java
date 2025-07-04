package com.microServices.courseManagement.controllers;

import com.microServices.courseManagement.dto.CourseDTO;
import com.microServices.courseManagement.dto.CourseApprovalDTO;
import com.microServices.courseManagement.services.CourseService;
import com.microServices.courseManagement.services.AuthorizationService;
import com.microServices.courseManagement.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private AuthorizationService authorizationService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // Public endpoints - accessible by everyone
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllAcceptedCourses() {
        return ResponseEntity.ok(courseService.getAcceptedCourses());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }
    
    // Instructor endpoints - only instructors can access
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseDTO>> getCoursesByInstructor(
            @PathVariable Long instructorId, 
            HttpServletRequest request) {
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isInstructor(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // التحقق من أن الأستاذ يطلب كورساته فقط
        Long tokenInstructorId = jwtUtil.getInstructorId(token);
        if (tokenInstructorId == null || !tokenInstructorId.equals(instructorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }
    
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(
            @Valid @RequestBody CourseDTO courseDTO, 
            HttpServletRequest request) {
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isInstructor(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // تعيين instructorId من الـ token
        Long instructorId = jwtUtil.getInstructorId(token);
        if (instructorId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        courseDTO.setInstructorId(instructorId);
        
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        
        // إضافة معلومات المستخدم للـ response
        Map<String, Object> response = new HashMap<>();
        response.put("course", createdCourse);
        response.put("userInfo", extractUserInfo(token));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable Long id, 
            @Valid @RequestBody CourseDTO courseDTO, 
            HttpServletRequest request) {
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isInstructor(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // التحقق من أن الأستاذ يعدل كورسه فقط
        Long instructorId = jwtUtil.getInstructorId(token);
        if (instructorId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // يمكن إضافة منطق إضافي للتحقق من ملكية الكورس هنا
        
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long id, 
            HttpServletRequest request) {
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isInstructor(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // التحقق من أن الأستاذ يحذف كورسه فقط
        Long instructorId = jwtUtil.getInstructorId(token);
        if (instructorId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // يمكن إضافة منطق إضافي للتحقق من ملكية الكورس هنا
        
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
    
    // Admin endpoints - only admins can access
    @GetMapping("/admin/pending")
    public ResponseEntity<List<CourseDTO>> getPendingCourses(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(courseService.getPendingCourses());
    }
    
    @GetMapping("/admin/all")
    public ResponseEntity<List<CourseDTO>> getAllCourses(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    
    @PostMapping("/admin/approve")
    public ResponseEntity<CourseDTO> approveCourse(
            @Valid @RequestBody CourseApprovalDTO approvalDTO, 
            HttpServletRequest request) {
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        if (approvalDTO.getApproved()) {
            return ResponseEntity.ok(courseService.approveCourse(approvalDTO.getCourseId()));
        } else {
            courseService.rejectCourse(approvalDTO.getCourseId());
            return ResponseEntity.ok().build();
        }
    }
    
    @PostMapping("/admin/{courseId}/approve")
    public ResponseEntity<CourseDTO> approveCourseById(
            @PathVariable Long courseId, 
            HttpServletRequest request) {
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(courseService.approveCourse(courseId));
    }
    
    @DeleteMapping("/admin/{courseId}/reject")
    public ResponseEntity<Void> rejectCourseById(
            @PathVariable Long courseId, 
            HttpServletRequest request) {
        
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!jwtUtil.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        courseService.rejectCourse(courseId);
        return ResponseEntity.ok().build();
    }
    
    // Test endpoint to show extracted token info
    @GetMapping("/test/token-info")
    public ResponseEntity<Map<String, Object>> getTokenInfo(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Map<String, Object> tokenInfo = extractUserInfo(token);
        return ResponseEntity.ok(tokenInfo);
    }
    
    // Helper method to extract token from request
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    // Helper method to extract user info from token
    private Map<String, Object> extractUserInfo(String token) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", jwtUtil.extractUserId(token));
        userInfo.put("email", jwtUtil.extractEmail(token));
        userInfo.put("role", jwtUtil.extractRole(token));
        userInfo.put("instructorId", jwtUtil.getInstructorId(token));
        userInfo.put("adminId", jwtUtil.getAdminId(token));
        userInfo.put("studentId", jwtUtil.getStudentId(token));
        return userInfo;
    }
} 