-- V2__Create_accounts_table.sql
-- Create accounts table for card account transaction management system

CREATE TABLE accounts (
    account_id BIGINT PRIMARY KEY,
    current_balance DECIMAL(12, 2) NOT NULL,
    credit_limit DECIMAL(12, 2) NOT NULL,
    customer_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_accounts_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT chk_account_id CHECK (account_id >= 10000000000 AND account_id <= 99999999999)
);

CREATE INDEX idx_accounts_account_id ON accounts(account_id);
CREATE INDEX idx_accounts_customer_id ON accounts(customer_id);
CREATE INDEX idx_accounts_current_balance ON accounts(current_balance);
