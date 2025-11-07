package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for filtering credit card records
 * Business Rule BR004: Credit card records can be filtered by account ID and/or card number.
 * Filters are applied cumulatively if both are specified.
 * Business Rule BR008: Records that do not match the specified filter criteria are excluded from display.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardFilterRequestDto {
    
    @Schema(description = "Account ID filter - 11 numeric digits, blank or zeros indicates no filter", 
            example = "12345678901")
    private String accountId;
    
    @Schema(description = "Card number filter - 16 numeric digits, blank or zeros indicates no filter", 
            example = "1234567890123456")
    private String cardNumber;
    
    /**
     * Check if account ID filter should be applied
     * Business Rule: Can be blank or zeros to indicate no filter
     */
    public boolean hasAccountIdFilter() {
        if (accountId == null || accountId.trim().isEmpty()) {
            return false;
        }
        return !accountId.matches("0+");
    }
    
    /**
     * Check if card number filter should be applied
     * Business Rule: Can be blank or zeros to indicate no filter
     */
    public boolean hasCardNumberFilter() {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }
        return !cardNumber.matches("0+");
    }
    
    /**
     * Check if any filter is applied
     */
    public boolean hasAnyFilter() {
        return hasAccountIdFilter() || hasCardNumberFilter();
    }
}
