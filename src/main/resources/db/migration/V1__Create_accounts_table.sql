-- V1__Create_accounts_table.sql
-- Create accounts table for credit card account management
-- Account ID is an 11-digit numeric value that serves as the primary key

CREATE TABLE accounts (
    account_id BIGINT PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add comment to table
COMMENT ON TABLE accounts IS 'Customer accounts that can have multiple credit cards';

-- Add comments to columns
COMMENT ON COLUMN accounts.account_id IS '11-digit unique account identifier';
COMMENT ON COLUMN accounts.created_at IS 'Timestamp when the account was created';
COMMENT ON COLUMN accounts.updated_at IS 'Timestamp when the account was last updated';

-- Create index for faster lookups
CREATE INDEX idx_accounts_account_id ON accounts(account_id);
