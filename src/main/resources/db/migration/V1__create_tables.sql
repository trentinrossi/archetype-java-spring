-- Create accounts table
CREATE TABLE accounts (
    account_id VARCHAR(11) PRIMARY KEY,
    active_status VARCHAR(1) NOT NULL,
    current_balance DECIMAL(19,2) NOT NULL,
    credit_limit DECIMAL(19,2) NOT NULL,
    cash_credit_limit DECIMAL(19,2) NOT NULL,
    open_date VARCHAR(10) NOT NULL,
    expiration_date VARCHAR(10) NOT NULL,
    reissue_date VARCHAR(10) NOT NULL,
    current_cycle_credit DECIMAL(19,2) NOT NULL,
    current_cycle_debit DECIMAL(19,2) NOT NULL,
    address_zip_code VARCHAR(10) NOT NULL,
    group_id VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create customers table
CREATE TABLE customers (
    customer_id VARCHAR(9) PRIMARY KEY,
    first_name VARCHAR(25) NOT NULL,
    middle_name VARCHAR(25),
    last_name VARCHAR(25) NOT NULL,
    address_line_1 VARCHAR(50) NOT NULL,
    address_line_2 VARCHAR(50),
    address_line_3 VARCHAR(50),
    state_code VARCHAR(2) NOT NULL,
    country_code VARCHAR(3) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    phone_number_1 VARCHAR(15),
    phone_number_2 VARCHAR(15),
    ssn VARCHAR(9) NOT NULL,
    government_issued_id VARCHAR(20),
    date_of_birth VARCHAR(10) NOT NULL,
    eft_account_id VARCHAR(10),
    primary_cardholder_indicator VARCHAR(1) NOT NULL,
    fico_credit_score INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create cards table
CREATE TABLE cards (
    card_number VARCHAR(16) PRIMARY KEY,
    account_id VARCHAR(11) NOT NULL,
    cvv_code VARCHAR(3) NOT NULL,
    embossed_name VARCHAR(50) NOT NULL,
    expiration_date VARCHAR(10) NOT NULL,
    active_status VARCHAR(1) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Create card_cross_references table
CREATE TABLE card_cross_references (
    card_number VARCHAR(16) PRIMARY KEY,
    customer_id VARCHAR(9) NOT NULL,
    account_id VARCHAR(11) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (card_number) REFERENCES cards(card_number),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Create transactions table
CREATE TABLE transactions (
    transaction_id VARCHAR(16) PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL,
    account_id VARCHAR(11) NOT NULL,
    transaction_type_code VARCHAR(2) NOT NULL,
    transaction_category_code VARCHAR(4) NOT NULL,
    transaction_source VARCHAR(50) NOT NULL,
    transaction_description VARCHAR(100) NOT NULL,
    transaction_amount DECIMAL(19,2) NOT NULL,
    merchant_id BIGINT,
    merchant_name VARCHAR(50),
    merchant_city VARCHAR(50),
    merchant_zip VARCHAR(10),
    original_timestamp TIMESTAMP NOT NULL,
    processing_timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (card_number) REFERENCES cards(card_number),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Create transaction_category_balances table
CREATE TABLE transaction_category_balances (
    account_id VARCHAR(11) NOT NULL,
    transaction_type_code VARCHAR(2) NOT NULL,
    transaction_category_code VARCHAR(4) NOT NULL,
    balance DECIMAL(19,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id, transaction_type_code, transaction_category_code),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Create disclosure_groups table
CREATE TABLE disclosure_groups (
    account_group_id VARCHAR(10) NOT NULL,
    transaction_type_code VARCHAR(2) NOT NULL,
    transaction_category_code VARCHAR(4) NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (account_group_id, transaction_type_code, transaction_category_code)
);

-- Create indexes for better query performance
CREATE INDEX idx_cards_account_id ON cards(account_id);
CREATE INDEX idx_cards_active_status ON cards(active_status);
CREATE INDEX idx_accounts_active_status ON accounts(active_status);
CREATE INDEX idx_accounts_group_id ON accounts(group_id);
CREATE INDEX idx_customers_last_name ON customers(last_name);
CREATE INDEX idx_transactions_card_number ON transactions(card_number);
CREATE INDEX idx_transactions_account_id ON transactions(account_id);
CREATE INDEX idx_transactions_original_timestamp ON transactions(original_timestamp);
CREATE INDEX idx_card_cross_references_customer_id ON card_cross_references(customer_id);
CREATE INDEX idx_card_cross_references_account_id ON card_cross_references(account_id);
CREATE INDEX idx_transaction_category_balances_account_id ON transaction_category_balances(account_id);
