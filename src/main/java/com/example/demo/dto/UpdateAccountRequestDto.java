package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {

    @Pattern(regexp = "^[YN]$", message = "Account status must be Y (active) or N (inactive)")
    private String activeStatus;

    @Digits(integer = 10, fraction = 2, message = "Invalid current balance format")
    private BigDecimal currentBalance;

    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit must be a valid positive amount")
    @Digits(integer = 10, fraction = 2, message = "Credit limit must be a valid positive amount")
    private BigDecimal creditLimit;

    @DecimalMin(value = "0.0", inclusive = true, message = "Cash credit limit must be a valid positive amount")
    @Digits(integer = 10, fraction = 2, message = "Cash credit limit must be a valid positive amount")
    private BigDecimal cashCreditLimit;

    @Pattern(regexp = "^\\d{8}$", message = "Expiration date must be in YYYYMMDD format")
    private String expirationDate;

    @Pattern(regexp = "^\\d{8}$", message = "Reissue date must be in YYYYMMDD format")
    private String reissueDate;

    @Digits(integer = 10, fraction = 2, message = "Invalid current cycle credit format")
    private BigDecimal currentCycleCredit;

    @Digits(integer = 10, fraction = 2, message = "Invalid current cycle debit format")
    private BigDecimal currentCycleDebit;

    @Size(max = 10, message = "Group ID must not exceed 10 characters")
    private String groupId;

    @Size(max = 289, message = "Account data must not exceed 289 characters")
    private String accountData;
}
