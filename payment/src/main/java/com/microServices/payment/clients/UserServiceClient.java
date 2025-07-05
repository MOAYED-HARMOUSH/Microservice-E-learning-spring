package com.microServices.payment.clients;

import com.microServices.payment.dto.WalletDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${user.service.url:http://localhost:8090}")
    private String userServiceUrl;
    
    /**
     * الحصول على رصيد المحفظة
     */
    public WalletDTO getWalletBalance(Long userId, String jwtToken) {
        String url = userServiceUrl + "/api/users/wallet/" + userId;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<WalletDTO> response = restTemplate.exchange(
            url, 
            HttpMethod.GET, 
            request, 
            WalletDTO.class
        );
        
        return response.getBody();
    }
    
    /**
     * إضافة مال للمحفظة
     */
    public WalletDTO addMoneyToWallet(Long userId, Double amount, String jwtToken) {
        String url = userServiceUrl + "/api/users/wallet/" + userId + "/add";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + jwtToken);
        
        HttpEntity<Double> request = new HttpEntity<>(amount, headers);
        ResponseEntity<WalletDTO> response = restTemplate.exchange(
            url, 
            HttpMethod.POST, 
            request, 
            WalletDTO.class
        );
        
        return response.getBody();
    }
    
    /**
     * خصم مال من المحفظة
     */
    public WalletDTO deductMoneyFromWallet(Long userId, Double amount, String jwtToken) {
        String url = userServiceUrl + "/api/users/wallet/" + userId + "/deduct";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + jwtToken);
        
        HttpEntity<Double> request = new HttpEntity<>(amount, headers);
        ResponseEntity<WalletDTO> response = restTemplate.exchange(
            url, 
            HttpMethod.POST, 
            request, 
            WalletDTO.class
        );
        
        return response.getBody();
    }
    
    /**
     * التحقق من كفاية الرصيد
     */
    public boolean hasSufficientBalance(Long userId, Double requiredAmount, String jwtToken) {
        try {
            WalletDTO wallet = getWalletBalance(userId, jwtToken);
            return wallet != null && wallet.getBalance() >= requiredAmount;
        } catch (Exception e) {
            return false;
        }
    }
} 