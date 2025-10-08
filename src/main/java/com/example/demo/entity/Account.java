package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @Column(name = "account_number", length = 11, nullable = false)
    private String accountNumber;
    
    @Column(name = "customer_id", length = 9, nullable = false)
    private String customerId;
    
    @Column(name = "customer_name", length = 25, nullable = false)
    private String customerName;
    
    @Column(name = "limit_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal limitAmount;
    
    @Column(name = "balance", precision = 10, scale = 2, nullable = false)
    private BigDecimal balance;
    
    @Column(name = "credit_limit", precision = 10, scale = 2, nullable = false)
    private BigDecimal creditLimit;
    
    @Column(name = "cash_credit_limit", precision = 10, scale = 2, nullable = false)
    private BigDecimal cashCreditLimit;
    
    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;
    
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;
    
    @Column(name = "reissue_date")
    private LocalDate reissueDate;
    
    @Column(name = "curr_cycle_credit", precision = 10, scale = 2)
    private BigDecimal currCycleCredit;
    
    @Column(name = "curr_cycle_debit", precision = 10, scale = 2)
    private BigDecimal currCycleDebit;
    
    @Column(name = "group_id", length = 3)
    private String groupId;
    
    @Column(name = "status", length = 1, nullable = false)
    private String status;
    
    @Column(name = "fico_credit_score")
    private Integer ficoCreditScore;
    
    @Column(name = "cust_phone", length = 10)
    private String custPhone;
    
    @Column(name = "cust_fico_credit_score")
    private Integer custFicoCreditScore;
    
    @Column(name = "cust_first_name", length = 25)
    private String custFirstName;
    
    @Column(name = "cust_middle_name", length = 25)
    private String custMiddleName;
    
    @Column(name = "cust_last_name", length = 25)
    private String custLastName;
    
    @Column(name = "cust_addr_line_1", length = 50)
    private String custAddrLine1;
    
    @Column(name = "cust_addr_line_2", length = 50)
    private String custAddrLine2;
    
    @Column(name = "cust_addr_line_3", length = 50)
    private String custAddrLine3;
    
    @Column(name = "cust_addr_state_cd", length = 2)
    private String custAddrStateCd;
    
    @Column(name = "cust_addr_country_cd", length = 3)
    private String custAddrCountryCd;
    
    @Column(name = "cust_addr_zip", length = 10)
    private String custAddrZip;
    
    @Column(name = "cust_phone_home", length = 10)
    private String custPhoneHome;
    
    @Column(name = "cust_phone_work", length = 10)
    private String custPhoneWork;
    
    @Column(name = "cust_ssn", length = 11)
    private String custSsn;
    
    @Column(name = "cust_govt_issued_id", length = 20)
    private String custGovtIssuedId;
    
    @Column(name = "cust_dob_yyyy_mm_dd", length = 10)
    private String custDobYyyyMmDd;
    
    @Column(name = "card_number", length = 16)
    private String cardNumber;
    
    @Column(name = "card_cvv_cd", length = 3)
    private String cardCvvCd;
    
    @Column(name = "card_embossed_name", length = 50)
    private String cardEmbossedName;
    
    @Column(name = "card_expiry_date")
    private LocalDate cardExpiryDate;
    
    @Column(name = "card_active_status", length = 1)
    private String cardActiveStatus;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Account(String accountNumber, String customerId, String customerName, BigDecimal limitAmount, 
                  BigDecimal balance, BigDecimal creditLimit, BigDecimal cashCreditLimit, 
                  LocalDate openDate, LocalDate expiryDate, String status) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.limitAmount = limitAmount;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.cashCreditLimit = cashCreditLimit;
        this.openDate = openDate;
        this.expiryDate = expiryDate;
        this.status = status;
    }
    
    public String getFullCustomerName() {
        StringBuilder fullName = new StringBuilder();
        if (custFirstName != null) fullName.append(custFirstName);
        if (custMiddleName != null) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(custMiddleName);
        }
        if (custLastName != null) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(custLastName);
        }
        return fullName.toString();
    }
    
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        if (custAddrLine1 != null) address.append(custAddrLine1);
        if (custAddrLine2 != null) {
            if (address.length() > 0) address.append(", ");
            address.append(custAddrLine2);
        }
        if (custAddrLine3 != null) {
            if (address.length() > 0) address.append(", ");
            address.append(custAddrLine3);
        }
        if (custAddrStateCd != null) {
            if (address.length() > 0) address.append(", ");
            address.append(custAddrStateCd);
        }
        if (custAddrZip != null) {
            if (address.length() > 0) address.append(" ");
            address.append(custAddrZip);
        }
        return address.toString();
    }
    
    public BigDecimal getAvailableCredit() {
        return creditLimit.subtract(balance);
    }
    
    public BigDecimal getAvailableCashCredit() {
        return cashCreditLimit.subtract(balance);
    }
    
    public boolean isActive() {
        return "A".equals(status);
    }
    
    public boolean isSuspended() {
        return "S".equals(status);
    }
    
    public boolean isClosed() {
        return "C".equals(status);
    }
    
    public boolean isCardActive() {
        return "Y".equals(cardActiveStatus);
    }
    
    public boolean isValidFicoScore() {
        return ficoCreditScore != null && ficoCreditScore >= 300 && ficoCreditScore <= 850;
    }
    
    public boolean isValidCustomerFicoScore() {
        return custFicoCreditScore != null && custFicoCreditScore >= 300 && custFicoCreditScore <= 850;
    }
    
    public boolean isAccountNumberValid() {
        return accountNumber != null && accountNumber.matches("^[1-9]\\d{10}$");
    }
    
    public boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\d{10}$");
    }
    
    public boolean isSsnValid() {
        return custSsn != null && custSsn.matches("^\\d{3}-\\d{2}-\\d{4}$");
    }
    
    public boolean isDateFormatValid(String dateString) {
        return dateString != null && dateString.matches("^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$");
    }
    
    public boolean isZipCodeValid() {
        return custAddrZip != null && custAddrZip.matches("^\\d{5}(-\\d{4})?$");
    }
    
    public boolean isStateCodeValid() {
        return custAddrStateCd != null && custAddrStateCd.matches("^[A-Z]{2}$");
    }
    
    public boolean isCountryCodeValid() {
        return custAddrCountryCd != null && custAddrCountryCd.matches("^[A-Z]{3}$");
    }
    
    public boolean isCardNumberValid() {
        return cardNumber != null && cardNumber.matches("^\\d{16}$");
    }
    
    public boolean isCvvValid() {
        return cardCvvCd != null && cardCvvCd.matches("^\\d{3}$");
    }
    
    public boolean isCustomerPhoneValid() {
        return isPhoneNumberValid(custPhone);
    }
    
    public boolean isHomePhoneValid() {
        return isPhoneNumberValid(custPhoneHome);
    }
    
    public boolean isWorkPhoneValid() {
        return isPhoneNumberValid(custPhoneWork);
    }
    
    public boolean hasValidMandatoryFields() {
        return accountNumber != null && !accountNumber.trim().isEmpty() &&
               customerId != null && !customerId.trim().isEmpty() &&
               customerName != null && !customerName.trim().isEmpty() &&
               limitAmount != null && limitAmount.compareTo(BigDecimal.ZERO) > 0 &&
               balance != null && balance.compareTo(BigDecimal.ZERO) >= 0 &&
               creditLimit != null && creditLimit.compareTo(BigDecimal.ZERO) > 0 &&
               cashCreditLimit != null && cashCreditLimit.compareTo(BigDecimal.ZERO) > 0 &&
               openDate != null &&
               expiryDate != null &&
               status != null && status.matches("^[ACS]$");
    }
    
    public boolean isSsnRangeValid() {
        if (!isSsnValid()) return false;
        String[] parts = custSsn.split("-");
        int area = Integer.parseInt(parts[0]);
        int group = Integer.parseInt(parts[1]);
        int serial = Integer.parseInt(parts[2]);
        
        return area >= 1 && area <= 899 && area != 666 &&
               group >= 1 && group <= 99 &&
               serial >= 1 && serial <= 9999;
    }
    
    public boolean isExpiryDateValid() {
        return expiryDate != null && expiryDate.isAfter(LocalDate.now());
    }
    
    public boolean isCardExpiryDateValid() {
        return cardExpiryDate == null || cardExpiryDate.isAfter(LocalDate.now());
    }
    
    public boolean isCreditLimitSufficient() {
        return creditLimit != null && balance != null && 
               creditLimit.compareTo(balance) >= 0;
    }
    
    public boolean isCashCreditLimitSufficient() {
        return cashCreditLimit != null && balance != null && 
               cashCreditLimit.compareTo(balance) >= 0;
    }
    
    public boolean hasDataChanges(Account other) {
        if (other == null) return true;
        
        return !equals(other) ||
               !java.util.Objects.equals(this.limitAmount, other.limitAmount) ||
               !java.util.Objects.equals(this.balance, other.balance) ||
               !java.util.Objects.equals(this.creditLimit, other.creditLimit) ||
               !java.util.Objects.equals(this.cashCreditLimit, other.cashCreditLimit) ||
               !java.util.Objects.equals(this.status, other.status) ||
               !java.util.Objects.equals(this.ficoCreditScore, other.ficoCreditScore) ||
               !java.util.Objects.equals(this.custFicoCreditScore, other.custFicoCreditScore);
    }
    
    public void updateFromRequest(Account source) {
        if (source == null) return;
        
        if (source.getCustomerName() != null) this.customerName = source.getCustomerName();
        if (source.getLimitAmount() != null) this.limitAmount = source.getLimitAmount();
        if (source.getBalance() != null) this.balance = source.getBalance();
        if (source.getCreditLimit() != null) this.creditLimit = source.getCreditLimit();
        if (source.getCashCreditLimit() != null) this.cashCreditLimit = source.getCashCreditLimit();
        if (source.getExpiryDate() != null) this.expiryDate = source.getExpiryDate();
        if (source.getReissueDate() != null) this.reissueDate = source.getReissueDate();
        if (source.getCurrCycleCredit() != null) this.currCycleCredit = source.getCurrCycleCredit();
        if (source.getCurrCycleDebit() != null) this.currCycleDebit = source.getCurrCycleDebit();
        if (source.getGroupId() != null) this.groupId = source.getGroupId();
        if (source.getStatus() != null) this.status = source.getStatus();
        if (source.getFicoCreditScore() != null) this.ficoCreditScore = source.getFicoCreditScore();
        if (source.getCustPhone() != null) this.custPhone = source.getCustPhone();
        if (source.getCustFicoCreditScore() != null) this.custFicoCreditScore = source.getCustFicoCreditScore();
        if (source.getCustFirstName() != null) this.custFirstName = source.getCustFirstName();
        if (source.getCustMiddleName() != null) this.custMiddleName = source.getCustMiddleName();
        if (source.getCustLastName() != null) this.custLastName = source.getCustLastName();
        if (source.getCustAddrLine1() != null) this.custAddrLine1 = source.getCustAddrLine1();
        if (source.getCustAddrLine2() != null) this.custAddrLine2 = source.getCustAddrLine2();
        if (source.getCustAddrLine3() != null) this.custAddrLine3 = source.getCustAddrLine3();
        if (source.getCustAddrStateCd() != null) this.custAddrStateCd = source.getCustAddrStateCd();
        if (source.getCustAddrCountryCd() != null) this.custAddrCountryCd = source.getCustAddrCountryCd();
        if (source.getCustAddrZip() != null) this.custAddrZip = source.getCustAddrZip();
        if (source.getCustPhoneHome() != null) this.custPhoneHome = source.getCustPhoneHome();
        if (source.getCustPhoneWork() != null) this.custPhoneWork = source.getCustPhoneWork();
        if (source.getCustSsn() != null) this.custSsn = source.getCustSsn();
        if (source.getCustGovtIssuedId() != null) this.custGovtIssuedId = source.getCustGovtIssuedId();
        if (source.getCustDobYyyyMmDd() != null) this.custDobYyyyMmDd = source.getCustDobYyyyMmDd();
        if (source.getCardNumber() != null) this.cardNumber = source.getCardNumber();
        if (source.getCardCvvCd() != null) this.cardCvvCd = source.getCardCvvCd();
        if (source.getCardEmbossedName() != null) this.cardEmbossedName = source.getCardEmbossedName();
        if (source.getCardExpiryDate() != null) this.cardExpiryDate = source.getCardExpiryDate();
        if (source.getCardActiveStatus() != null) this.cardActiveStatus = source.getCardActiveStatus();
    }
    
    public boolean isReadyForProcessing() {
        return hasValidMandatoryFields() && 
               isAccountNumberValid() && 
               isCreditLimitSufficient() && 
               isCashCreditLimitSufficient() && 
               isExpiryDateValid();
    }
    
    public String getStatusDisplayName() {
        return switch (status) {
            case "A" -> "Active";
            case "S" -> "Suspended";
            case "C" -> "Closed";
            default -> "Unknown";
        };
    }
    
    public String getCardActiveStatusDisplayName() {
        return switch (cardActiveStatus) {
            case "Y" -> "Active";
            case "N" -> "Inactive";
            default -> "Unknown";
        };
    }
}