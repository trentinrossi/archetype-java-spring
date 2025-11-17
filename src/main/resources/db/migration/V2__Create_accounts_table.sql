-- V2__Create_accounts_table.sql
-- Account table for customer accounts

CREATE TABLE accounts (
    acct_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (acct_id)
);

CREATE INDEX idx_acct_id ON accounts(acct_id);
