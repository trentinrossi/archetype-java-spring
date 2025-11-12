package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @Schema(description = "Unique account identifier used as the record key", example = "12345678901", required = true)
    @NotNull(message = "Account number is required")
    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^(?!00000000000)[0-9]{11}$", message = "Account Filter must be a non-zero 11 digit number")
    @Size(min = 11, max = 11, message = "Account Filter must be a non-zero 11 digit number")
    private String accountId;

    @Schema(description = "Account active status - Y for active, N for inactive", example = "Y", required = true)
    @NotNull(message = "Account status must be Y (active) or N (inactive)")
    @NotBlank(message = "Account status must be Y (active) or N (inactive)")
    @Pattern(regexp = "^[YN]$", message = "Account status must be Y (active) or N (inactive)")
    @Size(min = 1, max = 1, message = "Account status must be Y (active) or N (inactive)")
    private String activeStatus;

    @Schema(description = "Current account balance with 2 decimal places, signed amount", example = "2500.75", required = true)
    @NotNull(message = "Invalid current balance format")
    @Digits(integer = 10, fraction = 2, message = "Invalid current balance format")
    private BigDecimal currentBalance;

    @Schema(description = "Credit limit with 2 decimal places, signed amount", example = "10000.00", required = true)
    @NotNull(message = "Credit limit must be a valid positive amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit must be a valid positive amount")
    @Digits(integer = 10, fraction = 2, message = "Credit limit must be a valid positive amount")
    private BigDecimal creditLimit;

    @Schema(description = "Cash credit limit with 2 decimal places, signed amount", example = "2000.00", required = true)
    @NotNull(message = "Cash credit limit must be a valid positive amount")
    @DecimalMin(value = "0.0", inclusive = true, message = "Cash credit limit must be a valid positive amount")
    @Digits(integer = 10, fraction = 2, message = "Cash credit limit must be a valid positive amount")
    private BigDecimal cashCreditLimit;

    @Schema(description = "Account opening date in YYYYMMDD format", example = "20230101", required = true)
    @NotNull(message = "Invalid account open date")
    @NotBlank(message = "Invalid account open date")
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "Invalid account open date")
    @Size(min = 8, max = 8, message = "Invalid account open date")
    private String openDate;

    @Schema(description = "Account expiration date in YYYYMMDD format", example = "20280101", required = true)
    @NotNull(message = "Invalid expiration date")
    @NotBlank(message = "Invalid expiration date")
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "Invalid expiration date")
    @Size(min = 8, max = 8, message = "Invalid expiration date")
    private String expirationDate;

    @Schema(description = "Card reissue date in YYYYMMDD format", example = "20250601", required = true)
    @NotNull(message = "Invalid reissue date")
    @NotBlank(message = "Invalid reissue date")
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "Invalid reissue date")
    @Size(min = 8, max = 8, message = "Invalid reissue date")
    private String reissueDate;

    @Schema(description = "Current cycle credit amount with 2 decimal places, signed amount", example = "750.00", required = true)
    @NotNull(message = "Invalid current cycle credit format")
    @Digits(integer = 10, fraction = 2, message = "Invalid current cycle credit format")
    private BigDecimal currentCycleCredit;

    @Schema(description = "Current cycle debit amount with 2 decimal places, signed amount", example = "450.00", required = true)
    @NotNull(message = "Invalid current cycle debit format")
    @Digits(integer = 10, fraction = 2, message = "Invalid current cycle debit format")
    private BigDecimal currentCycleDebit;

    @Schema(description = "Account grouping identifier", example = "GROUP123", required = false)
    @Size(max = 10, message = "Group ID cannot exceed 10 characters")
    private String groupId;

    @Schema(description = "Account active status indicator", example = "A", required = true)
    @NotNull(message = "Account status must be Y (active) or N (inactive)")
    @NotBlank(message = "Account status must be Y (active) or N (inactive)")
    @Size(min = 1, max = 1, message = "Account status must be a single character")
    private String accountStatus;
}
