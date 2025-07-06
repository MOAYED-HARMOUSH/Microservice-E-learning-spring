package com.microServices.Exam.clients;

import com.microServices.Exam.dto.ExamDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CourseServiceClient {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceClient.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${course.service.url:http://localhost:8082}")
    private String courseServiceUrl;
    
    /**
     * الحصول على امتحان محدد بواسطة ID مع الأسئلة ومعالجة الأخطاء
     */
    public ExamDTO getExamById(Long examId, String jwtToken) {
        try {
            String url = courseServiceUrl + "/api/courses/exams/" + examId + "/with-questions";
            logger.info("Getting exam by ID: {} from URL: {}", examId, url);
            
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
                logger.warn("No exam data received for exam ID: {}", examId);
                return null;
            }
            
            logger.info("Successfully retrieved exam data for exam ID: {}", examId);
            
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
                logger.info("Retrieved {} questions for exam ID: {}", questions.size(), examId);
            } else {
                logger.warn("No questions found for exam ID: {}", examId);
            }
            
            return exam;
        } catch (ResourceAccessException e) {
            // في حالة timeout أو فشل الاتصال
            logger.warn("Service timeout or connection failed for exam ID: {}. Error: {}", examId, e.getMessage());
            ExamDTO errorExam = new ExamDTO();
            errorExam.setId(examId);
            errorExam.setName("Exam Unavailable - Service Error");
            errorExam.setDescription("This exam is temporarily unavailable due to service issues.");
            errorExam.setErrorMessage("Service timeout or connection failed after 5 seconds");
            return errorExam;
        } catch (Exception e) {
            // في حالة أي خطأ آخر
            logger.error("Error getting exam by ID {}: {}", examId, e.getMessage(), e);
            ExamDTO errorExam = new ExamDTO();
            errorExam.setId(examId);
            errorExam.setName("Exam Unavailable - Service Error");
            errorExam.setDescription("This exam is temporarily unavailable due to service issues.");
            errorExam.setErrorMessage("Service error: " + e.getMessage());
            return errorExam;
        }
    }
    
    /**
     * التحقق من وجود امتحان مع معالجة الأخطاء
     */
    public boolean examExists(Long examId, String jwtToken) {
        try {
            logger.info("Checking if exam exists: {}", examId);
            ExamDTO exam = getExamById(examId, jwtToken);
            boolean exists = exam != null && exam.getErrorMessage() == null;
            logger.info("Exam {} exists: {}", examId, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking if exam exists {}: {}", examId, e.getMessage(), e);
            return false;
        }
    }
}