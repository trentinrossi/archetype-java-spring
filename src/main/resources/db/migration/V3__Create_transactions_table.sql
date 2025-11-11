-- V3__Create_transactions_table.sql
-- Create transactions table for Bill Payment system
-- BR005: Transaction ID Generation - Auto-incrementing transaction ID
-- BR006: Bill Payment Transaction Recording - Records bill payment with specific transaction attributes

CREATE TABLE transactions (
    tran_id BIGSERIAL PRIMARY KEY,
    tran_type_cd VARCHAR(2) NOT NULL,
    tran_cat_cd INTEGER NOT NULL,
    tran_source VARCHAR(10) NOT NULL,
    tran_desc VARCHAR(50) NOT NULL,
    tran_amt DECIMAL(11, 2) NOT NULL,
    tran_card_num VARCHAR(16) NOT NULL,
    tran_merchant_id BIGINT NOT NULL,
    tran_merchant_name VARCHAR(50) NOT NULL,
    tran_merchant_city VARCHAR(50) NOT NULL,
    tran_merchant_zip VARCHAR(10) NOT NULL,
    tran_orig_ts TIMESTAMP NOT NULL,
    tran_proc_ts TIMESTAMP NOT NULL,
    acct_id VARCHAR(11) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_account FOREIGN KEY (acct_id) 
        REFERENCES accounts(acct_id) ON DELETE CASCADE
);

-- Create indexes for performance
CREATE INDEX idx_transactions_account ON transactions(acct_id);
CREATE INDEX idx_transactions_card ON transactions(tran_card_num);
CREATE INDEX idx_transactions_type ON transactions(tran_type_cd);
CREATE INDEX idx_transactions_category ON transactions(tran_cat_cd);
CREATE INDEX idx_transactions_type_cat ON transactions(tran_type_cd, tran_cat_cd);
CREATE INDEX idx_transactions_merchant ON transactions(tran_merchant_id);
CREATE INDEX idx_transactions_orig_ts ON transactions(tran_orig_ts);
CREATE INDEX idx_transactions_proc_ts ON transactions(tran_proc_ts);
CREATE INDEX idx_transactions_amount ON transactions(tran_amt);

-- Add comments for documentation
COMMENT ON TABLE transactions IS 'Bill payment transaction records';
COMMENT ON COLUMN transactions.tran_id IS 'Unique transaction identifier (auto-generated)';
COMMENT ON COLUMN transactions.tran_type_cd IS 'Transaction type code (02 for bill payment)';
COMMENT ON COLUMN transactions.tran_cat_cd IS 'Transaction category code (2 for bill payment)';
COMMENT ON COLUMN transactions.tran_source IS 'Source of transaction (POS TERM)';
COMMENT ON COLUMN transactions.tran_desc IS 'Transaction description';
COMMENT ON COLUMN transactions.tran_amt IS 'Transaction amount with 2 decimal places';
COMMENT ON COLUMN transactions.tran_card_num IS 'Card number used for transaction';
COMMENT ON COLUMN transactions.tran_merchant_id IS 'Merchant identifier';
COMMENT ON COLUMN transactions.tran_merchant_name IS 'Merchant name';
COMMENT ON COLUMN transactions.tran_merchant_city IS 'Merchant city';
COMMENT ON COLUMN transactions.tran_merchant_zip IS 'Merchant ZIP code';
COMMENT ON COLUMN transactions.tran_orig_ts IS 'Transaction origination timestamp';
COMMENT ON COLUMN transactions.tran_proc_ts IS 'Transaction processing timestamp';
COMMENT ON COLUMN transactions.acct_id IS 'Account ID for the transaction';
COMMENT ON COLUMN transactions.created_at IS 'Timestamp when the transaction was created';
COMMENT ON COLUMN transactions.updated_at IS 'Timestamp when the transaction was last updated';
