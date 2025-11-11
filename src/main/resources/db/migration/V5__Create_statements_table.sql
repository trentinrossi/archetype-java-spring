-- V5__Create_statements_table.sql
-- Create statements table for card account transaction management system

CREATE TABLE statements (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(75) NOT NULL,
    customer_address VARCHAR(150) NOT NULL,
    account_id BIGINT NOT NULL,
    current_balance DECIMAL(12, 2) NOT NULL,
    fico_score INTEGER NOT NULL,
    total_transaction_amount DECIMAL(12, 2) NOT NULL,
    customer_id BIGINT NOT NULL,
    account_reference_id BIGINT NOT NULL,
    statement_period_start TIMESTAMP,
    statement_period_end TIMESTAMP,
    plain_text_content TEXT,
    html_content TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'GENERATED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_statements_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT fk_statements_account FOREIGN KEY (account_reference_id) REFERENCES accounts(account_id),
    CONSTRAINT chk_statement_account_id CHECK (account_id >= 10000000000 AND account_id <= 99999999999),
    CONSTRAINT chk_statement_fico_score CHECK (fico_score >= 0 AND fico_score <= 999)
);

CREATE INDEX idx_statements_account_id ON statements(account_id);
CREATE INDEX idx_statements_customer_id ON statements(customer_id);
CREATE INDEX idx_statements_status ON statements(status);
CREATE INDEX idx_statements_created_at ON statements(created_at);
CREATE INDEX idx_statements_period ON statements(statement_period_start, statement_period_end);
