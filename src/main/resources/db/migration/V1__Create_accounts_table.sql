-- V1__Create_accounts_table.sql
-- Create accounts table for Bill Payment system
-- BR001: Account Validation - Stores account information
-- BR002: Balance Check - Stores current balance
-- BR007: Account Balance Update - Balance is updated after payment

CREATE TABLE accounts (
    acct_id VARCHAR(11) PRIMARY KEY,
    acct_curr_bal DECIMAL(13, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index on balance for performance
CREATE INDEX idx_accounts_balance ON accounts(acct_curr_bal);

-- Add comments for documentation
COMMENT ON TABLE accounts IS 'Customer account information including balance and identification';
COMMENT ON COLUMN accounts.acct_id IS 'Unique account identifier (11 characters)';
COMMENT ON COLUMN accounts.acct_curr_bal IS 'Current account balance with 2 decimal places';
COMMENT ON COLUMN accounts.created_at IS 'Timestamp when the account was created';
COMMENT ON COLUMN accounts.updated_at IS 'Timestamp when the account was last updated';
