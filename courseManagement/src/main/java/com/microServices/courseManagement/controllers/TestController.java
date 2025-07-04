package com.microServices.courseManagement.controllers;

import com.microServices.courseManagement.services.AuthorizationService;
import com.microServices.courseManagement.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private AuthorizationService authorizationService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/health")
    public String health() {
        return "Course Management Service is running!";
    }
    
    @GetMapping("/token-info")
    public Map<String, Object> getTokenInfo(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            response.put("error", "No token provided");
            return response;
        }
        
        String token = bearerToken.substring(7);
        
        try {
            // التحقق من صحة الـ token
            if (!jwtUtil.validateToken(token)) {
                response.put("error", "Invalid token");
                return response;
            }
            
            // استخراج جميع المعلومات من الـ token
            response.put("userId", jwtUtil.extractUserId(token));
            response.put("email", jwtUtil.extractEmail(token));
            response.put("role", jwtUtil.extractRole(token));
            response.put("instructorId", jwtUtil.getInstructorId(token));
            response.put("adminId", jwtUtil.getAdminId(token));
            response.put("studentId", jwtUtil.getStudentId(token));
            response.put("isInstructor", jwtUtil.isInstructor(token));
            response.put("isAdmin", jwtUtil.isAdmin(token));
            response.put("isStudent", jwtUtil.isStudent(token));
            response.put("tokenValid", true);
            
        } catch (Exception e) {
            response.put("error", "Token parsing failed: " + e.getMessage());
            response.put("tokenValid", false);
        }
        
        return response;
    }
    
    @GetMapping("/token-details")
    public String getTokenDetails(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return "No token provided";
        }
        
        String token = bearerToken.substring(7);
        
        try {
            if (!jwtUtil.validateToken(token)) {
                return "Invalid token";
            }
            
            StringBuilder details = new StringBuilder();
            details.append("=== JWT Token Details ===\n");
            details.append("User ID: ").append(jwtUtil.extractUserId(token)).append("\n");
            details.append("Email: ").append(jwtUtil.extractEmail(token)).append("\n");
            details.append("Role: ").append(jwtUtil.extractRole(token)).append("\n");
            details.append("Instructor ID: ").append(jwtUtil.getInstructorId(token)).append("\n");
            details.append("Admin ID: ").append(jwtUtil.getAdminId(token)).append("\n");
            details.append("Student ID: ").append(jwtUtil.getStudentId(token)).append("\n");
            details.append("Is Instructor: ").append(jwtUtil.isInstructor(token)).append("\n");
            details.append("Is Admin: ").append(jwtUtil.isAdmin(token)).append("\n");
            details.append("Is Student: ").append(jwtUtil.isStudent(token)).append("\n");
            details.append("Token Valid: ").append(jwtUtil.validateToken(token)).append("\n");
            
            return details.toString();
            
        } catch (Exception e) {
            return "Token parsing failed: " + e.getMessage();
        }
    }
} 