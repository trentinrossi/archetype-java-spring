-- V3__Create_cards_table.sql
-- Create cards table for card account transaction management system

CREATE TABLE cards (
    card_number VARCHAR(16) PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cards_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT fk_cards_account FOREIGN KEY (account_id) REFERENCES accounts(account_id),
    CONSTRAINT chk_card_number CHECK (LENGTH(card_number) = 16)
);

CREATE INDEX idx_cards_card_number ON cards(card_number);
CREATE INDEX idx_cards_customer_id ON cards(customer_id);
CREATE INDEX idx_cards_account_id ON cards(account_id);
