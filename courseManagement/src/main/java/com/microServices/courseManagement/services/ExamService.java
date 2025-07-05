package com.microServices.courseManagement.services;

import com.microServices.courseManagement.dto.ExamDTO;
import com.microServices.courseManagement.dto.ExamWithQuestionsDTO;
import com.microServices.courseManagement.dto.QuestionDTO;
import com.microServices.courseManagement.entities.Exam;
import com.microServices.courseManagement.repositories.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {
    
    @Autowired
    private ExamRepository examRepository;
    
    @Autowired
    private QuestionService questionService;
    
    public List<ExamDTO> getAllExams() {
        return examRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public ExamDTO getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        return convertToDTO(exam);
    }
    
    public List<ExamDTO> getExamsByCourse(Long courseId) {
        return examRepository.findByCourseId(courseId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public ExamDTO createExam(ExamDTO examDTO) {
        Exam exam = convertToEntity(examDTO);
        Exam savedExam = examRepository.save(exam);
        return convertToDTO(savedExam);
    }
    
    public ExamDTO updateExam(Long id, ExamDTO examDTO) {
        Exam existingExam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        
        existingExam.setName(examDTO.getName());
        existingExam.setDescription(examDTO.getDescription());
        existingExam.setDegree(examDTO.getDegree());
        existingExam.setSuccessDegree(examDTO.getSuccessDegree());
        
        Exam updatedExam = examRepository.save(existingExam);
        return convertToDTO(updatedExam);
    }
    
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }
    
    /**
     * الحصول على امتحان مع الأسئلة
     */
    public ExamWithQuestionsDTO getExamWithQuestions(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        
        List<QuestionDTO> questions = questionService.getQuestionsByExam(id);
        
        return new ExamWithQuestionsDTO(
            exam.getId(),
            exam.getName(),
            exam.getDescription(),
            exam.getCourseId(),
            exam.getDegree(),
            exam.getSuccessDegree(),
            questions
        );
    }
    
    private ExamDTO convertToDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        dto.setId(exam.getId());
        dto.setCourseId(exam.getCourseId());
        dto.setName(exam.getName());
        dto.setDescription(exam.getDescription());
        dto.setDegree(exam.getDegree());
        dto.setSuccessDegree(exam.getSuccessDegree());
        return dto;
    }
    
    private Exam convertToEntity(ExamDTO dto) {
        Exam exam = new Exam();
        exam.setId(dto.getId());
        exam.setCourseId(dto.getCourseId());
        exam.setName(dto.getName());
        exam.setDescription(dto.getDescription());
        exam.setDegree(dto.getDegree());
        exam.setSuccessDegree(dto.getSuccessDegree());
        return exam;
    }
} 