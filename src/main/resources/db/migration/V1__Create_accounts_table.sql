-- Migration: Create accounts table
-- Business Rule: Account serves as the primary container for credit card management
-- Account ID must be exactly 11 numeric digits

CREATE TABLE accounts (
    account_id VARCHAR(11) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_account_id_format CHECK (account_id ~ '^\d{11}$')
);

-- Create index for performance
CREATE INDEX idx_account_id ON accounts(account_id);

-- Add comment to table
COMMENT ON TABLE accounts IS 'Customer accounts that own one or more credit cards';
COMMENT ON COLUMN accounts.account_id IS 'Unique 11-digit numeric account identifier';
COMMENT ON COLUMN accounts.created_at IS 'Timestamp when the account was created';
COMMENT ON COLUMN accounts.updated_at IS 'Timestamp when the account was last updated';
