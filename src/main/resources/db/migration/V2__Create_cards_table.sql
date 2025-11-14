-- V2__Create_cards_table.sql
-- Migration for Card entity - Bill Payment Processing

CREATE TABLE cards (
    id BIGSERIAL PRIMARY KEY,
    xref_card_num VARCHAR(16) NOT NULL,
    xref_acct_id VARCHAR(11) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_card_num ON cards(xref_card_num);
CREATE INDEX idx_card_acct_id ON cards(xref_acct_id);

ALTER TABLE cards ADD CONSTRAINT fk_card_account FOREIGN KEY (xref_acct_id) REFERENCES accounts(acct_id);
