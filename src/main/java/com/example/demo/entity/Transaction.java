package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tran_id", nullable = false)
    private Long transactionId;

    @Column(name = "tran_type_cd", nullable = false, length = 2)
    private String transactionTypeCode;

    @Column(name = "tran_cat_cd", nullable = false)
    private Integer transactionCategoryCode;

    @Column(name = "tran_source", nullable = false, length = 10)
    private String transactionSource;

    @Column(name = "tran_desc", nullable = false, length = 50)
    private String description;

    @Column(name = "tran_amt", nullable = false, precision = 11, scale = 2)
    private BigDecimal amount;

    @Column(name = "tran_card_num", nullable = false, length = 16)
    private String cardNumber;

    @Column(name = "tran_merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "tran_merchant_name", nullable = false, length = 50)
    private String merchantName;

    @Column(name = "tran_merchant_city", nullable = false, length = 50)
    private String merchantCity;

    @Column(name = "tran_merchant_zip", nullable = false, length = 10)
    private String merchantZip;

    @Column(name = "tran_orig_ts", nullable = false)
    private LocalDateTime originationTimestamp;

    @Column(name = "tran_proc_ts", nullable = false)
    private LocalDateTime processingTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acct_id", nullable = false)
    private Account account;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Transaction(String transactionTypeCode, Integer transactionCategoryCode, String transactionSource,
                      String description, BigDecimal amount, String cardNumber, Long merchantId,
                      String merchantName, String merchantCity, String merchantZip, Account account) {
        this.transactionTypeCode = transactionTypeCode;
        this.transactionCategoryCode = transactionCategoryCode;
        this.transactionSource = transactionSource;
        this.description = description;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.merchantCity = merchantCity;
        this.merchantZip = merchantZip;
        this.account = account;
        this.originationTimestamp = LocalDateTime.now();
        this.processingTimestamp = LocalDateTime.now();
    }

    /**
     * BR006: Bill Payment Transaction Recording - Creates a bill payment transaction with specific attributes
     * BR004: Full Balance Payment - Payment processes the full current account balance
     */
    public static Transaction createBillPaymentTransaction(BigDecimal paymentAmount, String cardNumber, Account account) {
        Transaction transaction = new Transaction();
        transaction.setTransactionTypeCode("02"); // Bill payment type code
        transaction.setTransactionCategoryCode(2); // Bill payment category
        transaction.setTransactionSource("POS TERM");
        transaction.setDescription("BILL PAYMENT - ONLINE");
        transaction.setAmount(paymentAmount);
        transaction.setCardNumber(cardNumber);
        transaction.setMerchantId(999999999L);
        transaction.setMerchantName("BILL PAYMENT");
        transaction.setMerchantCity("N/A");
        transaction.setMerchantZip("N/A");
        transaction.setOriginationTimestamp(LocalDateTime.now());
        transaction.setProcessingTimestamp(LocalDateTime.now());
        transaction.setAccount(account);
        return transaction;
    }

    /**
     * Check if this transaction is a bill payment transaction
     */
    public boolean isBillPayment() {
        return "02".equals(this.transactionTypeCode) && Integer.valueOf(2).equals(this.transactionCategoryCode);
    }

    /**
     * Set both timestamps to current time
     */
    public void setTimestampsToCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        this.originationTimestamp = currentTime;
        this.processingTimestamp = currentTime;
    }

    /**
     * Apply default values for bill payment transactions
     */
    public void applyBillPaymentDefaults() {
        this.transactionTypeCode = "02";
        this.transactionCategoryCode = 2;
        this.transactionSource = "POS TERM";
        this.description = "BILL PAYMENT - ONLINE";
        this.merchantId = 999999999L;
        this.merchantName = "BILL PAYMENT";
        this.merchantCity = "N/A";
        this.merchantZip = "N/A";
        setTimestampsToCurrentTime();
    }

    /**
     * BR004: Check if this is a full balance payment
     */
    public boolean isFullBalancePayment(BigDecimal accountBalance) {
        return this.amount != null && accountBalance != null && 
               this.amount.compareTo(accountBalance) == 0;
    }

    @PrePersist
    protected void onCreate() {
        if (this.originationTimestamp == null) {
            this.originationTimestamp = LocalDateTime.now();
        }
        if (this.processingTimestamp == null) {
            this.processingTimestamp = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.processingTimestamp = LocalDateTime.now();
    }
}
