-- V7__Create_transactions_table.sql

CREATE TABLE transactions (
    tran_id VARCHAR(16) NOT NULL,
    tran_type_cd VARCHAR(2) NOT NULL,
    tran_cat_cd VARCHAR(2) NOT NULL,
    tran_source VARCHAR(50) NOT NULL,
    tran_desc VARCHAR(255) NOT NULL,
    tran_amt DECIMAL(12, 2) NOT NULL,
    tran_card_num VARCHAR(16) NOT NULL,
    tran_merchant_id BIGINT NOT NULL,
    tran_merchant_name VARCHAR(100),
    tran_merchant_city VARCHAR(50),
    tran_merchant_zip VARCHAR(10),
    tran_orig_ts VARCHAR(26) NOT NULL,
    tran_proc_ts VARCHAR(26) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (tran_id)
);

CREATE INDEX idx_transactions_card_num ON transactions(tran_card_num);
CREATE INDEX idx_transactions_type_cd ON transactions(tran_type_cd);
CREATE INDEX idx_transactions_cat_cd ON transactions(tran_cat_cd);
