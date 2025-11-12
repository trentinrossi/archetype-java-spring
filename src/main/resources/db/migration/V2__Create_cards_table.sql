-- V2__Create_cards_table.sql
CREATE TABLE cards (
    card_number VARCHAR(16) PRIMARY KEY,
    account_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    cvv_code VARCHAR(3) NOT NULL,
    expiration_date VARCHAR(10) NOT NULL,
    xref_card_num VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cards_account_id ON cards(account_id);
CREATE INDEX idx_cards_customer_id ON cards(customer_id);
CREATE INDEX idx_cards_xref_card_num ON cards(xref_card_num);
CREATE INDEX idx_cards_expiration_date ON cards(expiration_date);
