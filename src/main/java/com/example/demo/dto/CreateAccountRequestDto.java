package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @Schema(description = "Unique account identifier used as the record key", example = "12345678901", required = true)
    @NotNull(message = "Account number is required")
    @Pattern(regexp = "^(?!00000000000)\\d{11}$", message = "Account Filter must be a non-zero 11 digit number")
    private String accountId;

    @Schema(description = "Account active status indicator - Y for active, N for inactive", example = "Y", required = true)
    @NotNull(message = "Account status must be Y (active) or N (inactive)")
    @Pattern(regexp = "^[YN]$", message = "Account status must be Y (active) or N (inactive)")
    private String activeStatus;

    @Schema(description = "Current account balance with 2 decimal places, signed amount", example = "1500.50", required = true)
    @NotNull(message = "Invalid current balance format")
    @Digits(integer = 10, fraction = 2, message = "Invalid current balance format")
    private BigDecimal currentBalance;

    @Schema(description = "Credit limit with 2 decimal places, signed amount", example = "5000.00", required = true)
    @NotNull(message = "Credit limit must be a valid positive amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit must be a valid positive amount")
    @Digits(integer = 10, fraction = 2, message = "Credit limit must be a valid positive amount")
    private BigDecimal creditLimit;

    @Schema(description = "Cash credit limit with 2 decimal places, signed amount", example = "1000.00", required = true)
    @NotNull(message = "Cash credit limit must be a valid positive amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Cash credit limit must be a valid positive amount")
    @Digits(integer = 10, fraction = 2, message = "Cash credit limit must be a valid positive amount")
    private BigDecimal cashCreditLimit;

    @Schema(description = "Account opening date in YYYYMMDD format", example = "2023-01-15", required = true)
    @NotNull(message = "Invalid account open date")
    @PastOrPresent(message = "Invalid account open date")
    private LocalDate openDate;

    @Schema(description = "Account expiration date in YYYYMMDD format", example = "2026-01-15", required = true)
    @NotNull(message = "Invalid expiration date")
    private LocalDate expirationDate;

    @Schema(description = "Card reissue date in YYYYMMDD format", example = "2024-01-15", required = true)
    @NotNull(message = "Invalid reissue date")
    private LocalDate reissueDate;

    @Schema(description = "Current cycle credit amount with 2 decimal places, signed amount", example = "500.00", required = true)
    @NotNull(message = "Invalid current cycle credit format")
    @Digits(integer = 10, fraction = 2, message = "Invalid current cycle credit format")
    private BigDecimal currentCycleCredit;

    @Schema(description = "Current cycle debit amount with 2 decimal places, signed amount", example = "300.00", required = true)
    @NotNull(message = "Invalid current cycle debit format")
    @Digits(integer = 10, fraction = 2, message = "Invalid current cycle debit format")
    private BigDecimal currentCycleDebit;

    @Schema(description = "Account grouping identifier", example = "GROUP001", required = false)
    @Size(max = 10, message = "Group ID must not exceed 10 characters")
    private String groupId;
}
