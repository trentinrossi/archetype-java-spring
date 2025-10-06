package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    
    @NotBlank(message = "Account ID is required")
    @Size(max = 11, message = "Account ID must not exceed 11 characters")
    @Schema(description = "Account ID", example = "12345678901", required = true)
    private String acctId;
    
    @NotBlank(message = "Account active status is required")
    @Size(max = 1, message = "Account active status must be 1 character")
    @Pattern(regexp = "[YNA]", message = "Account active status must be Y, N, or A")
    @Schema(description = "Account active status", example = "Y", required = true)
    private String acctActiveStatus;
    
    @Schema(description = "Current balance", example = "1500.75", required = true)
    private BigDecimal acctCurrBal;
    
    @Schema(description = "Credit limit", example = "5000.00", required = true)
    private BigDecimal acctCreditLimit;
    
    @Schema(description = "Cash credit limit", example = "1000.00", required = true)
    private BigDecimal acctCashCreditLimit;
    
    @NotBlank(message = "Account open date is required")
    @Schema(description = "Account open date", example = "2023-01-15", required = true)
    private String acctOpenDate;
    
    @NotBlank(message = "Account expiration date is required")
    @Schema(description = "Account expiration date", example = "2025-01-15", required = true)
    private String acctExpirationDate;
    
    @Schema(description = "Account reissue date", example = "2024-01-15", required = false)
    private String acctReissueDate;
    
    @Schema(description = "Current cycle credit", example = "250.00", required = false)
    private BigDecimal acctCurrCycCredit;
    
    @Schema(description = "Current cycle debit", example = "150.00", required = false)
    private BigDecimal acctCurrCycDebit;
    
    @NotBlank(message = "Account group ID is required")
    @Size(max = 10, message = "Account group ID must not exceed 10 characters")
    @Schema(description = "Account group ID", example = "GRP001", required = true)
    private String acctGroupId;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UpdateAccountRequest {
    
    @Size(max = 1, message = "Account active status must be 1 character")
    @Pattern(regexp = "[YNA]", message = "Account active status must be Y, N, or A")
    @Schema(description = "Account active status", example = "Y", required = false)
    private String acctActiveStatus;
    
    @Schema(description = "Current balance", example = "1500.75", required = false)
    private BigDecimal acctCurrBal;
    
    @Schema(description = "Credit limit", example = "5000.00", required = false)
    private BigDecimal acctCreditLimit;
    
    @Schema(description = "Cash credit limit", example = "1000.00", required = false)
    private BigDecimal acctCashCreditLimit;
    
    @Schema(description = "Account expiration date", example = "2025-01-15", required = false)
    private String acctExpirationDate;
    
    @Schema(description = "Account reissue date", example = "2024-01-15", required = false)
    private String acctReissueDate;
    
    @Schema(description = "Current cycle credit", example = "250.00", required = false)
    private BigDecimal acctCurrCycCredit;
    
    @Schema(description = "Current cycle debit", example = "150.00", required = false)
    private BigDecimal acctCurrCycDebit;
    
    @Size(max = 10, message = "Account group ID must not exceed 10 characters")
    @Schema(description = "Account group ID", example = "GRP001", required = false)
    private String acctGroupId;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class AccountResponse {
    
    @Schema(description = "Account ID", example = "12345678901", required = true)
    private String acctId;
    
    @Schema(description = "Account active status", example = "Y", required = true)
    private String acctActiveStatus;
    
    @Schema(description = "Account status description", example = "Active", required = true)
    private String accountStatusDescription;
    
    @Schema(description = "Current balance", example = "1500.75", required = true)
    private BigDecimal acctCurrBal;
    
    @Schema(description = "Credit limit", example = "5000.00", required = true)
    private BigDecimal acctCreditLimit;
    
    @Schema(description = "Cash credit limit", example = "1000.00", required = true)
    private BigDecimal acctCashCreditLimit;
    
    @Schema(description = "Available credit", example = "3500.25", required = true)
    private BigDecimal availableCredit;
    
    @Schema(description = "Available cash credit", example = "500.00", required = true)
    private BigDecimal availableCashCredit;
    
    @Schema(description = "Account open date", example = "2023-01-15", required = true)
    private String acctOpenDate;
    
    @Schema(description = "Account expiration date", example = "2025-01-15", required = true)
    private String acctExpirationDate;
    
    @Schema(description = "Account reissue date", example = "2024-01-15", required = false)
    private String acctReissueDate;
    
    @Schema(description = "Current cycle credit", example = "250.00", required = true)
    private BigDecimal acctCurrCycCredit;
    
    @Schema(description = "Current cycle debit", example = "150.00", required = true)
    private BigDecimal acctCurrCycDebit;
    
    @Schema(description = "Cycle net amount", example = "100.00", required = true)
    private BigDecimal cycleNetAmount;
    
    @Schema(description = "Account group ID", example = "GRP001", required = true)
    private String acctGroupId;
    
    @Schema(description = "Is account active", example = "true", required = true)
    private Boolean isActive;
    
    @Schema(description = "Is account expired", example = "false", required = true)
    private Boolean isExpired;
    
    @Schema(description = "Is account over limit", example = "false", required = true)
    private Boolean isOverLimit;
    
    @Schema(description = "Is account over cash limit", example = "false", required = true)
    private Boolean isOverCashLimit;
    
    @Schema(description = "Has recent reissue", example = "true", required = true)
    private Boolean hasRecentReissue;
    
    @Schema(description = "Timestamp when the account was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the account was last updated", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class AccountSequentialReadRequest {
    
    @Schema(description = "Starting account ID for sequential read", example = "12345678901", required = false)
    private String startingAcctId;
    
    @Schema(description = "Number of records to read", example = "10", required = false)
    private Integer recordCount;
    
    @Size(max = 1, message = "Status filter must be 1 character")
    @Schema(description = "Account status filter", example = "Y", required = false)
    private String statusFilter;
    
    @Size(max = 10, message = "Group ID filter must not exceed 10 characters")
    @Schema(description = "Account group ID filter", example = "GRP001", required = false)
    private String groupIdFilter;
    
    @Schema(description = "Include inactive accounts", example = "false", required = false)
    private Boolean includeInactive;
    
    @Schema(description = "Include expired accounts", example = "false", required = false)
    private Boolean includeExpired;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class AccountSequentialReadResponse {
    
    @Schema(description = "Starting account ID used", example = "12345678901", required = true)
    private String startingAcctId;
    
    @Schema(description = "Number of records requested", example = "10", required = true)
    private Integer recordsRequested;
    
    @Schema(description = "Number of records returned", example = "8", required = true)
    private Integer recordsReturned;
    
    @Schema(description = "Has more records available", example = "true", required = true)
    private Boolean hasMoreRecords;
    
    @Schema(description = "Next account ID for continuation", example = "12345678909", required = false)
    private String nextAcctId;
    
    @Schema(description = "List of account responses", required = true)
    private List<AccountResponse> accounts;
    
    @Schema(description = "Total accounts matching criteria", example = "25", required = true)
    private Long totalMatchingAccounts;
    
    @Schema(description = "Applied filters summary", example = "Status: Y, Group: GRP001", required = false)
    private String appliedFilters;
}