package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardUpdateDTO {
    
    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits")
    private String cardNumber;
    
    @NotBlank(message = "Account ID is required")
    @Pattern(regexp = "^\\d{11}$", message = "Account ID must be 11 digits")
    private String accountId;
    
    @NotBlank(message = "Embossed name is required")
    @Size(max = 50, message = "Embossed name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Embossed name can only contain alphabets and spaces")
    private String embossedName;
    
    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private LocalDate expirationDate;
    
    @NotBlank(message = "Active status is required")
    @Pattern(regexp = "^[YN]$", message = "Active status must be Y or N")
    private String activeStatus;
}
