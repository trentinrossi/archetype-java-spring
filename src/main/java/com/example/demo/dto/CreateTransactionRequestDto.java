package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {
    
    @Schema(description = "Card number", example = "1234567890123456", required = true, maxLength = 16)
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be numeric and exactly 16 digits")
    private String cardNumber;
    
    @Schema(description = "Type code", example = "01", required = true, maxLength = 2)
    @NotBlank(message = "Type code is required")
    @Size(min = 2, max = 2, message = "Type code must be exactly 2 characters")
    @Pattern(regexp = "^[0-9]{2}$", message = "Type code must be numeric and exactly 2 digits")
    private String typeCode;
    
    @Schema(description = "Category code", example = "5411", required = true, maxLength = 4)
    @NotBlank(message = "Category code is required")
    @Size(min = 4, max = 4, message = "Category code must be exactly 4 characters")
    @Pattern(regexp = "^[0-9]{4}$", message = "Category code must be numeric and exactly 4 digits")
    private String categoryCode;
    
    @Schema(description = "Source", example = "POS", required = true, maxLength = 10)
    @NotBlank(message = "Source is required")
    @Size(max = 10, message = "Source must not exceed 10 characters")
    private String source;
    
    @Schema(description = "Description", example = "Purchase at grocery store", maxLength = 100)
    @Size(max = 100, message = "Description must not exceed 100 characters")
    private String description;
    
    @Schema(description = "Amount", example = "12345.67", required = true)
    @NotNull(message = "Amount is required")
    @Digits(integer = 9, fraction = 2, message = "Amount must be in format +/-99999999.99")
    @DecimalMin(value = "-99999999.99", message = "Amount must be greater than or equal to -99999999.99")
    @DecimalMax(value = "99999999.99", message = "Amount must be less than or equal to 99999999.99")
    private BigDecimal amount;
    
    @Schema(description = "Merchant ID", example = "123456789", required = true)
    @NotNull(message = "Merchant ID is required")
    @Min(value = 100000000, message = "Merchant ID must be exactly 9 digits")
    @Max(value = 999999999, message = "Merchant ID must be exactly 9 digits")
    private Long merchantId;
    
    @Schema(description = "Merchant name", example = "ABC Grocery Store", required = true, maxLength = 50)
    @NotBlank(message = "Merchant name is required")
    @Size(max = 50, message = "Merchant name must not exceed 50 characters")
    private String merchantName;
    
    @Schema(description = "Merchant city", example = "New York", maxLength = 50)
    @Size(max = 50, message = "Merchant city must not exceed 50 characters")
    private String merchantCity;
    
    @Schema(description = "Merchant ZIP code", example = "10001", maxLength = 10)
    @Size(max = 10, message = "Merchant ZIP must not exceed 10 characters")
    private String merchantZip;
    
    @Schema(description = "Original timestamp", example = "2024-01-15T10:30:00", required = true)
    @NotNull(message = "Original timestamp is required")
    private LocalDateTime originalTimestamp;
    
    @Schema(description = "Processing timestamp", example = "2024-01-15T10:30:05", required = true)
    @NotNull(message = "Processing timestamp is required")
    private LocalDateTime processingTimestamp;
}
