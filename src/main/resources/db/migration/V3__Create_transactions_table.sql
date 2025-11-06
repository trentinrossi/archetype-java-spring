-- V3__Create_transactions_table.sql
CREATE TABLE transactions (
    card_number VARCHAR(16) NOT NULL,
    transaction_id VARCHAR(16) NOT NULL,
    transaction_data VARCHAR(318) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (card_number, transaction_id)
);

CREATE INDEX idx_transactions_card_number ON transactions(card_number);
CREATE INDEX idx_transactions_composite_key ON transactions(card_number, transaction_id);

COMMENT ON TABLE transactions IS 'Credit card transaction records containing transaction details, amounts, dates, and status information';
COMMENT ON COLUMN transactions.card_number IS 'Card number portion of composite primary key (must be exactly 16 characters)';
COMMENT ON COLUMN transactions.transaction_id IS 'Unique transaction identifier portion of composite primary key (must be exactly 16 characters)';
COMMENT ON COLUMN transactions.transaction_data IS 'Complete transaction details including amounts, dates, merchant information, and status';