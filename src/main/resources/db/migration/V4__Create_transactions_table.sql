-- V4__Create_transactions_table.sql
-- Create transactions table for card account transaction management system

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(16) UNIQUE NOT NULL,
    transaction_type_code VARCHAR(2) NOT NULL,
    transaction_category_code VARCHAR(4) NOT NULL,
    transaction_source VARCHAR(10) NOT NULL,
    transaction_description VARCHAR(100) NOT NULL,
    transaction_amount DECIMAL(11, 2) NOT NULL,
    card_number VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transactions_card FOREIGN KEY (card_number) REFERENCES cards(card_number),
    CONSTRAINT chk_transaction_id CHECK (LENGTH(transaction_id) = 16)
);

CREATE INDEX idx_transactions_transaction_id ON transactions(transaction_id);
CREATE INDEX idx_transactions_card_number ON transactions(card_number);
CREATE INDEX idx_transactions_type_code ON transactions(transaction_type_code);
CREATE INDEX idx_transactions_category_code ON transactions(transaction_category_code);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);
