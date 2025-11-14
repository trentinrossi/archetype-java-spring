-- V3__Create_transactions_table.sql
-- Migration for Transaction entity - Bill Payment Processing

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    tran_id BIGINT NOT NULL UNIQUE,
    tran_type_cd VARCHAR(2) NOT NULL,
    tran_cat_cd INTEGER NOT NULL,
    tran_source VARCHAR(8) NOT NULL,
    tran_desc VARCHAR(50) NOT NULL,
    tran_amt DECIMAL(11, 2) NOT NULL,
    tran_card_num VARCHAR(16) NOT NULL,
    tran_merchant_id BIGINT NOT NULL,
    tran_merchant_name VARCHAR(50) NOT NULL,
    tran_merchant_city VARCHAR(50) NOT NULL,
    tran_merchant_zip VARCHAR(10) NOT NULL,
    tran_orig_ts TIMESTAMP NOT NULL,
    tran_proc_ts TIMESTAMP NOT NULL,
    tran_acct_id VARCHAR(11) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_tran_id ON transactions(tran_id);
CREATE INDEX idx_tran_card_num ON transactions(tran_card_num);
CREATE INDEX idx_tran_type_cd ON transactions(tran_type_cd);
CREATE INDEX idx_tran_orig_ts ON transactions(tran_orig_ts);
CREATE INDEX idx_tran_acct_id ON transactions(tran_acct_id);

ALTER TABLE transactions ADD CONSTRAINT fk_transaction_account FOREIGN KEY (tran_acct_id) REFERENCES accounts(acct_id);
