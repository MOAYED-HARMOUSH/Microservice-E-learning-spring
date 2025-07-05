package com.example.user_service.repositories;

import com.example.user_service.entities.Student;
import com.example.user_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByUser(User user);
    Student findByUserId(Long userId);
}