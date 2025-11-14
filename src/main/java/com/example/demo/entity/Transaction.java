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
@Table(name = "transactions", indexes = {
    @Index(name = "idx_tran_id", columnList = "tran_id", unique = true),
    @Index(name = "idx_tran_card_num", columnList = "tran_card_num"),
    @Index(name = "idx_tran_type_cd", columnList = "tran_type_cd"),
    @Index(name = "idx_tran_orig_ts", columnList = "tran_orig_ts"),
    @Index(name = "idx_tran_acct_id", columnList = "tran_acct_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tran_id", nullable = false, unique = true)
    private Long tranId;

    @Column(name = "tran_type_cd", nullable = false, length = 2)
    private String tranTypeCd;

    @Column(name = "tran_cat_cd", nullable = false)
    private Integer tranCatCd;

    @Column(name = "tran_source", nullable = false, length = 8)
    private String tranSource;

    @Column(name = "tran_desc", nullable = false, length = 50)
    private String tranDesc;

    @Column(name = "tran_amt", nullable = false, precision = 11, scale = 2)
    private BigDecimal tranAmt;

    @Column(name = "tran_card_num", nullable = false, length = 16)
    private String tranCardNum;

    @Column(name = "tran_merchant_id", nullable = false)
    private Long tranMerchantId;

    @Column(name = "tran_merchant_name", nullable = false, length = 50)
    private String tranMerchantName;

    @Column(name = "tran_merchant_city", nullable = false, length = 50)
    private String tranMerchantCity;

    @Column(name = "tran_merchant_zip", nullable = false, length = 10)
    private String tranMerchantZip;

    @Column(name = "tran_orig_ts", nullable = false)
    private LocalDateTime tranOrigTs;

    @Column(name = "tran_proc_ts", nullable = false)
    private LocalDateTime tranProcTs;

    @Column(name = "tran_acct_id", nullable = false, length = 11)
    private String tranAcctId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tran_acct_id", referencedColumnName = "acct_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_transaction_account"))
    private Account account;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Transaction(Long tranId, String tranTypeCd, Integer tranCatCd, String tranSource, 
                      String tranDesc, BigDecimal tranAmt, String tranCardNum, 
                      Long tranMerchantId, String tranMerchantName, String tranMerchantCity, 
                      String tranMerchantZip, LocalDateTime tranOrigTs, LocalDateTime tranProcTs, 
                      String tranAcctId) {
        this.tranId = tranId;
        this.tranTypeCd = tranTypeCd;
        this.tranCatCd = tranCatCd;
        this.tranSource = tranSource;
        this.tranDesc = tranDesc;
        this.tranAmt = tranAmt;
        this.tranCardNum = tranCardNum;
        this.tranMerchantId = tranMerchantId;
        this.tranMerchantName = tranMerchantName;
        this.tranMerchantCity = tranMerchantCity;
        this.tranMerchantZip = tranMerchantZip;
        this.tranOrigTs = tranOrigTs;
        this.tranProcTs = tranProcTs;
        this.tranAcctId = tranAcctId;
    }

    public static Transaction createBillPaymentTransaction(Long tranId, BigDecimal paymentAmount, 
                                                           String cardNumber, String accountId) {
        LocalDateTime currentTimestamp = LocalDateTime.now();
        
        return new Transaction(
            tranId,
            "02",
            2,
            "POS TERM",
            "BILL PAYMENT - ONLINE",
            paymentAmount,
            cardNumber,
            999999999L,
            "BILL PAYMENT",
            "N/A",
            "N/A",
            currentTimestamp,
            currentTimestamp,
            accountId
        );
    }

    public boolean isBillPaymentTransaction() {
        return "02".equals(this.tranTypeCd) && 
               this.tranCatCd != null && 
               this.tranCatCd == 2 &&
               "BILL PAYMENT - ONLINE".equals(this.tranDesc);
    }

    public String getFormattedTransactionAmount() {
        if (this.tranAmt != null) {
            return String.format("$%,.2f", this.tranAmt);
        }
        return "$0.00";
    }

    public String getTransactionSummary() {
        return String.format("Transaction ID: %d, Type: %s, Amount: %s, Merchant: %s, Date: %s",
            this.tranId,
            this.tranTypeCd,
            getFormattedTransactionAmount(),
            this.tranMerchantName,
            this.tranOrigTs != null ? this.tranOrigTs.toString() : "N/A"
        );
    }

    @PrePersist
    protected void onCreate() {
        if (this.tranOrigTs == null) {
            this.tranOrigTs = LocalDateTime.now();
        }
        if (this.tranProcTs == null) {
            this.tranProcTs = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.tranProcTs = LocalDateTime.now();
    }
}
