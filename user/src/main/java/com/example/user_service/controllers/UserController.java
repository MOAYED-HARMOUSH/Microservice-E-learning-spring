package com.example.user_service.controllers;

import com.example.user_service.dto.*;
import com.example.user_service.entities.*;
import com.example.user_service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        logger.info("Login attempt for email: {}", loginDTO.getEmail());
        try {
            LoginResponseDTO response = userService.login(loginDTO);
            logger.info("Login successful for email: {}", loginDTO.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed for email: {}", loginDTO.getEmail(), e);
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO dto) {
        logger.info("Registration attempt for email: {}, role: {}", dto.getEmail(), dto.getRole());
        try {
            User user = userService.registerUser(dto);
            logger.info("Registration successful for email: {}, userId: {}", dto.getEmail(), user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            logger.error("Registration failed for email: {}", dto.getEmail(), e);
            throw e;
        }
    }

    // Admin endpoints for managing instructors
    @GetMapping("/instructors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        logger.info("Getting all instructors");
        try {
            List<InstructorDTO> instructors = userService.getAllInstructors();
            logger.info("Found {} instructors", instructors.size());
            return ResponseEntity.ok(instructors);
        } catch (Exception e) {
            logger.error("Failed to get instructors", e);
            throw e;
        }
    }

    @GetMapping("/instructors/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstructorDTO> getInstructor(@PathVariable Long id) {
        logger.info("Getting instructor with id: {}", id);
        try {
            InstructorDTO instructor = userService.getInstructor(id);
            logger.info("Found instructor: {}", instructor.getName());
            return ResponseEntity.ok(instructor);
        } catch (Exception e) {
            logger.error("Failed to get instructor with id: {}", id, e);
            throw e;
        }
    }

    @PostMapping("/instructors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstructorDTO> createInstructor(@Valid @RequestBody UserRegistrationDTO dto) {
        logger.info("Creating instructor with email: {}", dto.getEmail());
        try {
            InstructorDTO instructor = userService.createInstructor(dto);
            logger.info("Instructor created successfully: {}", instructor.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(instructor);
        } catch (Exception e) {
            logger.error("Failed to create instructor with email: {}", dto.getEmail(), e);
            throw e;
        }
    }

    @PutMapping("/instructors/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstructorDTO> updateInstructor(
            @PathVariable Long id,
            @Valid @RequestBody UserRegistrationDTO dto) {
        logger.info("Updating instructor with id: {}", id);
        try {
            InstructorDTO instructor = userService.updateInstructor(id, dto);
            logger.info("Instructor updated successfully: {}", instructor.getName());
            return ResponseEntity.ok(instructor);
        } catch (Exception e) {
            logger.error("Failed to update instructor with id: {}", id, e);
            throw e;
        }
    }

    @DeleteMapping("/instructors/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        logger.info("Deleting instructor with id: {}", id);
        try {
            userService.deleteInstructor(id);
            logger.info("Instructor deleted successfully with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete instructor with id: {}", id, e);
            throw e;
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        logger.info("Health check requested");
        return ResponseEntity.ok("User Service is running");
    }
}