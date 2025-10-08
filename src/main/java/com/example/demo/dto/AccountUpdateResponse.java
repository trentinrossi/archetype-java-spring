package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateResponse {
    @Schema(description = "Account number", example = "12345678901", required = true)
    private String accountNumber;

    @Schema(description = "Customer ID", example = "123456789", required = true)
    private String customerId;

    @Schema(description = "Customer name", example = "John Doe", required = true)
    private String customerName;

    @Schema(description = "Limit amount", example = "5000.00", required = true)
    private BigDecimal limitAmount;

    @Schema(description = "Current balance", example = "1500.00", required = true)
    private BigDecimal balance;

    @Schema(description = "Credit limit", example = "5000.00", required = true)
    private BigDecimal creditLimit;

    @Schema(description = "Cash credit limit", example = "1000.00", required = true)
    private BigDecimal cashCreditLimit;

    @Schema(description = "Account open date", example = "2023-01-15", required = true)
    private LocalDate openDate;

    @Schema(description = "Account expiry date", example = "2026-01-15", required = true)
    private LocalDate expiryDate;

    @Schema(description = "Card reissue date", example = "2024-01-15", required = false)
    private LocalDate reissueDate;

    @Schema(description = "Current cycle credit", example = "500.00", required = false)
    private BigDecimal currCycleCredit;

    @Schema(description = "Current cycle debit", example = "300.00", required = false)
    private BigDecimal currCycleDebit;

    @Schema(description = "Group ID", example = "GRP", required = false)
    private String groupId;

    @Schema(description = "Account status", example = "A", required = true)
    private String status;

    @Schema(description = "FICO credit score", example = "750", required = false)
    private Integer ficoCreditScore;

    @Schema(description = "Customer phone", example = "5551234567", required = false)
    private String custPhone;

    @Schema(description = "Customer FICO credit score", example = "750", required = false)
    private Integer custFicoCreditScore;

    @Schema(description = "Customer first name", example = "John", required = false)
    private String custFirstName;

    @Schema(description = "Customer middle name", example = "Michael", required = false)
    private String custMiddleName;

    @Schema(description = "Customer last name", example = "Doe", required = false)
    private String custLastName;

    @Schema(description = "Customer address line 1", example = "123 Main St", required = false)
    private String custAddrLine1;

    @Schema(description = "Customer address line 2", example = "Apt 4B", required = false)
    private String custAddrLine2;

    @Schema(description = "Customer address line 3", example = "Building A", required = false)
    private String custAddrLine3;

    @Schema(description = "Customer address state code", example = "NY", required = false)
    private String custAddrStateCd;

    @Schema(description = "Customer address country code", example = "USA", required = false)
    private String custAddrCountryCd;

    @Schema(description = "Customer address ZIP", example = "10001", required = false)
    private String custAddrZip;

    @Schema(description = "Customer home phone", example = "5551234567", required = false)
    private String custPhoneHome;

    @Schema(description = "Customer work phone", example = "5559876543", required = false)
    private String custPhoneWork;

    @Schema(description = "Customer SSN", example = "123-45-6789", required = false)
    private String custSsn;

    @Schema(description = "Customer government issued ID", example = "DL123456789", required = false)
    private String custGovtIssuedId;

    @Schema(description = "Customer date of birth", example = "19850615", required = false)
    private String custDobYyyyMmDd;

    @Schema(description = "Card number", example = "1234567890123456", required = false)
    private String cardNumber;

    @Schema(description = "Card CVV code", example = "123", required = false)
    private String cardCvvCd;

    @Schema(description = "Card embossed name", example = "JOHN M DOE", required = false)
    private String cardEmbossedName;

    @Schema(description = "Card expiry date", example = "2026-12-31", required = false)
    private LocalDate cardExpiryDate;

    @Schema(description = "Card active status", example = "Y", required = false)
    private String cardActiveStatus;

    @Schema(description = "Full customer name", example = "John Michael Doe", required = false)
    private String fullCustomerName;

    @Schema(description = "Full address", example = "123 Main St, Apt 4B, Building A, NY 10001", required = false)
    private String fullAddress;

    @Schema(description = "Available credit", example = "3500.00", required = false)
    private BigDecimal availableCredit;

    @Schema(description = "Available cash credit", example = "500.00", required = false)
    private BigDecimal availableCashCredit;

    @Schema(description = "Update operation status", example = "SUCCESS", required = true)
    private String updateStatus;

    @Schema(description = "Update operation message", example = "Account updated successfully", required = false)
    private String updateMessage;

    @Schema(description = "List of fields that were updated", required = false)
    private List<String> updatedFields;

    @Schema(description = "Timestamp when the account was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the account was last updated", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
}