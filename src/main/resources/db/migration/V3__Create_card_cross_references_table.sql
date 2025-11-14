-- V3__Create_card_cross_references_table.sql
-- Migration script for creating card_cross_references table
-- Supports H2 and PostgreSQL syntax

CREATE TABLE card_cross_references (
    account_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id)
);

-- Foreign key constraints
ALTER TABLE card_cross_references ADD CONSTRAINT fk_card_cross_ref_account 
    FOREIGN KEY (account_id) REFERENCES accounts(account_id);

ALTER TABLE card_cross_references ADD CONSTRAINT fk_card_cross_ref_customer 
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id);

-- Indexes for performance optimization
CREATE INDEX idx_card_cross_ref_customer_id ON card_cross_references(customer_id);

-- Comments for documentation
COMMENT ON TABLE card_cross_references IS 'Links account and card information';
COMMENT ON COLUMN card_cross_references.account_id IS 'Account Identification Number (11 digits)';
COMMENT ON COLUMN card_cross_references.customer_id IS 'Customer Identification Number (9 digits)';
