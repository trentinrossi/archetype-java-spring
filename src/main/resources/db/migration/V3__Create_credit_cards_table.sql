-- Create credit_cards table
-- Card number must be exactly 16 digits numeric
-- Card status is a single character code
CREATE TABLE credit_cards (
    card_number VARCHAR(16) PRIMARY KEY,
    account_id VARCHAR(11) NOT NULL,
    card_status VARCHAR(1) NOT NULL,
    cardholder_name VARCHAR(100),
    expiry_month VARCHAR(2),
    expiry_year VARCHAR(4),
    card_type VARCHAR(20),
    credit_limit DECIMAL(15, 2),
    available_credit DECIMAL(15, 2),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_card_number_format CHECK (card_number ~ '^[0-9]{16}$'),
    CONSTRAINT chk_card_status CHECK (card_status ~ '^[A-Z]$'),
    CONSTRAINT chk_expiry_month CHECK (expiry_month IS NULL OR expiry_month ~ '^(0[1-9]|1[0-2])$'),
    CONSTRAINT chk_expiry_year CHECK (expiry_year IS NULL OR expiry_year ~ '^[0-9]{4}$'),
    CONSTRAINT chk_credit_limit CHECK (credit_limit IS NULL OR credit_limit >= 0),
    CONSTRAINT chk_available_credit CHECK (available_credit IS NULL OR available_credit >= 0),
    CONSTRAINT fk_credit_cards_account FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- Create indexes for faster lookups and filtering
CREATE INDEX idx_credit_cards_account_id ON credit_cards(account_id);
CREATE INDEX idx_credit_cards_card_status ON credit_cards(card_status);
CREATE INDEX idx_credit_cards_account_status ON credit_cards(account_id, card_status);
CREATE INDEX idx_credit_cards_cardholder_name ON credit_cards(cardholder_name);
CREATE INDEX idx_credit_cards_card_type ON credit_cards(card_type);
CREATE INDEX idx_credit_cards_created_at ON credit_cards(created_at);

-- Add comments to table
COMMENT ON TABLE credit_cards IS 'Credit cards associated with customer accounts';
COMMENT ON COLUMN credit_cards.card_number IS 'Unique 16-digit credit card number';
COMMENT ON COLUMN credit_cards.account_id IS 'Associated account identifier (11 digits)';
COMMENT ON COLUMN credit_cards.card_status IS 'Status code: A=Active, I=Inactive, B=Blocked, C=Cancelled, S=Suspended';
COMMENT ON COLUMN credit_cards.credit_limit IS 'Maximum credit limit for the card';
COMMENT ON COLUMN credit_cards.available_credit IS 'Currently available credit on the card';
