package com.microServices.payment.clients;

import com.microServices.payment.dto.WalletDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${user.service.url:http://localhost:8090}")
    private String userServiceUrl;
    
    /**
     * الحصول على رصيد المحفظة مع معالجة الأخطاء
     */
    public WalletDTO getWalletBalance(Long userId, String jwtToken) {
        try {
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
        } catch (ResourceAccessException e) {
            // في حالة timeout أو فشل الاتصال
            WalletDTO errorWallet = new WalletDTO();
            errorWallet.setUserId(userId);
            errorWallet.setBalance(0.0);
            errorWallet.setErrorMessage("Service timeout or connection failed after 5 seconds");
            return errorWallet;
        } catch (Exception e) {
            // في حالة أي خطأ آخر
            WalletDTO errorWallet = new WalletDTO();
            errorWallet.setUserId(userId);
            errorWallet.setBalance(0.0);
            errorWallet.setErrorMessage("Service error: " + e.getMessage());
            return errorWallet;
        }
    }
    
    /**
     * إضافة مال للمحفظة مع معالجة الأخطاء
     */
    public WalletDTO addMoneyToWallet(Long userId, Double amount, String jwtToken) {
        try {
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
        } catch (ResourceAccessException e) {
            // في حالة timeout أو فشل الاتصال
            WalletDTO errorWallet = new WalletDTO();
            errorWallet.setUserId(userId);
            errorWallet.setBalance(0.0);
            errorWallet.setErrorMessage("Service timeout or connection failed after 5 seconds");
            return errorWallet;
        } catch (Exception e) {
            // في حالة أي خطأ آخر
            WalletDTO errorWallet = new WalletDTO();
            errorWallet.setUserId(userId);
            errorWallet.setBalance(0.0);
            errorWallet.setErrorMessage("Service error: " + e.getMessage());
            return errorWallet;
        }
    }
    
    /**
     * خصم مال من المحفظة مع معالجة الأخطاء
     */
    public WalletDTO deductMoneyFromWallet(Long userId, Double amount, String jwtToken) {
        try {
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
        } catch (ResourceAccessException e) {
            // في حالة timeout أو فشل الاتصال
            WalletDTO errorWallet = new WalletDTO();
            errorWallet.setUserId(userId);
            errorWallet.setBalance(0.0);
            errorWallet.setErrorMessage("Service timeout or connection failed after 5 seconds");
            return errorWallet;
        } catch (Exception e) {
            // في حالة أي خطأ آخر
            WalletDTO errorWallet = new WalletDTO();
            errorWallet.setUserId(userId);
            errorWallet.setBalance(0.0);
            errorWallet.setErrorMessage("Service error: " + e.getMessage());
            return errorWallet;
        }
    }
    
    /**
     * التحقق من كفاية الرصيد مع معالجة الأخطاء
     */
    public boolean hasSufficientBalance(Long userId, Double requiredAmount, String jwtToken) {
        try {
            WalletDTO wallet = getWalletBalance(userId, jwtToken);
            return wallet != null && wallet.getBalance() >= requiredAmount && wallet.getErrorMessage() == null;
        } catch (Exception e) {
            return false;
        }
    }
} 