-- Create accounts table
-- Account ID must be exactly 11 digits numeric
CREATE TABLE accounts (
    account_id VARCHAR(11) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_account_id_format CHECK (account_id ~ '^[0-9]{11}$')
);

-- Create index for faster lookups
CREATE INDEX idx_accounts_created_at ON accounts(created_at);

-- Add comment to table
COMMENT ON TABLE accounts IS 'Customer accounts that can have multiple credit cards associated';
COMMENT ON COLUMN accounts.account_id IS 'Unique 11-digit account identifier';
