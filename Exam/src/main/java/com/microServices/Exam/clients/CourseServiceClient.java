package com.microServices.Exam.clients;

import com.microServices.Exam.dto.ExamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CourseServiceClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${course.service.url:http://localhost:8082}")
    private String courseServiceUrl;
    
    /**
     * الحصول على امتحان محدد بواسطة ID مع الأسئلة
     */
    public ExamDTO getExamById(Long examId, String jwtToken) {
        String url = courseServiceUrl + "/api/courses/exams/" + examId + "/with-questions";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
            url, 
            HttpMethod.GET, 
            request, 
            Map.class
        );
        
        Map<String, Object> examData = response.getBody();
        if (examData == null) {
            return null;
        }
        
        // تحويل البيانات إلى ExamDTO
        ExamDTO exam = new ExamDTO();
        exam.setId(Long.valueOf(examData.get("id").toString()));
        exam.setName((String) examData.get("name"));
        exam.setDescription((String) examData.get("description"));
        exam.setCourseId(Long.valueOf(examData.get("courseId").toString()));
        exam.setTotalDegree(Double.valueOf(examData.get("totalDegree").toString()));
        exam.setPassingDegree(Double.valueOf(examData.get("passingDegree").toString()));
        
        // تحويل الأسئلة
        if (examData.containsKey("questions")) {
            @SuppressWarnings("unchecked")
            java.util.List<Map<String, Object>> questionsData = (java.util.List<Map<String, Object>>) examData.get("questions");
            java.util.List<com.microServices.Exam.dto.QuestionDTO> questions = questionsData.stream()
                .map(q -> {
                    com.microServices.Exam.dto.QuestionDTO question = new com.microServices.Exam.dto.QuestionDTO();
                    question.setId(Long.valueOf(q.get("id").toString()));
                    question.setQuestionText((String) q.get("question"));
                    question.setAnswer1((String) q.get("answer1"));
                    question.setAnswer2((String) q.get("answer2"));
                    question.setAnswer3((String) q.get("answer3"));
                    question.setAnswer4((String) q.get("answer4"));
                    question.setCorrectAnswer((String) q.get("correctAnswer"));
                    question.setDegree(Double.valueOf(q.get("degree").toString()));
                    question.setExamId(Long.valueOf(q.get("examId").toString()));
                    return question;
                })
                .collect(java.util.stream.Collectors.toList());
            exam.setQuestions(questions);
        }
        
        return exam;
    }
    
    /**
     * التحقق من وجود امتحان
     */
    public boolean examExists(Long examId, String jwtToken) {
        try {
            ExamDTO exam = getExamById(examId, jwtToken);
            return exam != null;
        } catch (Exception e) {
            return false;
        }
    }
} 