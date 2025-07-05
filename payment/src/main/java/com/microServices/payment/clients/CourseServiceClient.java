package com.microServices.payment.clients;

import com.microServices.payment.dto.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class CourseServiceClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${course.service.url:http://localhost:8082}")
    private String courseServiceUrl;
    
    /**
     * الحصول على جميع الكورسات المعتمدة
     */
    public List<CourseDTO> getAcceptedCourses() {
        String url = courseServiceUrl + "/api/courses";
        CourseDTO[] courses = restTemplate.getForObject(url, CourseDTO[].class);
        return courses != null ? Arrays.asList(courses) : List.of();
    }
    
    /**
     * الحصول على كورس محدد بواسطة ID
     */
    public CourseDTO getCourseById(Long courseId) {
        String url = courseServiceUrl + "/api/courses/" + courseId;
        return restTemplate.getForObject(url, CourseDTO.class);
    }
    
    /**
     * التحقق من وجود كورس
     */
    public boolean courseExists(Long courseId) {
        try {
            CourseDTO course = getCourseById(courseId);
            return course != null && "ACCEPTED".equals(course.getStatus());
        } catch (Exception e) {
            return false;
        }
    }
} 