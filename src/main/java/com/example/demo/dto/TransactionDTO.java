package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    
    private String transactionId;
    
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be 16 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Card number must be numeric")
    private String cardNumber;
    
    @NotBlank(message = "Account ID is required")
    @Size(min = 11, max = 11, message = "Account ID must be 11 characters")
    private String accountId;
    
    @NotBlank(message = "Transaction type code is required")
    @Size(min = 2, max = 2, message = "Transaction type code must be 2 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Transaction type code must be numeric")
    private String transactionTypeCode;
    
    @NotNull(message = "Transaction category code is required")
    @Min(value = 0, message = "Transaction category code must be positive")
    @Max(value = 9999, message = "Transaction category code must be 4 digits")
    private Integer transactionCategoryCode;
    
    @NotBlank(message = "Transaction source is required")
    @Size(max = 10, message = "Transaction source must not exceed 10 characters")
    private String transactionSource;
    
    @NotBlank(message = "Transaction description is required")
    @Size(max = 100, message = "Transaction description must not exceed 100 characters")
    private String transactionDescription;
    
    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "-99999999.99", message = "Transaction amount is out of range")
    @DecimalMax(value = "99999999.99", message = "Transaction amount is out of range")
    @Digits(integer = 9, fraction = 2, message = "Transaction amount must have at most 9 integer digits and 2 decimal places")
    private BigDecimal transactionAmount;
    
    @NotNull(message = "Merchant ID is required")
    @Min(value = 0, message = "Merchant ID must be positive")
    @Max(value = 999999999L, message = "Merchant ID must be 9 digits")
    private Long merchantId;
    
    @NotBlank(message = "Merchant name is required")
    @Size(max = 50, message = "Merchant name must not exceed 50 characters")
    private String merchantName;
    
    @NotBlank(message = "Merchant city is required")
    @Size(max = 50, message = "Merchant city must not exceed 50 characters")
    private String merchantCity;
    
    @NotBlank(message = "Merchant ZIP is required")
    @Size(max = 10, message = "Merchant ZIP must not exceed 10 characters")
    private String merchantZip;
    
    @NotNull(message = "Original timestamp is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime originalTimestamp;
    
    @NotNull(message = "Processing timestamp is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime processingTimestamp;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
