-- Card Transaction Lifecycle Management Database Schema

-- Customers table
CREATE TABLE customers (
    customer_id VARCHAR(9) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    address VARCHAR(200),
    city VARCHAR(50),
    state VARCHAR(2),
    zip_code VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Accounts table
CREATE TABLE accounts (
    account_id VARCHAR(11) PRIMARY KEY,
    customer_id VARCHAR(9) NOT NULL,
    current_balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    credit_limit DECIMAL(19, 2) NOT NULL,
    current_cycle_credit DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    current_cycle_debit DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    expiration_date DATE NOT NULL,
    account_status VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Cards table
CREATE TABLE cards (
    card_number VARCHAR(16) PRIMARY KEY,
    account_id VARCHAR(11) NOT NULL,
    customer_id VARCHAR(9) NOT NULL,
    card_status VARCHAR(10) NOT NULL,
    expiration_date DATE NOT NULL,
    card_type VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_card_account FOREIGN KEY (account_id) REFERENCES accounts(account_id),
    CONSTRAINT fk_card_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Card Cross Reference table
CREATE TABLE card_cross_reference (
    card_number VARCHAR(16) PRIMARY KEY,
    account_id VARCHAR(11) NOT NULL,
    customer_id VARCHAR(9) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_xref_card FOREIGN KEY (card_number) REFERENCES cards(card_number),
    CONSTRAINT fk_xref_account FOREIGN KEY (account_id) REFERENCES accounts(account_id),
    CONSTRAINT fk_xref_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Transactions table
CREATE TABLE transactions (
    transaction_id VARCHAR(16) PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL,
    account_id VARCHAR(11) NOT NULL,
    transaction_type_code VARCHAR(2) NOT NULL,
    transaction_category_code INTEGER NOT NULL,
    transaction_source VARCHAR(10) NOT NULL,
    transaction_description VARCHAR(100) NOT NULL,
    transaction_amount DECIMAL(19, 2) NOT NULL,
    merchant_id BIGINT NOT NULL,
    merchant_name VARCHAR(50) NOT NULL,
    merchant_city VARCHAR(50) NOT NULL,
    merchant_zip VARCHAR(10) NOT NULL,
    original_timestamp TIMESTAMP NOT NULL,
    processing_timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_card FOREIGN KEY (card_number) REFERENCES cards(card_number),
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Transaction Category Balance table
CREATE TABLE transaction_category_balance (
    account_id VARCHAR(11) NOT NULL,
    transaction_type_code VARCHAR(2) NOT NULL,
    transaction_category_code INTEGER NOT NULL,
    category_balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id, transaction_type_code, transaction_category_code),
    CONSTRAINT fk_catbal_account FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Indexes for performance optimization
CREATE INDEX idx_transactions_card_number ON transactions(card_number);
CREATE INDEX idx_transactions_account_id ON transactions(account_id);
CREATE INDEX idx_transactions_processing_timestamp ON transactions(processing_timestamp);
CREATE INDEX idx_transactions_original_timestamp ON transactions(original_timestamp);
CREATE INDEX idx_accounts_customer_id ON accounts(customer_id);
CREATE INDEX idx_cards_account_id ON cards(account_id);
CREATE INDEX idx_cards_customer_id ON cards(customer_id);
CREATE INDEX idx_card_xref_account_id ON card_cross_reference(account_id);
CREATE INDEX idx_transaction_category_balance_account ON transaction_category_balance(account_id);

-- Comments for documentation
COMMENT ON TABLE customers IS 'Customer master data';
COMMENT ON TABLE accounts IS 'Customer account information with balances and credit limits';
COMMENT ON TABLE cards IS 'Credit card information linked to accounts';
COMMENT ON TABLE card_cross_reference IS 'Cross-reference mapping between cards, accounts, and customers';
COMMENT ON TABLE transactions IS 'Financial transaction records with merchant details';
COMMENT ON TABLE transaction_category_balance IS 'Running balances by transaction category and account';
