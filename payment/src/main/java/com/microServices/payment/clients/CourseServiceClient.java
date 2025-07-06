package com.microServices.payment.clients;

import com.microServices.payment.dto.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class CourseServiceClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${course.service.url:http://localhost:8082}")
    private String courseServiceUrl;

    public List<CourseDTO> getAcceptedCourses() {
        try {
            String url = courseServiceUrl + "/api/courses";
            CourseDTO[] courses = restTemplate.getForObject(url, CourseDTO[].class);
            return courses != null ? Arrays.asList(courses) : List.of();
        } catch (ResourceAccessException e) {
             return List.of();
        } catch (Exception e) {
             return List.of();
        }
    }
    
    /**
     * الحصول على كورس محدد بواسطة ID مع معالجة الأخطاء
     */
    public CourseDTO getCourseById(Long courseId) {
        try {
            String url = courseServiceUrl + "/api/courses/" + courseId;
            return restTemplate.getForObject(url, CourseDTO.class);
        } catch (ResourceAccessException e) {
            // في حالة timeout أو فشل الاتصال
            CourseDTO errorCourse = new CourseDTO();
            errorCourse.setId(courseId);
            errorCourse.setTitle("Course Unavailable - Service Error");
            errorCourse.setErrorMessage("Service timeout or connection failed after 5 seconds");
            return errorCourse;
        } catch (Exception e) {
            // في حالة أي خطأ آخر
            CourseDTO errorCourse = new CourseDTO();
            errorCourse.setId(courseId);
            errorCourse.setTitle("Course Unavailable - Service Error");
            errorCourse.setErrorMessage("Service error: " + e.getMessage());
            return errorCourse;
        }
    }
    
    /**
     * التحقق من وجود كورس مع معالجة الأخطاء
     */
    public boolean courseExists(Long courseId) {
        try {
            CourseDTO course = getCourseById(courseId);
            return course != null && "ACCEPTED".equals(course.getStatus()) && course.getErrorMessage() == null;
        } catch (Exception e) {
            return false;
        }
    }
} 