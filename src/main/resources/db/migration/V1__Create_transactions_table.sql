-- V1__Create_transactions_table.sql
-- Transaction table for credit card transactions

CREATE TABLE transactions (
    transaction_id VARCHAR(16) NOT NULL,
    tran_id BIGINT NOT NULL,
    tran_type_cd INTEGER NOT NULL,
    tran_cat_cd INTEGER NOT NULL,
    tran_source VARCHAR(10) NOT NULL,
    tran_desc VARCHAR(60) NOT NULL,
    tran_amt DECIMAL(11, 2) NOT NULL,
    tran_card_num VARCHAR(16) NOT NULL,
    tran_merchant_id VARCHAR(9) NOT NULL,
    tran_merchant_name VARCHAR(30) NOT NULL,
    tran_merchant_city VARCHAR(25) NOT NULL,
    tran_merchant_zip VARCHAR(10) NOT NULL,
    tran_orig_ts DATE NOT NULL,
    tran_proc_ts DATE NOT NULL,
    card_id VARCHAR(16),
    account_id BIGINT,
    merchant_id VARCHAR(9),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (transaction_id)
);

CREATE UNIQUE INDEX uk_tran_id ON transactions(tran_id);
CREATE INDEX idx_tran_card_num ON transactions(tran_card_num);
CREATE INDEX idx_tran_merchant_id ON transactions(tran_merchant_id);
CREATE INDEX idx_tran_orig_ts ON transactions(tran_orig_ts);
CREATE INDEX idx_tran_proc_ts ON transactions(tran_proc_ts);
CREATE INDEX idx_tran_type_cd ON transactions(tran_type_cd);
CREATE INDEX idx_tran_cat_cd ON transactions(tran_cat_cd);
