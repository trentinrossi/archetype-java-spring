-- V1__Create_accounts_table.sql

CREATE TABLE accounts (
    account_id BIGINT NOT NULL,
    acct_id BIGINT NOT NULL,
    acct_active_status VARCHAR(1) NOT NULL,
    acct_curr_bal DECIMAL(12, 2) NOT NULL,
    acct_credit_limit DECIMAL(12, 2) NOT NULL,
    acct_cash_credit_limit DECIMAL(12, 2) NOT NULL,
    acct_open_date DATE NOT NULL,
    acct_expiraion_date DATE NOT NULL,
    acct_reissue_date DATE,
    acct_curr_cyc_credit DECIMAL(12, 2) NOT NULL,
    acct_curr_cyc_debit DECIMAL(12, 2) NOT NULL,
    acct_group_id VARCHAR(10),
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
    account_status VARCHAR(1) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id)
);

CREATE INDEX idx_accounts_acct_id ON accounts(acct_id);
CREATE INDEX idx_accounts_acct_group_id ON accounts(acct_group_id);
CREATE INDEX idx_accounts_group_id ON accounts(group_id);
CREATE INDEX idx_accounts_account_status ON accounts(account_status);
CREATE INDEX idx_accounts_active_status ON accounts(active_status);
