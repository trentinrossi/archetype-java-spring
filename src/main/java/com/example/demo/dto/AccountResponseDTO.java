package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    
    private String accountNumber;
    private String customerId;
    private String customerName;
    private BigDecimal limitAmount;
    private BigDecimal balance;
    private BigDecimal creditLimit;
    private BigDecimal cashCreditLimit;
    private LocalDate openDate;
    private LocalDate expiryDate;
    private LocalDate reissueDate;
    private BigDecimal currCycleCredit;
    private BigDecimal currCycleDebit;
    private String groupId;
    private String status;
    private String statusDescription;
    private Integer ficoCreditScore;
    private String custPhone;
    private Integer custFicoCreditScore;
    private String custFirstName;
    private String custMiddleName;
    private String custLastName;
    private String fullCustomerName;
    private String custAddrLine1;
    private String custAddrLine2;
    private String custAddrLine3;
    private String custAddrStateCd;
    private String custAddrCountryCd;
    private String custAddrZip;
    private String fullAddress;
    private String custPhoneHome;
    private String custPhoneWork;
    private String custSsn;
    private String custGovtIssuedId;
    private String custDobYyyyMmDd;
    private String cardNumber;
    private String cardCvvCd;
    private String cardEmbossedName;
    private LocalDate cardExpiryDate;
    private String cardActiveStatus;
    private BigDecimal availableCredit;
    private BigDecimal availableCashCredit;
    private boolean isActive;
    private boolean isCardActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public String getStatusDescription() {
        if (status == null) return null;
        switch (status) {
            case "A": return "Active";
            case "C": return "Closed";
            case "S": return "Suspended";
            default: return "Unknown";
        }
    }
    
    public boolean getIsActive() {
        return "A".equals(status);
    }
    
    public boolean getIsCardActive() {
        return "Y".equals(cardActiveStatus);
    }
}