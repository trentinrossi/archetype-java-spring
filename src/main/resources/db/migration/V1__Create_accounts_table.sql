-- V1__Create_accounts_table.sql
CREATE TABLE accounts (
    account_id BIGINT PRIMARY KEY,
    acct_id BIGINT NOT NULL,
    xref_acct_id BIGINT NOT NULL,
    active_status VARCHAR(1) NOT NULL,
    acct_active_status VARCHAR(1) NOT NULL,
    account_status VARCHAR(1) NOT NULL,
    current_balance DECIMAL(12, 2) NOT NULL,
    acct_curr_bal DECIMAL(12, 2) NOT NULL,
    credit_limit DECIMAL(12, 2) NOT NULL,
    acct_credit_limit DECIMAL(12, 2) NOT NULL,
    cash_credit_limit DECIMAL(12, 2) NOT NULL,
    acct_cash_credit_limit DECIMAL(12, 2) NOT NULL,
    open_date VARCHAR(8) NOT NULL,
    acct_open_date VARCHAR(8) NOT NULL,
    expiration_date VARCHAR(8) NOT NULL,
    acct_expiraion_date VARCHAR(8) NOT NULL,
    reissue_date VARCHAR(8) NOT NULL,
    acct_reissue_date VARCHAR(8),
    current_cycle_credit DECIMAL(12, 2) NOT NULL,
    acct_curr_cyc_credit DECIMAL(12, 2) NOT NULL,
    current_cycle_debit DECIMAL(12, 2) NOT NULL,
    acct_curr_cyc_debit DECIMAL(12, 2) NOT NULL,
    group_id VARCHAR(10),
    acct_group_id VARCHAR(10),
    account_data VARCHAR(289) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_accounts_account_id ON accounts(account_id);
CREATE INDEX idx_accounts_xref_acct_id ON accounts(xref_acct_id);
CREATE INDEX idx_accounts_active_status ON accounts(active_status);
CREATE INDEX idx_accounts_group_id ON accounts(group_id);
CREATE INDEX idx_accounts_open_date ON accounts(open_date);
CREATE INDEX idx_accounts_expiration_date ON accounts(expiration_date);
