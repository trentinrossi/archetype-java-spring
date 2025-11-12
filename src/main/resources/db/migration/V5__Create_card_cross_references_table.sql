-- V5__Create_card_cross_references_table.sql

CREATE TABLE card_cross_references (
    id BIGSERIAL PRIMARY KEY,
    xref_card_num VARCHAR(16) NOT NULL,
    xref_cust_num BIGINT NOT NULL,
    xref_acct_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    card_number VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_card_cross_references_card_num ON card_cross_references(xref_card_num);
CREATE INDEX idx_card_cross_references_acct_id ON card_cross_references(xref_acct_id);
CREATE INDEX idx_card_cross_references_account_id ON card_cross_references(account_id);
