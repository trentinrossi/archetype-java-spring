-- Migration: Create credit_cards table
-- Business Rule BR004: Credit card records can be filtered by account ID and/or card number
-- Business Rule BR008: Records that do not match the specified filter criteria are excluded from display

CREATE TABLE credit_cards (
    id BIGSERIAL PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    account_id VARCHAR(11) NOT NULL,
    card_status VARCHAR(1) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_credit_card_account FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    CONSTRAINT chk_card_number_format CHECK (card_number ~ '^\d{16}$'),
    CONSTRAINT chk_card_status CHECK (card_status IN ('A', 'B', 'E', 'S', 'C'))
);

-- Create indexes for performance and filtering
CREATE INDEX idx_card_number ON credit_cards(card_number);
CREATE INDEX idx_credit_card_account_id ON credit_cards(account_id);
CREATE INDEX idx_card_status ON credit_cards(card_status);

-- Add comments to table
COMMENT ON TABLE credit_cards IS 'Credit cards associated with customer accounts';
COMMENT ON COLUMN credit_cards.id IS 'Internal unique identifier for the credit card';
COMMENT ON COLUMN credit_cards.card_number IS 'Unique 16-digit credit card number';
COMMENT ON COLUMN credit_cards.account_id IS 'Associated account identifier (11 digits)';
COMMENT ON COLUMN credit_cards.card_status IS 'Current status: A=Active, B=Blocked, E=Expired, S=Suspended, C=Cancelled';
COMMENT ON COLUMN credit_cards.created_at IS 'Timestamp when the card was created';
COMMENT ON COLUMN credit_cards.updated_at IS 'Timestamp when the card was last updated';
