-- Create transactions table
CREATE TABLE transactions (
    card_number VARCHAR(16) NOT NULL,
    transaction_id VARCHAR(16) NOT NULL,
    transaction_data VARCHAR(318) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (card_number, transaction_id),
    CONSTRAINT fk_transactions_card FOREIGN KEY (card_number) REFERENCES card_cross_references(card_number) ON DELETE CASCADE
);

-- Create indexes for faster lookups and sequential access
CREATE INDEX idx_transactions_card_number ON transactions(card_number);
CREATE INDEX idx_transactions_composite_key ON transactions(card_number, transaction_id);
CREATE INDEX idx_transactions_transaction_id ON transactions(transaction_id);

-- Add comment to table
COMMENT ON TABLE transactions IS 'Credit card transaction records containing transaction details, amounts, dates, and status information';
