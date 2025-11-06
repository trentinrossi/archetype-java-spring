-- Create card_cross_references table
CREATE TABLE card_cross_references (
    card_number VARCHAR(16) PRIMARY KEY,
    customer_id VARCHAR(9) NOT NULL,
    account_id VARCHAR(11) NOT NULL,
    card_type VARCHAR(20),
    card_holder_name VARCHAR(100),
    expiration_date DATE,
    issue_date DATE,
    card_status VARCHAR(20) NOT NULL DEFAULT 'PENDING_ACTIVATION',
    is_primary_card BOOLEAN DEFAULT FALSE,
    card_sequence_number INTEGER,
    embossed_name VARCHAR(26),
    pin_set BOOLEAN DEFAULT FALSE,
    pin_set_date DATE,
    activation_date DATE,
    last_used_date DATE,
    replacement_card_number VARCHAR(16),
    replaced_card_number VARCHAR(16),
    card_production_date DATE,
    card_mailed_date DATE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_card_cross_references_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
    CONSTRAINT fk_card_cross_references_account FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- Create indexes for frequently queried columns
CREATE INDEX idx_card_cross_references_customer_id ON card_cross_references(customer_id);
CREATE INDEX idx_card_cross_references_account_id ON card_cross_references(account_id);
CREATE INDEX idx_card_cross_references_card_status ON card_cross_references(card_status);
CREATE INDEX idx_card_cross_references_card_type ON card_cross_references(card_type);
CREATE INDEX idx_card_cross_references_expiration_date ON card_cross_references(expiration_date);
CREATE INDEX idx_card_cross_references_is_primary_card ON card_cross_references(is_primary_card);
CREATE INDEX idx_card_cross_references_pin_set ON card_cross_references(pin_set);
CREATE INDEX idx_card_cross_references_last_used_date ON card_cross_references(last_used_date);
CREATE INDEX idx_card_cross_references_created_at ON card_cross_references(created_at);

-- Add comments
COMMENT ON TABLE card_cross_references IS 'Maintains relationships between card numbers, customer identifiers, and account identifiers';
COMMENT ON COLUMN card_cross_references.card_number IS 'Primary key - card number linking to customer and account (16 characters)';
COMMENT ON COLUMN card_cross_references.card_status IS 'Card status: ACTIVE, INACTIVE, SUSPENDED, LOST, STOLEN, DAMAGED, EXPIRED, PENDING_ACTIVATION, CLOSED, FRAUD';
