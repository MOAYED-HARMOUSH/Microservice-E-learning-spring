package com.example.user_service.services;

import com.example.user_service.dto.*;
import com.example.user_service.entities.*;
import com.example.user_service.exceptions.*;
import com.example.user_service.repositories.*;
import com.example.user_service.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private InstructorRepository instructorRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponseDTO login(LoginDTO loginDTO) {
        logger.info("Processing login for email: {}", loginDTO.getEmail());
        
        User user = userRepository.findByEmail(loginDTO.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            logger.warn("Invalid password for email: {}", loginDTO.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user);
        logger.info("Login successful for user: {} (ID: {})", user.getEmail(), user.getId());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setToken(token);

        return response;
    }

    public User registerUser(UserRegistrationDTO dto) {
        logger.info("Starting user registration for email: {}, role: {}", dto.getEmail(), dto.getRole());
        
        try {
            // Check if email already exists
            if (userRepository.existsByEmail(dto.getEmail())) {
                logger.warn("Registration failed: Email already exists: {}", dto.getEmail());
                throw new RuntimeException("Email already exists");
            }
            
            logger.info("Creating user entity for email: {}", dto.getEmail());
            
            // Create and save user
            User user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setPhone(dto.getPhone());
            user.setRole(dto.getRole());
            
            logger.info("Saving user to database: {}", dto.getEmail());
            user = userRepository.save(user);
            logger.info("User saved successfully with ID: {}", user.getId());

            // Create role-specific entity
            switch (dto.getRole()) {
                case STUDENT:
                    logger.info("Creating student entity for user ID: {}", user.getId());
                    Student student = new Student();
                    student.setStatus(dto.getStatus() != null ? dto.getStatus() : "ACTIVE");
                    student.setUser(user);
                    studentRepository.save(student);
                    logger.info("Student entity created successfully");
                    break;
                    
                case INSTRUCTOR:
                    logger.info("Creating instructor entity for user ID: {}", user.getId());
                    Instructor instructor = new Instructor();
                    instructor.setSpecialization(dto.getSpecialization());
                    instructor.setUser(user);
                    instructorRepository.save(instructor);
                    logger.info("Instructor entity created successfully with specialization: {}", dto.getSpecialization());
                    break;
                    
                case ADMIN:
                    logger.info("Creating admin entity for user ID: {}", user.getId());
                    Admin admin = new Admin();
                    admin.setCountry(dto.getCountry());
                    admin.setUser(user);
                    adminRepository.save(admin);
                    logger.info("Admin entity created successfully");
                    break;
                    
                default:
                    logger.error("Unknown role: {}", dto.getRole());
                    throw new RuntimeException("Unknown role: " + dto.getRole());
            }

            logger.info("User registration completed successfully for email: {}, ID: {}", dto.getEmail(), user.getId());
            return user;
            
        } catch (Exception e) {
            logger.error("Registration failed for email: {}", dto.getEmail(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    // CRUD operations for Instructor (Admin only)
    public List<InstructorDTO> getAllInstructors() {
        logger.info("Fetching all instructors");
        List<Instructor> instructors = instructorRepository.findAll();
        logger.info("Found {} instructors", instructors.size());
        return instructors.stream()
            .map(this::convertToInstructorDTO)
            .collect(Collectors.toList());
    }

    public InstructorDTO getInstructor(Long id) {
        logger.info("Fetching instructor with ID: {}", id);
        Instructor instructor = instructorRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Instructor not found with id: " + id));
        logger.info("Found instructor: {} (ID: {})", instructor.getUser().getName(), id);
        return convertToInstructorDTO(instructor);
    }

    public void deleteInstructor(Long id) {
        logger.info("Deleting instructor with ID: {}", id);
        Instructor instructor = instructorRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Instructor not found with id: " + id));
        instructorRepository.delete(instructor);
        userRepository.delete(instructor.getUser());
        logger.info("Instructor deleted successfully (ID: {})", id);
    }

    public InstructorDTO updateInstructor(Long id, UserRegistrationDTO dto) {
        logger.info("Updating instructor with ID: {}", id);
        Instructor instructor = instructorRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Instructor not found with id: " + id));
        
        User user = instructor.getUser();

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getSpecialization() != null) {
            instructor.setSpecialization(dto.getSpecialization());
        }

        userRepository.save(user);
        instructor = instructorRepository.save(instructor);
        
        logger.info("Instructor updated successfully (ID: {})", id);
        return convertToInstructorDTO(instructor);
    }

    public InstructorDTO createInstructor(UserRegistrationDTO dto) {
        logger.info("Creating instructor with email: {}", dto.getEmail());
        dto.setRole(UserRole.INSTRUCTOR);
        User user = registerUser(dto);
        Instructor instructor = instructorRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("Failed to create instructor"));
        logger.info("Instructor created successfully: {} (ID: {})", user.getName(), user.getId());
        return convertToInstructorDTO(instructor);
    }

    private InstructorDTO convertToInstructorDTO(Instructor instructor) {
        User user = instructor.getUser();
        return new InstructorDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getRole(),
            instructor.getSpecialization()
        );
    }

    // Helper method to get current user from JWT token
    public User getCurrentUser(String token) {
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Helper method to check if user is admin
    public boolean isAdmin(User user) {
        return user.getRole() == UserRole.ADMIN;
    }
} 