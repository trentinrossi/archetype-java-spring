-- V2__Create_card_cross_reference_table.sql
-- Create card_cross_reference table for Bill Payment system
-- Cross-reference between accounts and card numbers

CREATE TABLE card_cross_reference (
    xref_acct_id VARCHAR(11) NOT NULL,
    xref_card_num VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (xref_acct_id, xref_card_num),
    CONSTRAINT fk_card_xref_account FOREIGN KEY (xref_acct_id) 
        REFERENCES accounts(acct_id) ON DELETE CASCADE
);

-- Create indexes for performance
CREATE INDEX idx_card_xref_account ON card_cross_reference(xref_acct_id);
CREATE INDEX idx_card_xref_card ON card_cross_reference(xref_card_num);

-- Add comments for documentation
COMMENT ON TABLE card_cross_reference IS 'Cross-reference between accounts and card numbers';
COMMENT ON COLUMN card_cross_reference.xref_acct_id IS 'Account ID in cross-reference (11 characters)';
COMMENT ON COLUMN card_cross_reference.xref_card_num IS 'Card number associated with account (16 characters)';
COMMENT ON COLUMN card_cross_reference.created_at IS 'Timestamp when the cross-reference was created';
COMMENT ON COLUMN card_cross_reference.updated_at IS 'Timestamp when the cross-reference was last updated';
