package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {
    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^[1-9]\\d{10}$", message = "Account number must be 11 digits and cannot start with zero")
    @Schema(description = "Account number", example = "12345678901", required = true)
    private String accountNumber;

    @Size(max = 9, message = "Customer ID must not exceed 9 characters")
    @Schema(description = "Customer ID", example = "123456789", required = false)
    private String customerId;

    @Size(max = 25, message = "Customer name must not exceed 25 characters")
    @Schema(description = "Customer name", example = "John Doe", required = false)
    private String customerName;

    @DecimalMin(value = "0.01", message = "Limit amount must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Limit amount must have at most 8 integer digits and 2 decimal places")
    @Schema(description = "Limit amount", example = "5000.00", required = false)
    private BigDecimal limitAmount;

    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Balance must have at most 8 integer digits and 2 decimal places")
    @Schema(description = "Current balance", example = "1500.00", required = false)
    private BigDecimal balance;

    @DecimalMin(value = "0.01", message = "Credit limit must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Credit limit must have at most 8 integer digits and 2 decimal places")
    @Schema(description = "Credit limit", example = "5000.00", required = false)
    private BigDecimal creditLimit;

    @DecimalMin(value = "0.01", message = "Cash credit limit must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Cash credit limit must have at most 8 integer digits and 2 decimal places")
    @Schema(description = "Cash credit limit", example = "1000.00", required = false)
    private BigDecimal cashCreditLimit;

    @Schema(description = "Account open date in CCYYMMDD format", example = "20230115", required = false)
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "Open date must be in CCYYMMDD format")
    private String openDate;

    @Schema(description = "Account expiry date in CCYYMMDD format", example = "20260115", required = false)
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "Expiry date must be in CCYYMMDD format")
    private String expiryDate;

    @Schema(description = "Card reissue date in CCYYMMDD format", example = "20240115", required = false)
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "Reissue date must be in CCYYMMDD format")
    private String reissueDate;

    @Digits(integer = 8, fraction = 2, message = "Current cycle credit must have at most 8 integer digits and 2 decimal places")
    @Schema(description = "Current cycle credit", example = "500.00", required = false)
    private BigDecimal currCycleCredit;

    @Digits(integer = 8, fraction = 2, message = "Current cycle debit must have at most 8 integer digits and 2 decimal places")
    @Schema(description = "Current cycle debit", example = "300.00", required = false)
    private BigDecimal currCycleDebit;

    @Size(max = 3, message = "Group ID must not exceed 3 characters")
    @Schema(description = "Group ID", example = "GRP", required = false)
    private String groupId;

    @Pattern(regexp = "^[ACS]$", message = "Status must be A (Active), C (Closed), or S (Suspended)")
    @Schema(description = "Account status", example = "A", required = false)
    private String status;

    @Min(value = 300, message = "FICO score must be between 300 and 850")
    @Max(value = 850, message = "FICO score must be between 300 and 850")
    @Schema(description = "FICO credit score", example = "750", required = false)
    private Integer ficoCreditScore;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    @Schema(description = "Customer phone", example = "5551234567", required = false)
    private String custPhone;

    @Min(value = 300, message = "Customer FICO score must be between 300 and 850")
    @Max(value = 850, message = "Customer FICO score must be between 300 and 850")
    @Schema(description = "Customer FICO credit score", example = "750", required = false)
    private Integer custFicoCreditScore;

    @Size(max = 25, message = "First name must not exceed 25 characters")
    @Schema(description = "Customer first name", example = "John", required = false)
    private String custFirstName;

    @Size(max = 25, message = "Middle name must not exceed 25 characters")
    @Schema(description = "Customer middle name", example = "Michael", required = false)
    private String custMiddleName;

    @Size(max = 25, message = "Last name must not exceed 25 characters")
    @Schema(description = "Customer last name", example = "Doe", required = false)
    private String custLastName;

    @Size(max = 50, message = "Address line 1 must not exceed 50 characters")
    @Schema(description = "Customer address line 1", example = "123 Main St", required = false)
    private String custAddrLine1;

    @Size(max = 50, message = "Address line 2 must not exceed 50 characters")
    @Schema(description = "Customer address line 2", example = "Apt 4B", required = false)
    private String custAddrLine2;

    @Size(max = 50, message = "Address line 3 must not exceed 50 characters")
    @Schema(description = "Customer address line 3", example = "Building A", required = false)
    private String custAddrLine3;

    @Size(max = 2, message = "State code must be 2 characters")
    @Pattern(regexp = "^[A-Z]{2}$", message = "State code must be 2 uppercase letters")
    @Schema(description = "Customer address state code", example = "NY", required = false)
    private String custAddrStateCd;

    @Size(max = 3, message = "Country code must not exceed 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Country code must be 3 uppercase letters")
    @Schema(description = "Customer address country code", example = "USA", required = false)
    private String custAddrCountryCd;

    @Size(max = 10, message = "ZIP code must not exceed 10 characters")
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "ZIP code must be in format 12345 or 12345-6789")
    @Schema(description = "Customer address ZIP", example = "10001", required = false)
    private String custAddrZip;

    @Pattern(regexp = "^\\d{10}$", message = "Home phone number must be 10 digits")
    @Schema(description = "Customer home phone", example = "5551234567", required = false)
    private String custPhoneHome;

    @Pattern(regexp = "^\\d{10}$", message = "Work phone number must be 10 digits")
    @Schema(description = "Customer work phone", example = "5559876543", required = false)
    private String custPhoneWork;

    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{4}$", message = "SSN must be in format XXX-XX-XXXX")
    @Schema(description = "Customer SSN", example = "123-45-6789", required = false)
    private String custSsn;

    @Size(max = 20, message = "Government issued ID must not exceed 20 characters")
    @Schema(description = "Customer government issued ID", example = "DL123456789", required = false)
    private String custGovtIssuedId;

    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "Date of birth must be in CCYYMMDD format")
    @Schema(description = "Customer date of birth in CCYYMMDD format", example = "19850615", required = false)
    private String custDobYyyyMmDd;

    @Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits")
    @Schema(description = "Card number", example = "1234567890123456", required = false)
    private String cardNumber;

    @Pattern(regexp = "^\\d{3}$", message = "CVV must be 3 digits")
    @Schema(description = "Card CVV code", example = "123", required = false)
    private String cardCvvCd;

    @Size(max = 50, message = "Card embossed name must not exceed 50 characters")
    @Schema(description = "Card embossed name", example = "JOHN M DOE", required = false)
    private String cardEmbossedName;

    @Schema(description = "Card expiry date in CCYYMMDD format", example = "20261231", required = false)
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "Card expiry date must be in CCYYMMDD format")
    private String cardExpiryDate;

    @Pattern(regexp = "^[YN]$", message = "Card active status must be Y (Yes) or N (No)")
    @Schema(description = "Card active status", example = "Y", required = false)
    private String cardActiveStatus;
}