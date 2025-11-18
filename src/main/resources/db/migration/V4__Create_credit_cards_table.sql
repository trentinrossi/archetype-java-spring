-- V4__Create_credit_cards_table.sql
-- Create credit_cards table for credit card management with comprehensive validation

CREATE TABLE credit_cards (
    card_number VARCHAR(16) PRIMARY KEY,
    account_id BIGINT NOT NULL,
    customer_id BIGINT,
    embossed_name VARCHAR(50),
    expiration_date DATE NOT NULL,
    expiry_month VARCHAR(2) NOT NULL,
    expiry_year VARCHAR(4) NOT NULL,
    active_status VARCHAR(1) NOT NULL,
    card_status VARCHAR(1) NOT NULL,
    expiration_month INTEGER NOT NULL,
    expiration_year INTEGER NOT NULL,
    expiration_day INTEGER,
    cvv_code VARCHAR(3) NOT NULL,
    version BIGINT DEFAULT 0,
    last_modified_by VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_credit_cards_account FOREIGN KEY (account_id) REFERENCES accounts(account_id),
    CONSTRAINT fk_credit_cards_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT chk_card_number_length CHECK (LENGTH(card_number) = 16),
    CONSTRAINT chk_card_number_numeric CHECK (card_number ~ '^[0-9]{16}$'),
    CONSTRAINT chk_card_status CHECK (card_status IN ('Y', 'N')),
    CONSTRAINT chk_active_status CHECK (active_status IN ('Y', 'N')),
    CONSTRAINT chk_expiration_month CHECK (expiration_month >= 1 AND expiration_month <= 12),
    CONSTRAINT chk_expiration_year CHECK (expiration_year >= 1950 AND expiration_year <= 2099),
    CONSTRAINT chk_cvv_length CHECK (LENGTH(cvv_code) = 3)
);

-- Add comments to table
COMMENT ON TABLE credit_cards IS 'Credit cards with associated details and status';

-- Add comments to columns
COMMENT ON COLUMN credit_cards.card_number IS '16-digit credit card number (primary key)';
COMMENT ON COLUMN credit_cards.account_id IS '11-digit account number associated with the card';
COMMENT ON COLUMN credit_cards.customer_id IS 'Customer who owns the card';
COMMENT ON COLUMN credit_cards.embossed_name IS 'Name embossed on the credit card (alphabets and spaces only)';
COMMENT ON COLUMN credit_cards.expiration_date IS 'Card expiration date in YYYY-MM-DD format';
COMMENT ON COLUMN credit_cards.expiry_month IS 'Expiration month extracted from expiration date (2 digits)';
COMMENT ON COLUMN credit_cards.expiry_year IS 'Expiration year extracted from expiration date (4 digits)';
COMMENT ON COLUMN credit_cards.active_status IS 'Indicates if the card is currently active (Y/N)';
COMMENT ON COLUMN credit_cards.card_status IS 'Active status of the card - Y for active, N for inactive';
COMMENT ON COLUMN credit_cards.expiration_month IS 'Month component of card expiration date (1-12)';
COMMENT ON COLUMN credit_cards.expiration_year IS 'Year component of card expiration date (1950-2099)';
COMMENT ON COLUMN credit_cards.expiration_day IS 'Day component of card expiration date';
COMMENT ON COLUMN credit_cards.cvv_code IS 'Card Verification Value for security purposes (3 digits)';
COMMENT ON COLUMN credit_cards.version IS 'Version number for optimistic locking (concurrent update prevention)';
COMMENT ON COLUMN credit_cards.last_modified_by IS 'User ID of the person who last modified the card';
COMMENT ON COLUMN credit_cards.created_at IS 'Timestamp when the card was created';
COMMENT ON COLUMN credit_cards.updated_at IS 'Timestamp when the card was last updated';

-- Create indexes for faster lookups
CREATE INDEX idx_credit_cards_account_id ON credit_cards(account_id);
CREATE INDEX idx_credit_cards_customer_id ON credit_cards(customer_id);
CREATE INDEX idx_credit_cards_card_status ON credit_cards(card_status);
CREATE INDEX idx_credit_cards_expiration_date ON credit_cards(expiration_date);
CREATE INDEX idx_credit_cards_embossed_name ON credit_cards(embossed_name);

-- Create composite index for common search patterns
CREATE INDEX idx_credit_cards_account_card ON credit_cards(account_id, card_number);
