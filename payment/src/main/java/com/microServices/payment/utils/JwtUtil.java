package com.microServices.payment.utils;

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
    // هذا هو المفتاح السري الذي يوقع عليه الـ token
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
     * هذا يتم من خلال الـ claims المخزنة في الـ token نفسه
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
     * استخراج أي claim من الـ token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * استخراج جميع الـ claims من الـ token
     * هذه العملية تتحقق من صحة التوقيع تلقائياً
     */
    private Claims extractAllClaims(String token) {
        // هنا يتم التحقق من صحة التوقيع
        // إذا كان التوقيع خاطئ، سيتم رمي exception
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
     * هذا يتحقق من:
     * 1. صحة التوقيع (أن الـ token لم يتم تزيفه)
     * 2. عدم انتهاء الصلاحية
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            // إذا كان هناك أي خطأ في parsing الـ token
            // فهذا يعني أن الـ token غير صالح
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
     * الحصول على معرف الطالب من الـ token
     * يعيد null إذا لم يكن المستخدم طالب
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
} 