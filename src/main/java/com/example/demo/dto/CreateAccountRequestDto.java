package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^(?!00000000000)\\d{11}$", message = "Account Filter must be a non-zero 11 digit number")
    @Size(min = 11, max = 11, message = "Account number must be 11 digits")
    private String accountId;

    @NotBlank(message = "Active status is required")
    @Pattern(regexp = "^[YN]$", message = "Account status must be Y (active) or N (inactive)")
    private String activeStatus;

    @NotNull(message = "Current balance is required")
    @Digits(integer = 10, fraction = 2, message = "Invalid current balance format")
    private BigDecimal currentBalance;

    @NotNull(message = "Credit limit is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit must be a valid positive amount")
    @Digits(integer = 10, fraction = 2, message = "Credit limit must be a valid positive amount")
    private BigDecimal creditLimit;

    @NotNull(message = "Cash credit limit is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Cash credit limit must be a valid positive amount")
    @Digits(integer = 10, fraction = 2, message = "Cash credit limit must be a valid positive amount")
    private BigDecimal cashCreditLimit;

    @NotBlank(message = "Open date is required")
    @Pattern(regexp = "^\\d{8}$", message = "Open date must be in YYYYMMDD format")
    private String openDate;

    @NotBlank(message = "Expiration date is required")
    @Pattern(regexp = "^\\d{8}$", message = "Expiration date must be in YYYYMMDD format")
    private String expirationDate;

    @NotBlank(message = "Reissue date is required")
    @Pattern(regexp = "^\\d{8}$", message = "Reissue date must be in YYYYMMDD format")
    private String reissueDate;

    @NotNull(message = "Current cycle credit is required")
    @Digits(integer = 10, fraction = 2, message = "Invalid current cycle credit format")
    private BigDecimal currentCycleCredit;

    @NotNull(message = "Current cycle debit is required")
    @Digits(integer = 10, fraction = 2, message = "Invalid current cycle debit format")
    private BigDecimal currentCycleDebit;

    @Size(max = 10, message = "Group ID must not exceed 10 characters")
    private String groupId;

    @NotBlank(message = "Account data is required")
    @Size(max = 289, message = "Account data must not exceed 289 characters")
    private String accountData;
}
