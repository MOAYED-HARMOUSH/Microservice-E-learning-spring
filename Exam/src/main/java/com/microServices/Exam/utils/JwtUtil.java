package com.microServices.Exam.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    // ⚠️ مهم جداً: يجب أن يكون نفس SECRET_KEY المستخدم في User Service
    @Value("${jwt.secret:thisIsA64CharacterLongSecretKeyForHS256AlgorithmThatIsSecureEnoughForJWT!!}")
    private String SECRET_KEY;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    
    /**
     * استخراج البريد الإلكتروني من الـ token
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * استخراج تاريخ انتهاء صلاحية الـ token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * استخراج الدور (Role) من الـ token
     */
    public String extractRole(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }
    
    /**
     * استخراج معرف المستخدم من الـ token
     */
    public Long extractUserId(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }
    
    /**
     * استخراج معرف الطالب من الـ token
     */
    public Long extractStudentId(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("studentId", Long.class);
    }
    
    /**
     * استخراج معرف المدرس من الـ token
     */
    public Long extractInstructorId(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("instructorId", Long.class);
    }
    
    /**
     * استخراج أي claim من الـ token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * استخراج جميع الـ claims من الـ token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * التحقق من انتهاء صلاحية الـ token
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * التحقق من صحة الـ token
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * التحقق من أن المستخدم طالب
     */
    public Boolean isStudent(String token) {
        try {
            String role = extractRole(token);
            return "STUDENT".equals(role);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * التحقق من أن المستخدم مدرس
     */
    public Boolean isInstructor(String token) {
        try {
            String role = extractRole(token);
            return "INSTRUCTOR".equals(role);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * الحصول على معرف الطالب من الـ token
     */
    public Long getStudentId(String token) {
        try {
            if (isStudent(token)) {
                return extractStudentId(token);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * الحصول على معرف المدرس من الـ token
     */
    public Long getInstructorId(String token) {
        try {
            if (isInstructor(token)) {
                return extractInstructorId(token);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
} 