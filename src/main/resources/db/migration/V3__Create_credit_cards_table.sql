-- V3__Create_credit_cards_table.sql
-- Create credit_cards table for credit card management
-- Business Rules: BR002, BR004, BR005

CREATE TABLE credit_cards (
    card_number VARCHAR(16) NOT NULL,
    account_id VARCHAR(11) NOT NULL,
    card_status CHAR(1) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (card_number),
    CONSTRAINT fk_credit_cards_account FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    CONSTRAINT chk_credit_cards_card_number_format CHECK (card_number ~ '^[0-9]{16}$'),
    CONSTRAINT chk_credit_cards_card_number_not_zeros CHECK (card_number !~ '^0+$'),
    CONSTRAINT chk_credit_cards_account_id_format CHECK (account_id ~ '^[0-9]{11}$'),
    CONSTRAINT chk_credit_cards_account_id_not_zeros CHECK (account_id !~ '^0+$'),
    CONSTRAINT chk_credit_cards_card_status CHECK (card_status IN ('A', 'I', 'B', 'P', 'C', 'S', 'E', 'L', 'T', 'D'))
);

-- Indexes for performance
CREATE INDEX idx_credit_cards_account_id ON credit_cards(account_id);
CREATE INDEX idx_credit_cards_card_status ON credit_cards(card_status);
CREATE INDEX idx_credit_cards_created_at ON credit_cards(created_at);
CREATE INDEX idx_credit_cards_updated_at ON credit_cards(updated_at);
CREATE INDEX idx_credit_cards_account_status ON credit_cards(account_id, card_status);

-- Comments
COMMENT ON TABLE credit_cards IS 'Credit cards with associated account and status information';
COMMENT ON COLUMN credit_cards.card_number IS 'Unique 16-digit credit card number (BR002)';
COMMENT ON COLUMN credit_cards.account_id IS 'Associated 11-digit account identifier (BR004)';
COMMENT ON COLUMN credit_cards.card_status IS 'Current status of the credit card: A=Active, I=Inactive, B=Blocked, P=Pending, C=Closed, S=Suspended, E=Expired, L=Lost, T=Stolen, D=Damaged (BR005)';
COMMENT ON COLUMN credit_cards.created_at IS 'Timestamp when the card was created';
COMMENT ON COLUMN credit_cards.updated_at IS 'Timestamp when the card was last updated';
