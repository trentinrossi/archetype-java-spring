-- V4__Create_cards_table.sql
-- Migration script for creating cards table
-- Supports H2 and PostgreSQL syntax

CREATE TABLE cards (
    card_number VARCHAR(16) NOT NULL,
    account_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (card_number)
);

-- Foreign key constraints
ALTER TABLE cards ADD CONSTRAINT fk_cards_account 
    FOREIGN KEY (account_id) REFERENCES accounts(account_id);

ALTER TABLE cards ADD CONSTRAINT fk_cards_customer 
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id);

-- Indexes for performance optimization
CREATE INDEX idx_cards_account_id ON cards(account_id);
CREATE INDEX idx_cards_customer_id ON cards(customer_id);

-- Composite indexes for common queries
CREATE INDEX idx_cards_account_customer ON cards(account_id, customer_id);

-- Comments for documentation
COMMENT ON TABLE cards IS 'Represents credit cards associated with accounts';
COMMENT ON COLUMN cards.card_number IS 'Credit card number (16 characters)';
COMMENT ON COLUMN cards.account_id IS 'Associated account identifier (11 digits)';
COMMENT ON COLUMN cards.customer_id IS 'Associated customer identifier (9 digits)';
