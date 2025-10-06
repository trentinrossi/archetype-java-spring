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
public class CreateTransactionRequest {
    
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    @Pattern(regexp = "\\d{16}", message = "Card number must contain only digits")
    @Schema(description = "Card number (16 digits)", example = "1234567890123456", required = true)
    private String cardNumber;
    
    @NotBlank(message = "Transaction ID is required")
    @Size(min = 16, max = 16, message = "Transaction ID must be exactly 16 characters")
    @Schema(description = "Transaction identifier (16 characters)", example = "TXN1234567890123", required = true)
    private String transactionId;
    
    @NotBlank(message = "Transaction data is required")
    @Size(max = 318, message = "Transaction data must not exceed 318 characters")
    @Schema(description = "Transaction data payload (up to 318 characters)", example = "Transaction data content...", required = true)
    private String transactionData;
    
    @Size(max = 4, message = "Transaction type must not exceed 4 characters")
    @Schema(description = "Transaction type", example = "PURCH", required = false)
    private String transactionType;
    
    @Size(max = 15, message = "Transaction amount must not exceed 15 characters")
    @Schema(description = "Transaction amount", example = "10000", required = false)
    private String transactionAmount;
    
    @Size(max = 8, message = "Transaction date must not exceed 8 characters")
    @Schema(description = "Transaction date", example = "20231015", required = false)
    private String transactionDate;
    
    @Size(max = 6, message = "Transaction time must not exceed 6 characters")
    @Schema(description = "Transaction time", example = "123456", required = false)
    private String transactionTime;
    
    @Size(max = 15, message = "Merchant ID must not exceed 15 characters")
    @Schema(description = "Merchant ID", example = "MERCHANT123", required = false)
    private String merchantId;
    
    @Size(max = 50, message = "Merchant name must not exceed 50 characters")
    @Schema(description = "Merchant name", example = "ABC Store", required = false)
    private String merchantName;
    
    @Size(max = 4, message = "Merchant category must not exceed 4 characters")
    @Schema(description = "Merchant category", example = "5411", required = false)
    private String merchantCategory;
    
    @Size(max = 6, message = "Authorization code must not exceed 6 characters")
    @Schema(description = "Authorization code", example = "123456", required = false)
    private String authorizationCode;
    
    @Size(max = 2, message = "Response code must not exceed 2 characters")
    @Schema(description = "Response code", example = "00", required = false)
    private String responseCode;
    
    @Size(max = 8, message = "Terminal ID must not exceed 8 characters")
    @Schema(description = "Terminal ID", example = "TERM001", required = false)
    private String terminalId;
    
    @Size(max = 3, message = "Currency code must not exceed 3 characters")
    @Schema(description = "Currency code", example = "USD", required = false)
    private String currencyCode;
    
    @NotBlank(message = "Transaction status is required")
    @Size(max = 1, message = "Transaction status must be 1 character")
    @Pattern(regexp = "[PADRSC]", message = "Transaction status must be P, A, D, R, S, or C")
    @Schema(description = "Transaction status", example = "P", required = true)
    private String transactionStatus;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UpdateTransactionRequest {
    
    @Size(max = 318, message = "Transaction data must not exceed 318 characters")
    @Schema(description = "Transaction data payload (up to 318 characters)", example = "Updated transaction data content...", required = false)
    private String transactionData;
    
    @Size(max = 4, message = "Transaction type must not exceed 4 characters")
    @Schema(description = "Transaction type", example = "PURCH", required = false)
    private String transactionType;
    
    @Size(max = 15, message = "Transaction amount must not exceed 15 characters")
    @Schema(description = "Transaction amount", example = "10000", required = false)
    private String transactionAmount;
    
    @Size(max = 50, message = "Merchant name must not exceed 50 characters")
    @Schema(description = "Merchant name", example = "ABC Store", required = false)
    private String merchantName;
    
    @Size(max = 4, message = "Merchant category must not exceed 4 characters")
    @Schema(description = "Merchant category", example = "5411", required = false)
    private String merchantCategory;
    
    @Size(max = 6, message = "Authorization code must not exceed 6 characters")
    @Schema(description = "Authorization code", example = "123456", required = false)
    private String authorizationCode;
    
    @Size(max = 2, message = "Response code must not exceed 2 characters")
    @Schema(description = "Response code", example = "00", required = false)
    private String responseCode;
    
    @Size(max = 3, message = "Currency code must not exceed 3 characters")
    @Schema(description = "Currency code", example = "USD", required = false)
    private String currencyCode;
    
    @Size(max = 1, message = "Transaction status must be 1 character")
    @Pattern(regexp = "[PADRSC]", message = "Transaction status must be P, A, D, R, S, or C")
    @Schema(description = "Transaction status", example = "A", required = false)
    private String transactionStatus;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TransactionResponse {
    
    @Schema(description = "Card number (16 digits)", example = "1234567890123456", required = true)
    private String cardNumber;
    
    @Schema(description = "Formatted card number", example = "**** **** **** 3456", required = true)
    private String formattedCardNumber;
    
    @Schema(description = "Transaction identifier (16 characters)", example = "TXN1234567890123", required = true)
    private String transactionId;
    
    @Schema(description = "Transaction data payload", example = "Transaction data content...", required = true)
    private String transactionData;
    
    @Schema(description = "Transaction type", example = "PURCH", required = false)
    private String transactionType;
    
    @Schema(description = "Transaction amount", example = "10000", required = false)
    private String transactionAmount;
    
    @Schema(description = "Formatted amount", example = "$100.00", required = false)
    private String formattedAmount;
    
    @Schema(description = "Transaction date", example = "20231015", required = false)
    private String transactionDate;
    
    @Schema(description = "Transaction time", example = "123456", required = false)
    private String transactionTime;
    
    @Schema(description = "Transaction date time", example = "20231015 123456", required = false)
    private String transactionDateTime;
    
    @Schema(description = "Merchant ID", example = "MERCHANT123", required = false)
    private String merchantId;
    
    @Schema(description = "Merchant name", example = "ABC Store", required = false)
    private String merchantName;
    
    @Schema(description = "Merchant category", example = "5411", required = false)
    private String merchantCategory;
    
    @Schema(description = "Authorization code", example = "123456", required = false)
    private String authorizationCode;
    
    @Schema(description = "Response code", example = "00", required = false)
    private String responseCode;
    
    @Schema(description = "Terminal ID", example = "TERM001", required = false)
    private String terminalId;
    
    @Schema(description = "Currency code", example = "USD", required = false)
    private String currencyCode;
    
    @Schema(description = "Transaction status", example = "A", required = true)
    private String transactionStatus;
    
    @Schema(description = "Transaction status description", example = "Approved", required = true)
    private String transactionStatusDescription;
    
    @Schema(description = "Transaction is approved", example = "true", required = true)
    private Boolean isApproved;
    
    @Schema(description = "Transaction is declined", example = "false", required = true)
    private Boolean isDeclined;
    
    @Schema(description = "Transaction is pending", example = "false", required = true)
    private Boolean isPending;
    
    @Schema(description = "Transaction is reversed", example = "false", required = true)
    private Boolean isReversed;
    
    @Schema(description = "Transaction is settled", example = "false", required = true)
    private Boolean isSettled;
    
    @Schema(description = "Transaction is cancelled", example = "false", required = true)
    private Boolean isCancelled;
    
    @Schema(description = "Transaction is purchase", example = "true", required = true)
    private Boolean isPurchaseTransaction;
    
    @Schema(description = "Transaction is withdrawal", example = "false", required = true)
    private Boolean isWithdrawalTransaction;
    
    @Schema(description = "Transaction is refund", example = "false", required = true)
    private Boolean isRefundTransaction;
    
    @Schema(description = "Transaction is balance inquiry", example = "false", required = true)
    private Boolean isBalanceInquiry;
    
    @Schema(description = "Transaction has authorization code", example = "true", required = true)
    private Boolean hasAuthorizationCode;
    
    @Schema(description = "Transaction is high value", example = "false", required = true)
    private Boolean isHighValueTransaction;
    
    @Schema(description = "Transaction is international", example = "false", required = true)
    private Boolean isInternationalTransaction;
    
    @Schema(description = "Entry mode description", example = "Chip", required = false)
    private String entryModeDescription;
    
    @Schema(description = "Merchant info", example = "ABC Store (ID: MERCHANT123)", required = false)
    private String merchantInfo;
    
    @Schema(description = "Timestamp when the transaction was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the transaction was last updated", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TransactionSequentialReadRequest {
    
    @Schema(description = "Starting card number for sequential read", example = "1234567890123456", required = false)
    private String startingCardNumber;
    
    @Schema(description = "Starting transaction ID for sequential read", example = "TXN1234567890123", required = false)
    private String startingTransactionId;
    
    @Schema(description = "Number of records to read", example = "10", required = false)
    private Integer recordCount;
    
    @Size(max = 4, message = "Transaction type filter must not exceed 4 characters")
    @Schema(description = "Transaction type filter", example = "PURCH", required = false)
    private String transactionTypeFilter;
    
    @Size(max = 1, message = "Status filter must be 1 character")
    @Schema(description = "Transaction status filter", example = "A", required = false)
    private String statusFilter;
    
    @Size(max = 8, message = "From date must not exceed 8 characters")
    @Schema(description = "From date filter", example = "20231001", required = false)
    private String fromDate;
    
    @Size(max = 8, message = "To date must not exceed 8 characters")
    @Schema(description = "To date filter", example = "20231031", required = false)
    private String toDate;
    
    @Size(max = 15, message = "Merchant ID filter must not exceed 15 characters")
    @Schema(description = "Merchant ID filter", example = "MERCHANT123", required = false)
    private String merchantIdFilter;
    
    @Schema(description = "Include only approved transactions", example = "true", required = false)
    private Boolean approvedOnly;
    
    @Schema(description = "Include only high value transactions", example = "false", required = false)
    private Boolean highValueOnly;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TransactionSequentialReadResponse {
    
    @Schema(description = "Starting card number used", example = "1234567890123456", required = true)
    private String startingCardNumber;
    
    @Schema(description = "Starting transaction ID used", example = "TXN1234567890123", required = false)
    private String startingTransactionId;
    
    @Schema(description = "Number of records requested", example = "10", required = true)
    private Integer recordsRequested;
    
    @Schema(description = "Number of records returned", example = "8", required = true)
    private Integer recordsReturned;
    
    @Schema(description = "Has more records available", example = "true", required = true)
    private Boolean hasMoreRecords;
    
    @Schema(description = "Next card number for continuation", example = "1234567890123457", required = false)
    private String nextCardNumber;
    
    @Schema(description = "Next transaction ID for continuation", example = "TXN1234567890131", required = false)
    private String nextTransactionId;
    
    @Schema(description = "List of transaction responses", required = true)
    private List<TransactionResponse> transactions;
    
    @Schema(description = "Total transactions matching criteria", example = "25", required = true)
    private Long totalMatchingTransactions;
    
    @Schema(description = "Applied filters summary", example = "Type: PURCH, Status: A, Date: 20231001-20231031", required = false)
    private String appliedFilters;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TransactionCompositeKeyRequest {
    
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    @Pattern(regexp = "\\d{16}", message = "Card number must contain only digits")
    @Schema(description = "Card number (16 digits)", example = "1234567890123456", required = true)
    private String cardNumber;
    
    @NotBlank(message = "Transaction ID is required")
    @Size(min = 16, max = 16, message = "Transaction ID must be exactly 16 characters")
    @Schema(description = "Transaction identifier (16 characters)", example = "TXN1234567890123", required = true)
    private String transactionId;
}