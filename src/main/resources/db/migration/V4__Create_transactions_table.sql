-- Create transactions table
CREATE TABLE transactions (
    card_number VARCHAR(16) NOT NULL,
    transaction_id VARCHAR(16) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    transaction_amount DECIMAL(15, 2) NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_time TIMESTAMP WITH TIME ZONE,
    post_date DATE,
    merchant_name VARCHAR(100),
    merchant_category VARCHAR(50),
    merchant_id VARCHAR(20),
    merchant_city VARCHAR(50),
    merchant_state VARCHAR(2),
    merchant_country VARCHAR(3),
    merchant_zip VARCHAR(10),
    authorization_code VARCHAR(20),
    authorization_date DATE,
    authorization_amount DECIMAL(15, 2),
    transaction_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    currency_code VARCHAR(3) DEFAULT 'USD',
    original_amount DECIMAL(15, 2),
    original_currency VARCHAR(3),
    exchange_rate DECIMAL(10, 6),
    reference_number VARCHAR(50),
    description VARCHAR(255),
    is_international BOOLEAN DEFAULT FALSE,
    is_recurring BOOLEAN DEFAULT FALSE,
    is_disputed BOOLEAN DEFAULT FALSE,
    dispute_date DATE,
    dispute_reason VARCHAR(255),
    is_reversed BOOLEAN DEFAULT FALSE,
    reversal_date DATE,
    reversal_reason VARCHAR(255),
    reward_points_earned INTEGER DEFAULT 0,
    cashback_amount DECIMAL(10, 2) DEFAULT 0.00,
    fee_amount DECIMAL(10, 2) DEFAULT 0.00,
    interest_amount DECIMAL(10, 2) DEFAULT 0.00,
    statement_date DATE,
    billing_cycle VARCHAR(6),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (card_number, transaction_id)
);

-- Create indexes for frequently queried columns
CREATE INDEX idx_transactions_card_number ON transactions(card_number);
CREATE INDEX idx_transactions_transaction_date ON transactions(transaction_date);
CREATE INDEX idx_transactions_transaction_status ON transactions(transaction_status);
CREATE INDEX idx_transactions_transaction_type ON transactions(transaction_type);
CREATE INDEX idx_transactions_merchant_name ON transactions(merchant_name);
CREATE INDEX idx_transactions_merchant_category ON transactions(merchant_category);
CREATE INDEX idx_transactions_is_disputed ON transactions(is_disputed);
CREATE INDEX idx_transactions_is_reversed ON transactions(is_reversed);
CREATE INDEX idx_transactions_is_international ON transactions(is_international);
CREATE INDEX idx_transactions_is_recurring ON transactions(is_recurring);
CREATE INDEX idx_transactions_billing_cycle ON transactions(billing_cycle);
CREATE INDEX idx_transactions_statement_date ON transactions(statement_date);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);

-- Add comments
COMMENT ON TABLE transactions IS 'Credit card transaction records containing transaction details, amounts, dates, and status information';
COMMENT ON COLUMN transactions.card_number IS 'Card number portion of composite primary key (16 characters)';
COMMENT ON COLUMN transactions.transaction_id IS 'Unique transaction identifier portion of composite primary key (16 characters)';
COMMENT ON COLUMN transactions.transaction_status IS 'Transaction status: PENDING, AUTHORIZED, POSTED, DECLINED, REVERSED, DISPUTED, SETTLED, CANCELLED, FAILED';
