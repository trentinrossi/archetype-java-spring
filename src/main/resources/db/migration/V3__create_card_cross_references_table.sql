-- Create card_cross_references table
CREATE TABLE card_cross_references (
    card_number VARCHAR(16) NOT NULL,
    cross_reference_data VARCHAR(34) NOT NULL,
    customer_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (card_number),
    CONSTRAINT fk_card_cross_ref_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
    CONSTRAINT fk_card_cross_ref_account FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- Create indexes for faster lookups
CREATE INDEX idx_card_cross_ref_customer_id ON card_cross_references(customer_id);
CREATE INDEX idx_card_cross_ref_account_id ON card_cross_references(account_id);
CREATE INDEX idx_card_cross_ref_card_number ON card_cross_references(card_number);

-- Add comment to table
COMMENT ON TABLE card_cross_references IS 'Maintains relationships between card numbers, customer identifiers, and account identifiers';
