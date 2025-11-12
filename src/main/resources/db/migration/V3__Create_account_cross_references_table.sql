-- V3__Create_account_cross_references_table.sql

CREATE TABLE account_cross_references (
    card_number VARCHAR(16) NOT NULL,
    cross_reference_data VARCHAR(34) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (card_number)
);

CREATE INDEX idx_account_cross_references_card_number ON account_cross_references(card_number);
