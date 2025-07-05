package com.microServices.Exam.services;

import com.microServices.Exam.clients.CourseServiceClient;
import com.microServices.Exam.clients.PaymentServiceClient;
import com.microServices.Exam.dto.ExamDTO;
import com.microServices.Exam.dto.ExamResultDTO;
import com.microServices.Exam.dto.ExamSubmissionDTO;
import com.microServices.Exam.dto.QuestionDTO;
import com.microServices.Exam.entities.QuestionToStudentToExam;
import com.microServices.Exam.entities.StudentToExam;
import com.microServices.Exam.repositories.QuestionToStudentToExamRepository;
import com.microServices.Exam.repositories.StudentToExamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExamService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExamService.class);
    
    @Autowired
    private StudentToExamRepository studentToExamRepository;
    
    @Autowired
    private QuestionToStudentToExamRepository questionToStudentToExamRepository;
    
    @Autowired
    private CourseServiceClient courseServiceClient;
    
    @Autowired
    private PaymentServiceClient paymentServiceClient;
    
    /**
     * تقديم امتحان من قبل الطالب
     */
    public ExamResultDTO submitExam(ExamSubmissionDTO submissionDTO, String jwtToken) {
        logger.info("Processing exam submission for student: {} in exam: {}", 
                   submissionDTO.getStudentId(), submissionDTO.getExamId());
        
        // التحقق من وجود الامتحان
        if (!courseServiceClient.examExists(submissionDTO.getExamId(), jwtToken)) {
            logger.error("Exam {} does not exist", submissionDTO.getExamId());
            throw new RuntimeException("Exam not found");
        }
        
        // الحصول على تفاصيل الامتحان
        ExamDTO exam = courseServiceClient.getExamById(submissionDTO.getExamId(), jwtToken);
        if (exam == null) {
            logger.error("Failed to get exam details for exam: {}", submissionDTO.getExamId());
            throw new RuntimeException("Failed to get exam details");
        }
        
        // التحقق من اشتراك الطالب في الكورس
        if (!paymentServiceClient.isStudentEnrolledInCourse(
                submissionDTO.getStudentId(), exam.getCourseId(), jwtToken)) {
            logger.error("Student {} is not enrolled in course {}", 
                        submissionDTO.getStudentId(), exam.getCourseId());
            throw new RuntimeException("Student not enrolled in course");
        }
        
        // التحقق من عدم وجود امتحان سابق
        if (studentToExamRepository.existsByStudentIdAndExamId(
                submissionDTO.getStudentId(), submissionDTO.getExamId())) {
            logger.error("Student {} already submitted exam {}", 
                        submissionDTO.getStudentId(), submissionDTO.getExamId());
            throw new RuntimeException("Exam already submitted");
        }
        
        // إنشاء سجل الامتحان
        StudentToExam studentToExam = new StudentToExam(
            submissionDTO.getStudentId(), 
            submissionDTO.getExamId(), 
            exam.getCourseId()
        );
        studentToExam = studentToExamRepository.save(studentToExam);
        
        // حفظ إجابات الطالب
        for (Map.Entry<Long, QuestionToStudentToExam.StudentAnswer> entry : 
             submissionDTO.getAnswers().entrySet()) {
            QuestionToStudentToExam answer = new QuestionToStudentToExam(
                entry.getKey(), 
                studentToExam.getId(), 
                entry.getValue()
            );
            questionToStudentToExamRepository.save(answer);
        }
        
        // حساب النتيجة
        double totalScore = calculateScore(submissionDTO.getAnswers(), exam);
        studentToExam.setStudentDegree(totalScore);
        
        // تحديد النجاح أو الرسوب
        if (totalScore >= exam.getPassingDegree()) {
            studentToExam.setStatus("PASSED");
        } else {
            studentToExam.setStatus("FAILED");
        }
        
        studentToExam = studentToExamRepository.save(studentToExam);
        
        logger.info("Exam submitted successfully. Student: {}, Exam: {}, Score: {}, Status: {}", 
                   submissionDTO.getStudentId(), submissionDTO.getExamId(), 
                   totalScore, studentToExam.getStatus());
        
        return convertToExamResultDTO(studentToExam, exam);
    }
    
    /**
     * الحصول على نتيجة امتحان طالب محدد
     */
    public ExamResultDTO getStudentExamResult(Long studentId, Long examId, String jwtToken) {
        logger.info("Fetching exam result for student: {} in exam: {}", studentId, examId);
        
        StudentToExam studentToExam = studentToExamRepository
            .findByStudentIdAndExamId(studentId, examId)
            .orElse(null);
        
        if (studentToExam == null) {
            logger.error("No exam result found for student: {} in exam: {}", studentId, examId);
            throw new RuntimeException("Exam result not found");
        }
        
        ExamDTO exam = courseServiceClient.getExamById(examId, jwtToken);
        return convertToExamResultDTO(studentToExam, exam);
    }
    
    /**
     * الحصول على نتائج جميع الطلاب في امتحان محدد (للمدرس)
     */
    public List<ExamResultDTO> getExamResultsForInstructor(Long examId, String jwtToken) {
        logger.info("Fetching all results for exam: {}", examId);
        
        List<StudentToExam> studentToExams = studentToExamRepository.findByExamId(examId);
        
        ExamDTO exam = courseServiceClient.getExamById(examId, jwtToken);
        
        return studentToExams.stream()
            .map(studentToExam -> convertToExamResultDTO(studentToExam, exam))
            .collect(Collectors.toList());
    }
    
    /**
     * حساب درجة الطالب
     */
    private double calculateScore(Map<Long, QuestionToStudentToExam.StudentAnswer> studentAnswers, ExamDTO exam) {
        double totalScore = 0.0;
        
        if (exam.getQuestions() == null) {
            logger.warn("No questions found for exam: {}", exam.getId());
            return 0.0;
        }
        
        for (QuestionDTO question : exam.getQuestions()) {
            QuestionToStudentToExam.StudentAnswer studentAnswer = studentAnswers.get(question.getId());
            
            if (studentAnswer != null && studentAnswer.name().equals(question.getCorrectAnswer())) {
                Double questionDegree = question.getDegree();
                if (questionDegree != null) {
                    totalScore += questionDegree;
                } else {
                    logger.warn("Question {} has no degree, using default 10.0", question.getId());
                    totalScore += 10.0;
                }
            }
        }
        
        logger.info("Calculated total score: {} for exam: {}", totalScore, exam.getId());
        return totalScore;
    }
    
    /**
     * تحويل StudentToExam إلى ExamResultDTO
     */
    private ExamResultDTO convertToExamResultDTO(StudentToExam studentToExam, ExamDTO exam) {
        ExamResultDTO dto = new ExamResultDTO(
            studentToExam.getId(),
            studentToExam.getStudentId(),
            studentToExam.getExamId(),
            studentToExam.getCourseId(),
            studentToExam.getStudentDegree(),
            studentToExam.getStatus(),
            studentToExam.getSubmissionDate()
        );
        
        if (exam != null) {
            dto.setExamName(exam.getName());
            dto.setExamTotalDegree(exam.getTotalDegree());
            dto.setPassingDegree(exam.getPassingDegree());
        }
        
        return dto;
    }
} 