-- V1__Create_accounts_table.sql
-- Create accounts table for bill payment system
-- Stores customer account information including balance and identification

CREATE TABLE accounts (
    acct_id VARCHAR(11) PRIMARY KEY,
    acct_curr_bal DECIMAL(13, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add comment to table
COMMENT ON TABLE accounts IS 'Customer account information including balance and identification';

-- Add comments to columns
COMMENT ON COLUMN accounts.acct_id IS 'Unique account identifier (11 characters)';
COMMENT ON COLUMN accounts.acct_curr_bal IS 'Current account balance with 2 decimal places';
COMMENT ON COLUMN accounts.created_at IS 'Account creation timestamp';
COMMENT ON COLUMN accounts.updated_at IS 'Account last update timestamp';

-- Create index on balance for performance
CREATE INDEX idx_accounts_balance ON accounts(acct_curr_bal);

-- Create index on created_at for reporting
CREATE INDEX idx_accounts_created_at ON accounts(created_at);
