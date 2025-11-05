package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillPaymentDTO {
    
    @NotBlank(message = "Account ID is required")
    @Pattern(regexp = "^\\d{11}$", message = "Account ID must be 11 digits")
    private String accountId;
    
    private BigDecimal currentBalance;
    private BigDecimal paymentAmount;
    private String transactionId;
    private String confirmPayment;
}
