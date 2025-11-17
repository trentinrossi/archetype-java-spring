-- V3__Create_cards_table.sql
-- Card table for credit cards linked to accounts

CREATE TABLE cards (
    card_num VARCHAR(16) NOT NULL,
    acct_id BIGINT NOT NULL,
    account_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (card_num)
);

CREATE INDEX idx_card_acct_id ON cards(acct_id);
CREATE INDEX idx_card_account_id ON cards(account_id);
