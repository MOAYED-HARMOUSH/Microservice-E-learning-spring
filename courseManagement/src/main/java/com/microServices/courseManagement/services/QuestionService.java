package com.microServices.courseManagement.services;

import com.microServices.courseManagement.dto.QuestionDTO;
import com.microServices.courseManagement.entities.Question;
import com.microServices.courseManagement.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public QuestionDTO getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return convertToDTO(question);
    }
    
    public List<QuestionDTO> getQuestionsByExam(Long examId) {
        return questionRepository.findByExamId(examId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Question question = convertToEntity(questionDTO);
        Question savedQuestion = questionRepository.save(question);
        return convertToDTO(savedQuestion);
    }
    
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        
        existingQuestion.setQuestion(questionDTO.getQuestion());
        existingQuestion.setAnswer1(questionDTO.getAnswer1());
        existingQuestion.setAnswer2(questionDTO.getAnswer2());
        existingQuestion.setAnswer3(questionDTO.getAnswer3());
        existingQuestion.setAnswer4(questionDTO.getAnswer4());
        existingQuestion.setCorrectAnswer(questionDTO.getCorrectAnswer());
        existingQuestion.setDegree(questionDTO.getDegree());
        
        Question updatedQuestion = questionRepository.save(existingQuestion);
        return convertToDTO(updatedQuestion);
    }
    
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
    
    private QuestionDTO convertToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setExamId(question.getExamId());
        dto.setQuestion(question.getQuestion());
        dto.setAnswer1(question.getAnswer1());
        dto.setAnswer2(question.getAnswer2());
        dto.setAnswer3(question.getAnswer3());
        dto.setAnswer4(question.getAnswer4());
        dto.setCorrectAnswer(question.getCorrectAnswer());
        dto.setDegree(question.getDegree());
        return dto;
    }
    
    private Question convertToEntity(QuestionDTO dto) {
        Question question = new Question();
        question.setId(dto.getId());
        question.setExamId(dto.getExamId());
        question.setQuestion(dto.getQuestion());
        question.setAnswer1(dto.getAnswer1());
        question.setAnswer2(dto.getAnswer2());
        question.setAnswer3(dto.getAnswer3());
        question.setAnswer4(dto.getAnswer4());
        question.setCorrectAnswer(dto.getCorrectAnswer());
        question.setDegree(dto.getDegree());
        return question;
    }
} 