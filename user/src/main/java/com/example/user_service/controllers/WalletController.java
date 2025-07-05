package com.example.user_service.controllers;

import com.example.user_service.dto.WalletDTO;
import com.example.user_service.entities.Student;
import com.example.user_service.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/wallet")
public class WalletController {
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * الحصول على رصيد المحفظة
     */
    @GetMapping("/{userId}")
    public ResponseEntity<WalletDTO> getWalletBalance(@PathVariable Long userId) {
        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        
        WalletDTO wallet = new WalletDTO(userId, student.getWalletBalance());
        return ResponseEntity.ok(wallet);
    }
    
    /**
     * إضافة مال للمحفظة
     */
    @PostMapping("/{userId}/add")
    public ResponseEntity<WalletDTO> addMoneyToWallet(@PathVariable Long userId, @RequestBody Double amount) {

        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (amount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        
        student.setWalletBalance(student.getWalletBalance() + amount);
        student = studentRepository.save(student);
        
        WalletDTO wallet = new WalletDTO(userId, student.getWalletBalance());
        return ResponseEntity.ok(wallet);
    }
    
    /**
     * خصم مال من المحفظة
     */
    @PostMapping("/{userId}/deduct")
    public ResponseEntity<WalletDTO> deductMoneyFromWallet(@PathVariable Long userId, @RequestBody Double amount) {
        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (amount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        
        if (student.getWalletBalance() < amount) {
            return ResponseEntity.badRequest().build();
        }
        
        student.setWalletBalance(student.getWalletBalance() - amount);
        student = studentRepository.save(student);
        
        WalletDTO wallet = new WalletDTO(userId, student.getWalletBalance());
        return ResponseEntity.ok(wallet);
    }
} 