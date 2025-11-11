-- V2__Create_card_cross_reference_table.sql
-- Create card_cross_reference table for bill payment system
-- Cross-reference between accounts and card numbers

CREATE TABLE card_cross_reference (
    id BIGSERIAL PRIMARY KEY,
    xref_acct_id VARCHAR(11) NOT NULL,
    xref_card_num VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_card_xref_account FOREIGN KEY (xref_acct_id) REFERENCES accounts(acct_id) ON DELETE CASCADE,
    CONSTRAINT uk_card_xref_acct_card UNIQUE (xref_acct_id, xref_card_num)
);

-- Add comment to table
COMMENT ON TABLE card_cross_reference IS 'Cross-reference between accounts and card numbers';

-- Add comments to columns
COMMENT ON COLUMN card_cross_reference.id IS 'Unique identifier for the cross-reference';
COMMENT ON COLUMN card_cross_reference.xref_acct_id IS 'Account ID in cross-reference (11 characters)';
COMMENT ON COLUMN card_cross_reference.xref_card_num IS 'Card number associated with account (16 characters)';
COMMENT ON COLUMN card_cross_reference.created_at IS 'Cross-reference creation timestamp';
COMMENT ON COLUMN card_cross_reference.updated_at IS 'Cross-reference last update timestamp';

-- Create indexes for performance
CREATE INDEX idx_card_xref_acct_id ON card_cross_reference(xref_acct_id);
CREATE INDEX idx_card_xref_card_num ON card_cross_reference(xref_card_num);
