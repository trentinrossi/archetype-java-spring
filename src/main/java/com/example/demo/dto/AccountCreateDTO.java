package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateDTO {
    
    @NotBlank(message = "Account ID is required")
    @Pattern(regexp = "^\\d{11}$", message = "Account ID must be 11 digits")
    private String accountId;
    
    @NotBlank(message = "Active status is required")
    @Pattern(regexp = "^[YN]$", message = "Active status must be Y or N")
    private String activeStatus;
    
    @NotNull(message = "Credit limit is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Credit limit must be positive")
    @Digits(integer = 10, fraction = 2, message = "Credit limit must have at most 10 integer digits and 2 decimal places")
    private BigDecimal creditLimit;
    
    @NotNull(message = "Cash credit limit is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cash credit limit must be positive")
    @Digits(integer = 10, fraction = 2, message = "Cash credit limit must have at most 10 integer digits and 2 decimal places")
    private BigDecimal cashCreditLimit;
    
    @NotNull(message = "Open date is required")
    @PastOrPresent(message = "Open date cannot be in the future")
    private LocalDate openDate;
    
    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private LocalDate expirationDate;
    
    private LocalDate reissueDate;
    
    @Size(max = 10, message = "Group ID must not exceed 10 characters")
    private String groupId;
}
