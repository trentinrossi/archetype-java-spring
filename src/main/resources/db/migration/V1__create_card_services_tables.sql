-- Create accounts table
CREATE TABLE accounts (
    account_id VARCHAR(11) PRIMARY KEY,
    active_status VARCHAR(1) NOT NULL CHECK (active_status IN ('Y', 'N')),
    current_balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    credit_limit DECIMAL(19, 2) NOT NULL,
    cash_credit_limit DECIMAL(19, 2) NOT NULL,
    open_date DATE NOT NULL,
    expiration_date DATE NOT NULL,
    reissue_date DATE,
    current_cycle_credit DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    current_cycle_debit DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    group_id VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create customers table
CREATE TABLE customers (
    customer_id VARCHAR(9) PRIMARY KEY,
    first_name VARCHAR(25) NOT NULL,
    middle_name VARCHAR(25),
    last_name VARCHAR(25) NOT NULL,
    address_line1 VARCHAR(50) NOT NULL,
    address_line2 VARCHAR(50),
    city VARCHAR(50) NOT NULL,
    state_code VARCHAR(2) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    country_code VARCHAR(3) NOT NULL DEFAULT 'USA',
    phone_number1 VARCHAR(15),
    phone_number2 VARCHAR(15),
    ssn VARCHAR(9) NOT NULL,
    government_issued_id VARCHAR(20),
    date_of_birth DATE NOT NULL,
    eft_account_id VARCHAR(10) NOT NULL,
    primary_card_holder_indicator VARCHAR(1) CHECK (primary_card_holder_indicator IN ('Y', 'N')),
    fico_credit_score INTEGER CHECK (fico_credit_score BETWEEN 300 AND 850),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create cards table
CREATE TABLE cards (
    card_number VARCHAR(16) PRIMARY KEY,
    account_id VARCHAR(11) NOT NULL,
    cvv_code VARCHAR(3) NOT NULL,
    embossed_name VARCHAR(50) NOT NULL,
    expiration_date DATE NOT NULL,
    active_status VARCHAR(1) NOT NULL CHECK (active_status IN ('Y', 'N')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Create transactions table
CREATE TABLE transactions (
    transaction_id VARCHAR(16) PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL,
    type_code VARCHAR(2) NOT NULL,
    category_code INTEGER NOT NULL,
    source VARCHAR(10) NOT NULL,
    description VARCHAR(60),
    amount DECIMAL(19, 2) NOT NULL,
    merchant_id VARCHAR(9),
    merchant_name VARCHAR(30),
    merchant_city VARCHAR(25),
    merchant_zip VARCHAR(10),
    origination_timestamp TIMESTAMP NOT NULL,
    processing_timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (card_number) REFERENCES cards(card_number)
);

-- Create card_cross_references table
CREATE TABLE card_cross_references (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL,
    account_id VARCHAR(11) NOT NULL,
    customer_id VARCHAR(9) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (card_number) REFERENCES cards(card_number),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    UNIQUE KEY uk_card_account_customer (card_number, account_id, customer_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_accounts_active_status ON accounts(active_status);
CREATE INDEX idx_accounts_group_id ON accounts(group_id);
CREATE INDEX idx_customers_last_name ON customers(last_name);
CREATE INDEX idx_customers_state_code ON customers(state_code);
CREATE INDEX idx_cards_account_id ON cards(account_id);
CREATE INDEX idx_cards_active_status ON cards(active_status);
CREATE INDEX idx_transactions_card_number ON transactions(card_number);
CREATE INDEX idx_transactions_origination_timestamp ON transactions(origination_timestamp);
CREATE INDEX idx_card_cross_references_account_id ON card_cross_references(account_id);
CREATE INDEX idx_card_cross_references_customer_id ON card_cross_references(customer_id);
