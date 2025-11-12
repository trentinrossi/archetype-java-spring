-- V4__Create_transaction_category_balances_table.sql

CREATE TABLE transaction_category_balances (
    id BIGSERIAL PRIMARY KEY,
    trancat_acct_id BIGINT NOT NULL,
    trancat_type_cd VARCHAR(2) NOT NULL,
    trancat_cd VARCHAR(4) NOT NULL,
    tran_cat_bal DECIMAL(12, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transaction_category_balances_acct_id ON transaction_category_balances(trancat_acct_id);
CREATE INDEX idx_transaction_category_balances_type_cd ON transaction_category_balances(trancat_type_cd);
CREATE INDEX idx_transaction_category_balances_cd ON transaction_category_balances(trancat_cd);
