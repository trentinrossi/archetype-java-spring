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
    @Column(name = "tran_id", nullable = false, length = 16)
    private String tranId;
    
    @Column(name = "tran_type_cd", nullable = false, length = 2)
    private String tranTypeCd;
    
    @Column(name = "tran_cat_cd", nullable = false, length = 2)
    private String tranCatCd;
    
    @Column(name = "tran_source", nullable = false)
    private String tranSource;
    
    @Column(name = "tran_desc", nullable = false)
    private String tranDesc;
    
    @Column(name = "tran_amt", nullable = false, precision = 12, scale = 2)
    private BigDecimal tranAmt;
    
    @Column(name = "tran_card_num", nullable = false, length = 16)
    private String tranCardNum;
    
    @Column(name = "tran_merchant_id", nullable = false)
    private Long tranMerchantId;
    
    @Column(name = "tran_merchant_name")
    private String tranMerchantName;
    
    @Column(name = "tran_merchant_city")
    private String tranMerchantCity;
    
    @Column(name = "tran_merchant_zip")
    private String tranMerchantZip;
    
    @Column(name = "tran_orig_ts", nullable = false, length = 26)
    private String tranOrigTs;
    
    @Column(name = "tran_proc_ts", nullable = false, length = 26)
    private String tranProcTs;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
