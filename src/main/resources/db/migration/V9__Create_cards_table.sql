-- V9__Create_cards_table.sql

CREATE TABLE cards (
    card_number VARCHAR(16) NOT NULL,
    account_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (card_number)
);

CREATE INDEX idx_cards_account_id ON cards(account_id);
