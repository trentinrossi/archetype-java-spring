-- V1__Create_accounts_table.sql
-- Create accounts table for customer account management
-- Business Rules: BR001, BR004

CREATE TABLE accounts (
    account_id VARCHAR(11) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id),
    CONSTRAINT chk_account_id_format CHECK (account_id ~ '^[0-9]{11}$'),
    CONSTRAINT chk_account_id_not_zeros CHECK (account_id !~ '^0+$')
);

-- Indexes for performance
CREATE INDEX idx_accounts_created_at ON accounts(created_at);
CREATE INDEX idx_accounts_updated_at ON accounts(updated_at);

-- Comments
COMMENT ON TABLE accounts IS 'Customer accounts that own credit cards';
COMMENT ON COLUMN accounts.account_id IS 'Unique 11-digit numeric account identifier (BR004)';
COMMENT ON COLUMN accounts.created_at IS 'Timestamp when the account was created';
COMMENT ON COLUMN accounts.updated_at IS 'Timestamp when the account was last updated';
