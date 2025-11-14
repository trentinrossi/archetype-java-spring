-- V2__Create_accounts_table.sql
-- Migration script for creating accounts table
-- Supports H2 and PostgreSQL syntax

CREATE TABLE accounts (
    account_id BIGINT NOT NULL,
    active_status VARCHAR(1) NOT NULL,
    current_balance DECIMAL(12, 2) NOT NULL,
    credit_limit DECIMAL(12, 2) NOT NULL,
    cash_credit_limit DECIMAL(12, 2) NOT NULL,
    open_date DATE NOT NULL,
    expiration_date DATE NOT NULL,
    reissue_date DATE NOT NULL,
    current_cycle_credit DECIMAL(12, 2) NOT NULL,
    current_cycle_debit DECIMAL(12, 2) NOT NULL,
    group_id VARCHAR(10),
    customer_id BIGINT NOT NULL,
    account_status VARCHAR(1) NOT NULL,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id)
);

-- Foreign key constraint
ALTER TABLE accounts ADD CONSTRAINT fk_accounts_customer 
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id);

-- Indexes for performance optimization
CREATE INDEX idx_accounts_customer_id ON accounts(customer_id);
CREATE INDEX idx_accounts_active_status ON accounts(active_status);
CREATE INDEX idx_accounts_account_status ON accounts(account_status);
CREATE INDEX idx_accounts_group_id ON accounts(group_id);
CREATE INDEX idx_accounts_open_date ON accounts(open_date);
CREATE INDEX idx_accounts_expiration_date ON accounts(expiration_date);
CREATE INDEX idx_accounts_current_balance ON accounts(current_balance);
CREATE INDEX idx_accounts_credit_limit ON accounts(credit_limit);

-- Composite indexes for common queries
CREATE INDEX idx_accounts_customer_active ON accounts(customer_id, active_status);
CREATE INDEX idx_accounts_customer_status ON accounts(customer_id, account_status);
CREATE INDEX idx_accounts_group_active ON accounts(group_id, active_status);

-- Comments for documentation
COMMENT ON TABLE accounts IS 'Credit card account information';
COMMENT ON COLUMN accounts.account_id IS 'Account Identification Number - Primary key (11 digits)';
COMMENT ON COLUMN accounts.active_status IS 'Account Active Status (Y/N)';
COMMENT ON COLUMN accounts.current_balance IS 'Current Balance with 2 decimal places';
COMMENT ON COLUMN accounts.credit_limit IS 'Credit Limit with 2 decimal places';
COMMENT ON COLUMN accounts.cash_credit_limit IS 'Cash Credit Limit with 2 decimal places';
COMMENT ON COLUMN accounts.open_date IS 'Account Opening Date in YYYY-MM-DD format';
COMMENT ON COLUMN accounts.expiration_date IS 'Account Expiration Date in YYYY-MM-DD format';
COMMENT ON COLUMN accounts.reissue_date IS 'Account Reissue Date in YYYY-MM-DD format';
COMMENT ON COLUMN accounts.current_cycle_credit IS 'Current Cycle Credit with 2 decimal places';
COMMENT ON COLUMN accounts.current_cycle_debit IS 'Current Cycle Debit with 2 decimal places';
COMMENT ON COLUMN accounts.group_id IS 'Account Group ID for organizational purposes';
COMMENT ON COLUMN accounts.customer_id IS 'Customer Identification Number - Foreign key';
COMMENT ON COLUMN accounts.account_status IS 'Current status of the account';
COMMENT ON COLUMN accounts.version IS 'Version number for optimistic locking';
