-- V1__Create_accounts_table.sql
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL UNIQUE,
    account_data VARCHAR(289) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_accounts_account_id ON accounts(account_id);

COMMENT ON TABLE accounts IS 'Account master data containing account balances, credit limits, dates, and status information';
COMMENT ON COLUMN accounts.account_id IS 'Primary key - unique account identification number (11 numeric digits)';
COMMENT ON COLUMN accounts.account_data IS 'Account balance, credit limit, dates, and status information with proper decimal handling';