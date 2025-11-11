package com.carddemo.billpayment.entity;

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
    private Long tranId;

    @Column(name = "tran_type_cd", nullable = false, length = 2)
    private String tranTypeCd;

    @Column(name = "tran_cat_cd", nullable = false)
    private Integer tranCatCd;

    @Column(name = "tran_source", nullable = false, length = 10)
    private String tranSource;

    @Column(name = "tran_desc", nullable = false, length = 50)
    private String tranDesc;

    @Column(name = "tran_amt", nullable = false, precision = 11, scale = 2)
    private BigDecimal tranAmt;

    @Column(name = "tran_card_num", nullable = false, length = 16)
    private String tranCardNum;

    @Column(name = "tran_merchant_id", nullable = false)
    private Integer tranMerchantId;

    @Column(name = "tran_merchant_name", nullable = false, length = 50)
    private String tranMerchantName;

    @Column(name = "tran_merchant_city", nullable = false, length = 50)
    private String tranMerchantCity;

    @Column(name = "tran_merchant_zip", nullable = false, length = 10)
    private String tranMerchantZip;

    @Column(name = "tran_orig_ts", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime tranOrigTs;

    @Column(name = "tran_proc_ts", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime tranProcTs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Transaction(String tranTypeCd, Integer tranCatCd, String tranSource, String tranDesc, 
                      BigDecimal tranAmt, String tranCardNum, Integer tranMerchantId, 
                      String tranMerchantName, String tranMerchantCity, String tranMerchantZip, 
                      LocalDateTime tranOrigTs, LocalDateTime tranProcTs, Account account) {
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
        this.account = account;
    }

    public static Transaction createBillPaymentTransaction(BigDecimal paymentAmount, String cardNumber, Account account) {
        LocalDateTime currentTimestamp = LocalDateTime.now();
        
        Transaction transaction = new Transaction();
        transaction.setTranTypeCd("02");
        transaction.setTranCatCd(2);
        transaction.setTranSource("POS TERM");
        transaction.setTranDesc("BILL PAYMENT - ONLINE");
        transaction.setTranAmt(paymentAmount);
        transaction.setTranCardNum(cardNumber);
        transaction.setTranMerchantId(999999999);
        transaction.setTranMerchantName("BILL PAYMENT");
        transaction.setTranMerchantCity("N/A");
        transaction.setTranMerchantZip("N/A");
        transaction.setTranOrigTs(currentTimestamp);
        transaction.setTranProcTs(currentTimestamp);
        transaction.setAccount(account);
        
        return transaction;
    }

    public boolean isBillPayment() {
        return "02".equals(this.tranTypeCd) && Integer.valueOf(2).equals(this.tranCatCd);
    }

    public boolean isFullBalancePayment(BigDecimal accountBalance) {
        return this.tranAmt != null && this.tranAmt.compareTo(accountBalance) == 0;
    }

    public String getFormattedAmount() {
        return this.tranAmt != null ? String.format("$%.2f", this.tranAmt) : "$0.00";
    }

    public String getTransactionSummary() {
        return String.format("Transaction ID: %d, Type: %s, Amount: %s, Date: %s", 
                           this.tranId, this.tranDesc, getFormattedAmount(), 
                           this.tranOrigTs != null ? this.tranOrigTs.toString() : "N/A");
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
