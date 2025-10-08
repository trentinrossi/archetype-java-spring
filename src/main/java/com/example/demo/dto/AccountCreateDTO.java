package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateDTO {
    
    @NotBlank(message = "Account number is mandatory")
    @Pattern(regexp = "^[1-9]\\d{10}$", message = "Account number must be 11-digit non-zero numeric value")
    private String accountNumber;
    
    @NotBlank(message = "Customer ID is mandatory")
    @Size(max = 9, message = "Customer ID cannot exceed 9 characters")
    private String customerId;
    
    @NotBlank(message = "Customer name is mandatory")
    @Size(max = 25, message = "Customer name cannot exceed 25 characters")
    private String customerName;
    
    @NotNull(message = "Limit amount is mandatory")
    @DecimalMin(value = "0.01", message = "Limit amount must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Limit amount must have at most 8 integer digits and 2 decimal places")
    private BigDecimal limitAmount;
    
    @NotNull(message = "Balance is mandatory")
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Balance must have at most 8 integer digits and 2 decimal places")
    private BigDecimal balance;
    
    @NotNull(message = "Credit limit is mandatory")
    @DecimalMin(value = "0.01", message = "Credit limit must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Credit limit must have at most 8 integer digits and 2 decimal places")
    private BigDecimal creditLimit;
    
    @NotNull(message = "Cash credit limit is mandatory")
    @DecimalMin(value = "0.01", message = "Cash credit limit must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Cash credit limit must have at most 8 integer digits and 2 decimal places")
    private BigDecimal cashCreditLimit;
    
    @NotNull(message = "Open date is mandatory")
    @PastOrPresent(message = "Open date cannot be in the future")
    private LocalDate openDate;
    
    @NotNull(message = "Expiry date is mandatory")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;
    
    private LocalDate reissueDate;
    
    @Digits(integer = 8, fraction = 2, message = "Current cycle credit must have at most 8 integer digits and 2 decimal places")
    private BigDecimal currCycleCredit;
    
    @Digits(integer = 8, fraction = 2, message = "Current cycle debit must have at most 8 integer digits and 2 decimal places")
    private BigDecimal currCycleDebit;
    
    @Size(max = 3, message = "Group ID cannot exceed 3 characters")
    private String groupId;
    
    @NotBlank(message = "Status is mandatory")
    @Pattern(regexp = "^[ACS]$", message = "Status must be A (Active), C (Closed), or S (Suspended)")
    private String status;
    
    @Min(value = 300, message = "FICO credit score must be between 300 and 850")
    @Max(value = 850, message = "FICO credit score must be between 300 and 850")
    private Integer ficoCreditScore;
    
    @Pattern(regexp = "^\\d{10}$", message = "Customer phone must be 10-digit North American format")
    private String custPhone;
    
    @Min(value = 300, message = "Customer FICO credit score must be between 300 and 850")
    @Max(value = 850, message = "Customer FICO credit score must be between 300 and 850")
    private Integer custFicoCreditScore;
    
    @Size(max = 25, message = "First name cannot exceed 25 characters")
    private String custFirstName;
    
    @Size(max = 25, message = "Middle name cannot exceed 25 characters")
    private String custMiddleName;
    
    @Size(max = 25, message = "Last name cannot exceed 25 characters")
    private String custLastName;
    
    @Size(max = 50, message = "Address line 1 cannot exceed 50 characters")
    private String custAddrLine1;
    
    @Size(max = 50, message = "Address line 2 cannot exceed 50 characters")
    private String custAddrLine2;
    
    @Size(max = 50, message = "Address line 3 cannot exceed 50 characters")
    private String custAddrLine3;
    
    @Pattern(regexp = "^[A-Z]{2}$", message = "State code must be 2-letter uppercase format")
    private String custAddrStateCd;
    
    @Pattern(regexp = "^[A-Z]{3}$", message = "Country code must be 3-letter uppercase format")
    private String custAddrCountryCd;
    
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "ZIP code must be in format 12345 or 12345-6789")
    private String custAddrZip;
    
    @Pattern(regexp = "^\\d{10}$", message = "Home phone must be 10-digit North American format")
    private String custPhoneHome;
    
    @Pattern(regexp = "^\\d{10}$", message = "Work phone must be 10-digit North American format")
    private String custPhoneWork;
    
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{4}$", message = "SSN must be in format XXX-XX-XXXX")
    private String custSsn;
    
    @Size(max = 20, message = "Government issued ID cannot exceed 20 characters")
    private String custGovtIssuedId;
    
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$", message = "Date of birth must be in CCYYMMDD format")
    private String custDobYyyyMmDd;
    
    @Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits")
    private String cardNumber;
    
    @Pattern(regexp = "^\\d{3}$", message = "CVV must be 3 digits")
    private String cardCvvCd;
    
    @Size(max = 50, message = "Card embossed name cannot exceed 50 characters")
    private String cardEmbossedName;
    
    @Future(message = "Card expiry date must be in the future")
    private LocalDate cardExpiryDate;
    
    @Pattern(regexp = "^[YN]$", message = "Card active status must be Y or N")
    private String cardActiveStatus;
}