package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {

    @Schema(description = "Account active status - Y for active, N for inactive", example = "Y", required = false)
    @Pattern(regexp = "^[YN]$", message = "Account status must be Y (active) or N (inactive)")
    private String activeStatus;

    @Schema(description = "Current account balance with 2 decimal places, signed amount", example = "1500.50", required = false)
    @Digits(integer = 10, fraction = 2, message = "Invalid current balance format")
    private BigDecimal currentBalance;

    @Schema(description = "Credit limit with 2 decimal places, signed amount", example = "5000.00", required = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit must be a valid positive amount")
    @Digits(integer = 10, fraction = 2, message = "Credit limit must be a valid positive amount")
    private BigDecimal creditLimit;

    @Schema(description = "Cash credit limit with 2 decimal places, signed amount", example = "1000.00", required = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Cash credit limit must be a valid positive amount")
    @Digits(integer = 10, fraction = 2, message = "Cash credit limit must be a valid positive amount")
    private BigDecimal cashCreditLimit;

    @Schema(description = "Account expiration date in YYYYMMDD format", example = "20280115", required = false)
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "Invalid expiration date")
    private String expirationDate;

    @Schema(description = "Card reissue date in YYYYMMDD format", example = "20250115", required = false)
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "Invalid reissue date")
    private String reissueDate;

    @Schema(description = "Current cycle credit amount with 2 decimal places, signed amount", example = "250.00", required = false)
    @Digits(integer = 10, fraction = 2, message = "Invalid current cycle credit format")
    private BigDecimal currentCycleCredit;

    @Schema(description = "Current cycle debit amount with 2 decimal places, signed amount", example = "100.00", required = false)
    @Digits(integer = 10, fraction = 2, message = "Invalid current cycle debit format")
    private BigDecimal currentCycleDebit;

    @Schema(description = "Account grouping identifier", example = "GRP001", required = false)
    @Size(max = 10, message = "Group ID cannot exceed 10 characters")
    private String groupId;
}
