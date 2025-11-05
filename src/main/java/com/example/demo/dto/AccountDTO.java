package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    
    @NotBlank(message = "Account ID is required")
    @Size(min = 11, max = 11, message = "Account ID must be 11 characters")
    private String accountId;
    
    @NotBlank(message = "Customer ID is required")
    @Size(min = 9, max = 9, message = "Customer ID must be 9 characters")
    private String customerId;
    
    @NotNull(message = "Current balance is required")
    @Digits(integer = 17, fraction = 2, message = "Current balance must have at most 17 integer digits and 2 decimal places")
    private BigDecimal currentBalance;
    
    @NotNull(message = "Credit limit is required")
    @DecimalMin(value = "0.00", message = "Credit limit must be positive")
    @Digits(integer = 17, fraction = 2, message = "Credit limit must have at most 17 integer digits and 2 decimal places")
    private BigDecimal creditLimit;
    
    @NotNull(message = "Current cycle credit is required")
    @Digits(integer = 17, fraction = 2, message = "Current cycle credit must have at most 17 integer digits and 2 decimal places")
    private BigDecimal currentCycleCredit;
    
    @NotNull(message = "Current cycle debit is required")
    @Digits(integer = 17, fraction = 2, message = "Current cycle debit must have at most 17 integer digits and 2 decimal places")
    private BigDecimal currentCycleDebit;
    
    @NotNull(message = "Expiration date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
    
    @NotBlank(message = "Account status is required")
    @Size(max = 10, message = "Account status must not exceed 10 characters")
    private String accountStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
