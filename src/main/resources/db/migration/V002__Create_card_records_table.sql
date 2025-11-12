-- V002__Create_card_records_table.sql

CREATE TABLE card_records (
    card_number VARCHAR(16) PRIMARY KEY,
    card_data VARCHAR(134) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_card_records_card_number ON card_records(card_number);
