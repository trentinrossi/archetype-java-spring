-- Card Transaction Lifecycle Management Database Schema

-- Create accounts table
CREATE TABLE accounts (
    account_id BIGINT PRIMARY KEY,
    current_balance DECIMAL(11, 2) NOT NULL DEFAULT 0.00,
    credit_limit DECIMAL(11, 2) NOT NULL,
    current_cycle_credit DECIMAL(11, 2) NOT NULL DEFAULT 0.00,
    current_cycle_debit DECIMAL(11, 2) NOT NULL DEFAULT 0.00,
    expiration_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create cards table
CREATE TABLE cards (
    card_number VARCHAR(16) PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    card_details VARCHAR(150),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create card_cross_reference table
CREATE TABLE card_cross_reference (
    card_number VARCHAR(16) PRIMARY KEY,
    account_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (card_number) REFERENCES cards(card_number),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Create transaction_types table
CREATE TABLE transaction_types (
    type_code VARCHAR(2) PRIMARY KEY,
    description VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create transaction_categories table
CREATE TABLE transaction_categories (
    type_code VARCHAR(2) NOT NULL,
    category_code VARCHAR(4) NOT NULL,
    description VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (type_code, category_code),
    FOREIGN KEY (type_code) REFERENCES transaction_types(type_code)
);

-- Create transactions table
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id VARCHAR(16) NOT NULL UNIQUE,
    card_number VARCHAR(16) NOT NULL,
    type_code VARCHAR(2) NOT NULL,
    category_code VARCHAR(4) NOT NULL,
    source VARCHAR(10) NOT NULL,
    description VARCHAR(100),
    amount DECIMAL(11, 2) NOT NULL,
    merchant_id BIGINT NOT NULL,
    merchant_name VARCHAR(50) NOT NULL,
    merchant_city VARCHAR(50),
    merchant_zip VARCHAR(10),
    original_timestamp TIMESTAMP NOT NULL,
    processing_timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (card_number) REFERENCES cards(card_number),
    FOREIGN KEY (type_code) REFERENCES transaction_types(type_code)
);

-- Create transaction_category_balances table
CREATE TABLE transaction_category_balances (
    account_id BIGINT NOT NULL,
    type_code VARCHAR(2) NOT NULL,
    category_code VARCHAR(4) NOT NULL,
    balance DECIMAL(11, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id, type_code, category_code),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id),
    FOREIGN KEY (type_code, category_code) REFERENCES transaction_categories(type_code, category_code)
);

-- Create indexes for performance
CREATE INDEX idx_transactions_card_number ON transactions(card_number);
CREATE INDEX idx_transactions_processing_timestamp ON transactions(processing_timestamp);
CREATE INDEX idx_transactions_type_code ON transactions(type_code);
CREATE INDEX idx_transactions_category_code ON transactions(category_code);
CREATE INDEX idx_card_cross_reference_account_id ON card_cross_reference(account_id);
CREATE INDEX idx_transaction_category_balances_account_id ON transaction_category_balances(account_id);

-- Insert sample transaction types
INSERT INTO transaction_types (type_code, description) VALUES
('01', 'Purchase'),
('02', 'Cash Advance'),
('03', 'Payment'),
('04', 'Refund'),
('05', 'Fee');

-- Insert sample transaction categories
INSERT INTO transaction_categories (type_code, category_code, description) VALUES
('01', '5411', 'Grocery Stores'),
('01', '5812', 'Restaurants'),
('01', '5541', 'Gas Stations'),
('01', '5999', 'Miscellaneous Retail'),
('02', '6011', 'ATM Cash Withdrawal'),
('03', '0000', 'Payment'),
('04', '0000', 'Refund'),
('05', '0001', 'Late Fee'),
('05', '0002', 'Annual Fee');
