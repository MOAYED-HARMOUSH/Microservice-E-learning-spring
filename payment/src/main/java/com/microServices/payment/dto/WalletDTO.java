package com.microServices.payment.dto;

public class WalletDTO {
    
    private Long userId;
    private Double balance;
    private String currency = "USD";
    private String errorMessage;
    
    // Constructors
    public WalletDTO() {}
    
    public WalletDTO(Long userId, Double balance) {
        this.userId = userId;
        this.balance = balance;
    }
    
    public WalletDTO(Long userId, Double balance, String currency) {
        this.userId = userId;
        this.balance = balance;
        this.currency = currency;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Double getBalance() {
        return balance;
    }
    
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
} 