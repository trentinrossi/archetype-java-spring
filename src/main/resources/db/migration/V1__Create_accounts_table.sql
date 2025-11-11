-- V1__Create_accounts_table.sql
-- Migration script for Account entity
-- Creates the accounts table with all required fields and constraints

CREATE TABLE accounts (
    acct_id VARCHAR(11) NOT NULL,
    acct_active_status VARCHAR(1) NOT NULL,
    acct_curr_bal DECIMAL(15, 2) NOT NULL,
    acct_credit_limit DECIMAL(15, 2) NOT NULL,
    acct_cash_credit_limit DECIMAL(15, 2) NOT NULL,
    acct_open_date DATE NOT NULL,
    acct_expiration_date DATE NOT NULL,
    acct_reissue_date DATE,
    acct_curr_cyc_credit DECIMAL(15, 2) NOT NULL,
    acct_curr_cyc_debit DECIMAL(15, 2) NOT NULL,
    acct_group_id VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (acct_id),
    CONSTRAINT chk_acct_active_status CHECK (acct_active_status IN ('A', 'I')),
    CONSTRAINT chk_acct_id_format CHECK (acct_id ~ '^[0-9]{11}$')
);

-- Create indexes for frequently queried columns
CREATE INDEX idx_accounts_acct_group_id ON accounts(acct_group_id);
CREATE INDEX idx_accounts_acct_active_status ON accounts(acct_active_status);
CREATE INDEX idx_accounts_acct_open_date ON accounts(acct_open_date);
CREATE INDEX idx_accounts_acct_expiration_date ON accounts(acct_expiration_date);

-- Add comments for documentation
COMMENT ON TABLE accounts IS 'Account data table - stores customer account information with financial and status details';
COMMENT ON COLUMN accounts.acct_id IS 'Unique 11-digit account identifier used as the record key';
COMMENT ON COLUMN accounts.acct_active_status IS 'Account active status: A for active, I for inactive';
COMMENT ON COLUMN accounts.acct_curr_bal IS 'Current balance of the account in decimal format';
COMMENT ON COLUMN accounts.acct_credit_limit IS 'Maximum credit limit for the account';
COMMENT ON COLUMN accounts.acct_cash_credit_limit IS 'Maximum cash credit limit for the account';
COMMENT ON COLUMN accounts.acct_open_date IS 'Date when the account was opened';
COMMENT ON COLUMN accounts.acct_expiration_date IS 'Date when the account expires';
COMMENT ON COLUMN accounts.acct_reissue_date IS 'Date when the account was reissued (optional)';
COMMENT ON COLUMN accounts.acct_curr_cyc_credit IS 'Current cycle credit amount';
COMMENT ON COLUMN accounts.acct_curr_cyc_debit IS 'Current cycle debit amount';
COMMENT ON COLUMN accounts.acct_group_id IS 'Account group identifier for categorization (optional)';
COMMENT ON COLUMN accounts.created_at IS 'Timestamp when the record was created';
COMMENT ON COLUMN accounts.updated_at IS 'Timestamp when the record was last updated';
