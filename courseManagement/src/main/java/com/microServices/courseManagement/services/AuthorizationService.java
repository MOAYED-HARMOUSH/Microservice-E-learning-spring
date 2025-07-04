package com.microServices.courseManagement.services;

import com.microServices.courseManagement.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public Boolean isInstructor(String token) {
        return jwtUtil.isInstructor(token);
    }
    
    public Boolean isAdmin(String token) {
        return jwtUtil.isAdmin(token);
    }
    
    public Boolean isStudent(String token) {
        return jwtUtil.isStudent(token);
    }
    
    public Boolean hasRole(String token, String role) {
        return jwtUtil.hasRole(token, role);
    }
    
    public Long getUserId(String token) {
        return jwtUtil.extractUserId(token);
    }
    
    public String getUserEmail(String token) {
        return jwtUtil.extractEmail(token);
    }
    
    public Boolean canAccessCourse(Long courseId, String token) {
        // يمكن إضافة منطق إضافي هنا
        // مثلاً: التحقق من أن المستخدم مسجل في الكورس
        return true;
    }
    
    public Boolean canModifyCourse(Long courseId, String token) {
        if (isAdmin(token)) {
            return true;
        }
        
        if (isInstructor(token)) {
            // التحقق من أن الأستاذ هو صاحب الكورس
            // يمكن إضافة منطق إضافي هنا
            return true;
        }
        
        return false;
    }
} 