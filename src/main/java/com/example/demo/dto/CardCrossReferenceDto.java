package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardCrossReferenceRequest {
    
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    @Pattern(regexp = "\\d{16}", message = "Card number must contain only digits")
    @Schema(description = "Card number (16 digits)", example = "1234567890123456", required = true)
    private String cardNumber;
    
    @NotBlank(message = "Cross reference data is required")
    @Size(max = 34, message = "Cross reference data must not exceed 34 characters")
    @Schema(description = "Cross reference data (up to 34 characters)", example = "ACCT12345678901|CUST123456789|01", required = true)
    private String crossReferenceData;
    
    @Size(max = 20, message = "Account number must not exceed 20 characters")
    @Schema(description = "Account number", example = "ACCT12345678901", required = false)
    private String accountNumber;
    
    @Size(max = 2, message = "Account type must not exceed 2 characters")
    @Schema(description = "Account type", example = "01", required = false)
    private String accountType;
    
    @Size(max = 12, message = "Customer ID must not exceed 12 characters")
    @Schema(description = "Customer ID", example = "CUST123456789", required = false)
    private String customerId;
    
    @NotBlank(message = "Card status is required")
    @Size(max = 1, message = "Card status must be 1 character")
    @Pattern(regexp = "[ABSCEP]", message = "Card status must be A, B, S, C, E, or P")
    @Schema(description = "Card status", example = "A", required = true)
    private String cardStatus;
    
    @Size(max = 2, message = "Card type must not exceed 2 characters")
    @Schema(description = "Card type", example = "01", required = false)
    private String cardType;
    
    @Size(max = 8, message = "Issue date must not exceed 8 characters")
    @Schema(description = "Issue date", example = "20231001", required = false)
    private String issueDate;
    
    @Size(max = 8, message = "Expiry date must not exceed 8 characters")
    @Schema(description = "Expiry date", example = "20251001", required = false)
    private String expiryDate;
    
    @Size(max = 4, message = "Branch code must not exceed 4 characters")
    @Schema(description = "Branch code", example = "1001", required = false)
    private String branchCode;
    
    @Size(max = 3, message = "Product code must not exceed 3 characters")
    @Schema(description = "Product code", example = "001", required = false)
    private String productCode;
    
    @Size(max = 10, message = "Daily limit must not exceed 10 characters")
    @Schema(description = "Daily limit", example = "100000", required = false)
    private String dailyLimit;
    
    @Size(max = 10, message = "Monthly limit must not exceed 10 characters")
    @Schema(description = "Monthly limit", example = "500000", required = false)
    private String monthlyLimit;
    
    @Size(max = 26, message = "Emboss name must not exceed 26 characters")
    @Schema(description = "Emboss name", example = "JOHN DOE", required = false)
    private String embossName;
    
    @Size(max = 1, message = "International flag must be 1 character")
    @Pattern(regexp = "[YN01]", message = "International flag must be Y, N, 0, or 1")
    @Schema(description = "International flag", example = "Y", required = false)
    private String internationalFlag;
    
    @Size(max = 1, message = "Contactless flag must be 1 character")
    @Pattern(regexp = "[YN01]", message = "Contactless flag must be Y, N, 0, or 1")
    @Schema(description = "Contactless flag", example = "Y", required = false)
    private String contactlessFlag;
    
    @Size(max = 1, message = "Mobile payment flag must be 1 character")
    @Pattern(regexp = "[YN01]", message = "Mobile payment flag must be Y, N, 0, or 1")
    @Schema(description = "Mobile payment flag", example = "Y", required = false)
    private String mobilePaymentFlag;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UpdateCardCrossReferenceRequest {
    
    @Size(max = 34, message = "Cross reference data must not exceed 34 characters")
    @Schema(description = "Cross reference data (up to 34 characters)", example = "ACCT12345678901|CUST123456789|01", required = false)
    private String crossReferenceData;
    
    @Size(max = 20, message = "Account number must not exceed 20 characters")
    @Schema(description = "Account number", example = "ACCT12345678901", required = false)
    private String accountNumber;
    
    @Size(max = 2, message = "Account type must not exceed 2 characters")
    @Schema(description = "Account type", example = "01", required = false)
    private String accountType;
    
    @Size(max = 12, message = "Customer ID must not exceed 12 characters")
    @Schema(description = "Customer ID", example = "CUST123456789", required = false)
    private String customerId;
    
    @Size(max = 1, message = "Card status must be 1 character")
    @Pattern(regexp = "[ABSCEP]", message = "Card status must be A, B, S, C, E, or P")
    @Schema(description = "Card status", example = "A", required = false)
    private String cardStatus;
    
    @Size(max = 2, message = "Card type must not exceed 2 characters")
    @Schema(description = "Card type", example = "01", required = false)
    private String cardType;
    
    @Size(max = 8, message = "Expiry date must not exceed 8 characters")
    @Schema(description = "Expiry date", example = "20251001", required = false)
    private String expiryDate;
    
    @Size(max = 10, message = "Daily limit must not exceed 10 characters")
    @Schema(description = "Daily limit", example = "100000", required = false)
    private String dailyLimit;
    
    @Size(max = 10, message = "Monthly limit must not exceed 10 characters")
    @Schema(description = "Monthly limit", example = "500000", required = false)
    private String monthlyLimit;
    
    @Size(max = 8, message = "Last activity date must not exceed 8 characters")
    @Schema(description = "Last activity date", example = "20231015", required = false)
    private String lastActivityDate;
    
    @Size(max = 2, message = "Block code must not exceed 2 characters")
    @Schema(description = "Block code", example = "00", required = false)
    private String blockCode;
    
    @Size(max = 1, message = "International flag must be 1 character")
    @Pattern(regexp = "[YN01]", message = "International flag must be Y, N, 0, or 1")
    @Schema(description = "International flag", example = "Y", required = false)
    private String internationalFlag;
    
    @Size(max = 1, message = "Contactless flag must be 1 character")
    @Pattern(regexp = "[YN01]", message = "Contactless flag must be Y, N, 0, or 1")
    @Schema(description = "Contactless flag", example = "Y", required = false)
    private String contactlessFlag;
    
    @Size(max = 1, message = "Mobile payment flag must be 1 character")
    @Pattern(regexp = "[YN01]", message = "Mobile payment flag must be Y, N, 0, or 1")
    @Schema(description = "Mobile payment flag", example = "Y", required = false)
    private String mobilePaymentFlag;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CardCrossReferenceResponse {
    
    @Schema(description = "Card number (16 digits)", example = "1234567890123456", required = true)
    private String cardNumber;
    
    @Schema(description = "Formatted card number", example = "**** **** **** 3456", required = true)
    private String formattedCardNumber;
    
    @Schema(description = "Cross reference data", example = "ACCT12345678901|CUST123456789|01", required = true)
    private String crossReferenceData;
    
    @Schema(description = "Account number", example = "ACCT12345678901", required = false)
    private String accountNumber;
    
    @Schema(description = "Account type", example = "01", required = false)
    private String accountType;
    
    @Schema(description = "Account type description", example = "Checking Account", required = false)
    private String accountTypeDescription;
    
    @Schema(description = "Customer ID", example = "CUST123456789", required = false)
    private String customerId;
    
    @Schema(description = "Card status", example = "A", required = true)
    private String cardStatus;
    
    @Schema(description = "Card status description", example = "Active", required = true)
    private String cardStatusDescription;
    
    @Schema(description = "Card type", example = "01", required = false)
    private String cardType;
    
    @Schema(description = "Card type description", example = "Debit Card", required = false)
    private String cardTypeDescription;
    
    @Schema(description = "Issue date", example = "20231001", required = false)
    private String issueDate;
    
    @Schema(description = "Formatted issue date", example = "01/10/2023", required = false)
    private String formattedIssueDate;
    
    @Schema(description = "Expiry date", example = "20251001", required = false)
    private String expiryDate;
    
    @Schema(description = "Formatted expiry date", example = "10/25", required = false)
    private String formattedExpiryDate;
    
    @Schema(description = "Branch code", example = "1001", required = false)
    private String branchCode;
    
    @Schema(description = "Product code", example = "001", required = false)
    private String productCode;
    
    @Schema(description = "Daily limit", example = "100000", required = false)
    private String dailyLimit;
    
    @Schema(description = "Formatted daily limit", example = "$1,000.00", required = false)
    private String formattedDailyLimit;
    
    @Schema(description = "Monthly limit", example = "500000", required = false)
    private String monthlyLimit;
    
    @Schema(description = "Formatted monthly limit", example = "$5,000.00", required = false)
    private String formattedMonthlyLimit;
    
    @Schema(description = "Last activity date", example = "20231015", required = false)
    private String lastActivityDate;
    
    @Schema(description = "Block code", example = "00", required = false)
    private String blockCode;
    
    @Schema(description = "Block code description", example = "No Block", required = false)
    private String blockCodeDescription;
    
    @Schema(description = "Emboss name", example = "JOHN DOE", required = false)
    private String embossName;
    
    @Schema(description = "International flag", example = "Y", required = false)
    private String internationalFlag;
    
    @Schema(description = "International enabled", example = "true", required = true)
    private Boolean isInternationalEnabled;
    
    @Schema(description = "Contactless flag", example = "Y", required = false)
    private String contactlessFlag;
    
    @Schema(description = "Contactless enabled", example = "true", required = true)
    private Boolean isContactlessEnabled;
    
    @Schema(description = "Mobile payment flag", example = "Y", required = false)
    private String mobilePaymentFlag;
    
    @Schema(description = "Mobile payment enabled", example = "true", required = true)
    private Boolean isMobilePaymentEnabled;
    
    @Schema(description = "Card is active", example = "true", required = true)
    private Boolean isActive;
    
    @Schema(description = "Card is blocked", example = "false", required = true)
    private Boolean isBlocked;
    
    @Schema(description = "Card is suspended", example = "false", required = true)
    private Boolean isSuspended;
    
    @Schema(description = "Card is cancelled", example = "false", required = true)
    private Boolean isCancelled;
    
    @Schema(description = "Card is expired", example = "false", required = true)
    private Boolean isExpired;
    
    @Schema(description = "Card is pending", example = "false", required = true)
    private Boolean isPending;
    
    @Schema(description = "Card is debit card", example = "true", required = true)
    private Boolean isDebitCard;
    
    @Schema(description = "Card is credit card", example = "false", required = true)
    private Boolean isCreditCard;
    
    @Schema(description = "Card is prepaid card", example = "false", required = true)
    private Boolean isPrepaidCard;
    
    @Schema(description = "Card is business card", example = "false", required = true)
    private Boolean isBusinessCard;
    
    @Schema(description = "Card can perform transactions", example = "true", required = true)
    private Boolean canPerformTransactions;
    
    @Schema(description = "Card is recently active", example = "true", required = true)
    private Boolean isRecentlyActive;
    
    @Schema(description = "Card is high value", example = "false", required = true)
    private Boolean isHighValueCard;
    
    @Schema(description = "Card summary", example = "**** **** **** 3456 - Debit Card (Active) - 10/25", required = true)
    private String cardSummary;
    
    @Schema(description = "Timestamp when the card cross reference was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the card cross reference was last updated", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CardCrossReferenceSequentialReadRequest {
    
    @Schema(description = "Starting card number for sequential read", example = "1234567890123456", required = false)
    private String startingCardNumber;
    
    @Schema(description = "Number of records to read", example = "10", required = false)
    private Integer recordCount;
    
    @Size(max = 1, message = "Status filter must be 1 character")
    @Schema(description = "Card status filter", example = "A", required = false)
    private String statusFilter;
    
    @Size(max = 2, message = "Card type filter must not exceed 2 characters")
    @Schema(description = "Card type filter", example = "01", required = false)
    private String cardTypeFilter;
    
    @Size(max = 2, message = "Account type filter must not exceed 2 characters")
    @Schema(description = "Account type filter", example = "01", required = false)
    private String accountTypeFilter;
    
    @Size(max = 4, message = "Branch code filter must not exceed 4 characters")
    @Schema(description = "Branch code filter", example = "1001", required = false)
    private String branchCodeFilter;
    
    @Schema(description = "Include inactive cards", example = "false", required = false)
    private Boolean includeInactive;
    
    @Schema(description = "Include expired cards", example = "false", required = false)
    private Boolean includeExpired;
    
    @Schema(description = "Include blocked cards", example = "false", required = false)
    private Boolean includeBlocked;
    
    @Schema(description = "International enabled only", example = "false", required = false)
    private Boolean internationalEnabledOnly;
    
    @Schema(description = "Contactless enabled only", example = "false", required = false)
    private Boolean contactlessEnabledOnly;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CardCrossReferenceSequentialReadResponse {
    
    @Schema(description = "Starting card number used", example = "1234567890123456", required = true)
    private String startingCardNumber;
    
    @Schema(description = "Number of records requested", example = "10", required = true)
    private Integer recordsRequested;
    
    @Schema(description = "Number of records returned", example = "8", required = true)
    private Integer recordsReturned;
    
    @Schema(description = "Has more records available", example = "true", required = true)
    private Boolean hasMoreRecords;
    
    @Schema(description = "Next card number for continuation", example = "1234567890123464", required = false)
    private String nextCardNumber;
    
    @Schema(description = "List of card cross reference responses", required = true)
    private List<CardCrossReferenceResponse> cardCrossReferences;
    
    @Schema(description = "Total card cross references matching criteria", example = "25", required = true)
    private Long totalMatchingCardCrossReferences;
    
    @Schema(description = "Applied filters summary", example = "Status: A, Type: 01, Branch: 1001", required = false)
    private String appliedFilters;
}