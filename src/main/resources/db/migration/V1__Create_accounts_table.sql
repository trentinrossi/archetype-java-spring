-- V1__Create_accounts_table.sql
-- Migration for Account entity - Bill Payment Processing

CREATE TABLE accounts (
    acct_id VARCHAR(11) NOT NULL,
    acct_curr_bal DECIMAL(13, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (acct_id)
);

CREATE UNIQUE INDEX idx_acct_id ON accounts(acct_id);
