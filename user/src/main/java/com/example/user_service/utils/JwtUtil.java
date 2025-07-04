package com.example.user_service.utils;

import com.example.user_service.entities.User;
import com.example.user_service.entities.Instructor;
import com.example.user_service.entities.Admin;
import com.example.user_service.entities.Student;
import com.example.user_service.repositories.InstructorRepository;
import com.example.user_service.repositories.AdminRepository;
import com.example.user_service.repositories.StudentRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret:thisIsA64CharacterLongSecretKeyForHS256AlgorithmThatIsSecureEnoughForJWT!!}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private long expiration;
    
    @Autowired
    private InstructorRepository instructorRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * توليد token للمستخدم مع جميع المعلومات المطلوبة
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().name());
        
        // إضافة معرفات إضافية حسب نوع المستخدم
        switch (user.getRole()) {
            case INSTRUCTOR:
                List<Instructor> instructors = instructorRepository.findByUser(user);
                if (!instructors.isEmpty()) {
                    // نأخذ أول instructor (الأحدث)
                    claims.put("instructorId", instructors.get(0).getId());
                }
                break;
            case ADMIN:
                List<Admin> admins = adminRepository.findByUser(user);
                if (!admins.isEmpty()) {
                    // نأخذ أول admin (الأحدث)
                    claims.put("adminId", admins.get(0).getId());
                }
                break;
            case STUDENT:
                List<Student> students = studentRepository.findByUser(user);
                if (!students.isEmpty()) {
                    // نأخذ أول student (الأحدث)
                    claims.put("studentId", students.get(0).getId());
                }
                break;
        }
        
        return createToken(claims, user.getEmail());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }
    
    /**
     * استخراج معرف الأستاذ من الـ token
     */
    public Long extractInstructorId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("instructorId", Long.class);
    }
    
    /**
     * استخراج معرف المدير من الـ token
     */
    public Long extractAdminId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("adminId", Long.class);
    }
    
    /**
     * استخراج معرف الطالب من الـ token
     */
    public Long extractStudentId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("studentId", Long.class);
    }
    
    /**
     * التحقق من أن المستخدم أستاذ
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
     * التحقق من أن المستخدم مدير
     */
    public Boolean isAdmin(String token) {
        try {
            String role = extractRole(token);
            return "ADMIN".equals(role);
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
} 