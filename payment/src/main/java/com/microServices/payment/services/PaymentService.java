package com.microServices.payment.services;

import com.microServices.payment.clients.CourseServiceClient;
import com.microServices.payment.clients.UserServiceClient;
import com.microServices.payment.dto.CourseDTO;
import com.microServices.payment.dto.EnrollmentDTO;
import com.microServices.payment.dto.WalletDTO;
import com.microServices.payment.entities.Enrollment;
import com.microServices.payment.repositories.EnrollmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private CourseServiceClient courseServiceClient;
    
    @Autowired
    private UserServiceClient userServiceClient;
    
    /**
     * الحصول على جميع الكورسات المتاحة (المعتمدة)
     */
    public List<CourseDTO> getAvailableCourses() {
        logger.info("Fetching available courses");
        List<CourseDTO> courses = courseServiceClient.getAcceptedCourses();
        logger.info("Found {} available courses", courses.size());
        return courses;
    }
    
    /**
     * الحصول على كورسات الطالب المشترك بها
     */
    public List<EnrollmentDTO> getStudentEnrollments(Long studentId) {
        logger.info("Fetching enrollments for student: {}", studentId);
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        
        List<EnrollmentDTO> enrollmentDTOs = enrollments.stream()
            .map(this::convertToEnrollmentDTO)
            .collect(Collectors.toList());
        
        logger.info("Found {} enrollments for student: {}", enrollmentDTOs.size(), studentId);
        return enrollmentDTOs;
    }
    
    /**
     * الاشتراك في كورس معين
     */
    public EnrollmentDTO enrollInCourse(Long studentId, Long courseId, String jwtToken) {
        logger.info("Processing enrollment for student: {} in course: {}", studentId, courseId);
        
        // التحقق من وجود الكورس
        if (!courseServiceClient.courseExists(courseId)) {
            logger.error("Course {} does not exist or is not approved", courseId);
            throw new RuntimeException("Course not found or not approved");
        }
        
        // التحقق من عدم وجود اشتراك سابق
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            logger.error("Student {} is already enrolled in course {}", studentId, courseId);
            throw new RuntimeException("Already enrolled in this course");
        }
        
        // الحصول على تفاصيل الكورس
        CourseDTO course = courseServiceClient.getCourseById(courseId);
        if (course == null) {
            logger.error("Failed to get course details for course: {}", courseId);
            throw new RuntimeException("Failed to get course details");
        }
        
        // التحقق من كفاية الرصيد
        if (!userServiceClient.hasSufficientBalance(studentId, course.getCost(), jwtToken)) {
            logger.error("Insufficient balance for student: {} to enroll in course: {}", studentId, courseId);
            throw new RuntimeException("Insufficient balance");
        }
        
        // خصم المال من المحفظة
        WalletDTO updatedWallet = userServiceClient.deductMoneyFromWallet(studentId, course.getCost(), jwtToken);
        if (updatedWallet == null) {
            logger.error("Failed to deduct money from wallet for student: {}", studentId);
            throw new RuntimeException("Payment failed");
        }
        
        // إنشاء الاشتراك
        Enrollment enrollment = new Enrollment(studentId, courseId, course.getCost());
        enrollment = enrollmentRepository.save(enrollment);
        
        logger.info("Successfully enrolled student: {} in course: {} for amount: {}", 
                   studentId, courseId, course.getCost());
        
        return convertToEnrollmentDTO(enrollment);
    }
    
    /**
     * تحويل Enrollment إلى EnrollmentDTO مع تفاصيل الكورس
     */
    private EnrollmentDTO convertToEnrollmentDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO(
            enrollment.getId(),
            enrollment.getStudentId(),
            enrollment.getCourseId(),
            enrollment.getIsCompleted(),
            enrollment.getEnrollmentDate(),
            enrollment.getAmountPaid(),
            enrollment.getPaymentStatus()
        );
        
        // إضافة تفاصيل الكورس
        try {
            CourseDTO course = courseServiceClient.getCourseById(enrollment.getCourseId());
            if (course != null) {
                dto.setCourseName(course.getName());
                dto.setCourseDescription(course.getDescription());
                dto.setCourseCost(course.getCost());
            }
        } catch (Exception e) {
            logger.warn("Failed to get course details for course: {}", enrollment.getCourseId());
        }
        
        return dto;
    }
} 