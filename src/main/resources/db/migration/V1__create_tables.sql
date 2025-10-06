-- Create accounts table based on CBACT01C program functionality
CREATE TABLE accounts (
    acct_id VARCHAR(11) NOT NULL,
    acct_active_status VARCHAR(1) NOT NULL,
    acct_curr_bal DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    acct_credit_limit DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    acct_cash_credit_limit DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    acct_open_date DATE NOT NULL,
    acct_expiration_date DATE NOT NULL,
    acct_reissue_date DATE,
    acct_curr_cyc_credit DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    acct_curr_cyc_debit DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    acct_group_id VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (acct_id)
);

-- Create customers table based on CBCUS01C program functionality
CREATE TABLE customers (
    customer_id VARCHAR(9) NOT NULL,
    customer_data VARCHAR(491) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    middle_initial VARCHAR(1),
    address_line_1 VARCHAR(100),
    address_line_2 VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(2),
    zip_code VARCHAR(10),
    phone_number VARCHAR(15),
    email_address VARCHAR(100),
    customer_status VARCHAR(1) NOT NULL DEFAULT 'A',
    customer_type VARCHAR(2),
    credit_rating VARCHAR(3),
    date_of_birth VARCHAR(8),
    social_security_number VARCHAR(11),
    employer_name VARCHAR(50),
    annual_income VARCHAR(15),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (customer_id)
);

-- Create transactions table based on CBSTM03B program functionality
CREATE TABLE transactions (
    card_number VARCHAR(16) NOT NULL,
    transaction_id VARCHAR(16) NOT NULL,
    transaction_data VARCHAR(318) NOT NULL,
    transaction_type VARCHAR(4),
    transaction_amount VARCHAR(15),
    transaction_date VARCHAR(8),
    transaction_time VARCHAR(6),
    merchant_id VARCHAR(15),
    merchant_name VARCHAR(50),
    merchant_category VARCHAR(4),
    authorization_code VARCHAR(6),
    response_code VARCHAR(2),
    terminal_id VARCHAR(8),
    currency_code VARCHAR(3),
    transaction_status VARCHAR(1) NOT NULL DEFAULT 'P',
    batch_number VARCHAR(6),
    sequence_number VARCHAR(6),
    reference_number VARCHAR(12),
    original_amount VARCHAR(15),
    cashback_amount VARCHAR(15),
    fee_amount VARCHAR(15),
    processing_code VARCHAR(6),
    pos_entry_mode VARCHAR(3),
    card_acceptor_id VARCHAR(15),
    acquirer_id VARCHAR(11),
    retrieval_reference VARCHAR(12),
    system_trace_number VARCHAR(6),
    network_id VARCHAR(3),
    settlement_date VARCHAR(8),
    capture_date VARCHAR(8),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (card_number, transaction_id)
);

-- Create card_cross_references table based on CBACT03C and CBSTM03B program functionality
CREATE TABLE card_cross_references (
    card_number VARCHAR(16) NOT NULL,
    cross_reference_data VARCHAR(34) NOT NULL,
    account_number VARCHAR(20),
    account_type VARCHAR(2),
    customer_id VARCHAR(12),
    card_status VARCHAR(1) NOT NULL DEFAULT 'A',
    card_type VARCHAR(2),
    issue_date VARCHAR(8),
    expiry_date VARCHAR(8),
    branch_code VARCHAR(4),
    product_code VARCHAR(3),
    card_sequence VARCHAR(3),
    pin_offset VARCHAR(4),
    daily_limit VARCHAR(10),
    monthly_limit VARCHAR(10),
    last_activity_date VARCHAR(8),
    activation_date VARCHAR(8),
    block_code VARCHAR(2),
    replacement_card VARCHAR(16),
    previous_card VARCHAR(16),
    emboss_name VARCHAR(26),
    delivery_method VARCHAR(1),
    priority_flag VARCHAR(1),
    international_flag VARCHAR(1),
    contactless_flag VARCHAR(1),
    mobile_payment_flag VARCHAR(1),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (card_number)
);

-- Create indexes for better performance on sequential reads and common queries

-- Accounts indexes
CREATE INDEX idx_accounts_status ON accounts(acct_active_status);
CREATE INDEX idx_accounts_group ON accounts(acct_group_id);
CREATE INDEX idx_accounts_status_group ON accounts(acct_active_status, acct_group_id);
CREATE INDEX idx_accounts_open_date ON accounts(acct_open_date);
CREATE INDEX idx_accounts_expiration_date ON accounts(acct_expiration_date);
CREATE INDEX idx_accounts_reissue_date ON accounts(acct_reissue_date);
CREATE INDEX idx_accounts_balance ON accounts(acct_curr_bal);
CREATE INDEX idx_accounts_credit_limit ON accounts(acct_credit_limit);

-- Customers indexes
CREATE INDEX idx_customers_status ON customers(customer_status);
CREATE INDEX idx_customers_type ON customers(customer_type);
CREATE INDEX idx_customers_status_type ON customers(customer_status, customer_type);
CREATE INDEX idx_customers_first_name ON customers(first_name);
CREATE INDEX idx_customers_last_name ON customers(last_name);
CREATE INDEX idx_customers_email ON customers(email_address);
CREATE INDEX idx_customers_phone ON customers(phone_number);
CREATE INDEX idx_customers_city ON customers(city);
CREATE INDEX idx_customers_state ON customers(state);
CREATE INDEX idx_customers_zip ON customers(zip_code);
CREATE INDEX idx_customers_credit_rating ON customers(credit_rating);

-- Transactions indexes
CREATE INDEX idx_transactions_card ON transactions(card_number);
CREATE INDEX idx_transactions_status ON transactions(transaction_status);
CREATE INDEX idx_transactions_type ON transactions(transaction_type);
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
CREATE INDEX idx_transactions_merchant ON transactions(merchant_id);
CREATE INDEX idx_transactions_terminal ON transactions(terminal_id);
CREATE INDEX idx_transactions_amount ON transactions(transaction_amount);
CREATE INDEX idx_transactions_response_code ON transactions(response_code);
CREATE INDEX idx_transactions_settlement_date ON transactions(settlement_date);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);

-- Card cross references indexes
CREATE INDEX idx_card_xref_status ON card_cross_references(card_status);
CREATE INDEX idx_card_xref_type ON card_cross_references(card_type);
CREATE INDEX idx_card_xref_customer ON card_cross_references(customer_id);
CREATE INDEX idx_card_xref_account ON card_cross_references(account_number);
CREATE INDEX idx_card_xref_branch ON card_cross_references(branch_code);
CREATE INDEX idx_card_xref_product ON card_cross_references(product_code);
CREATE INDEX idx_card_xref_expiry ON card_cross_references(expiry_date);
CREATE INDEX idx_card_xref_last_activity ON card_cross_references(last_activity_date);
CREATE INDEX idx_card_xref_block_code ON card_cross_references(block_code);

-- Add comments to tables for documentation
COMMENT ON TABLE accounts IS 'Account data table based on CBACT01C COBOL program functionality for sequential account reading and display';
COMMENT ON TABLE customers IS 'Customer data table based on CBCUS01C COBOL program functionality for sequential customer reading and display';
COMMENT ON TABLE transactions IS 'Transaction data table based on CBSTM03B COBOL program functionality for transaction file access and processing';
COMMENT ON TABLE card_cross_references IS 'Card cross-reference data table based on CBACT03C COBOL program functionality for card-to-account mapping';

-- Add column comments for key fields
COMMENT ON COLUMN accounts.acct_id IS 'Account ID - 11 digit numeric identifier (primary key)';
COMMENT ON COLUMN accounts.acct_active_status IS 'Account active status - Y/N/A for active, N for inactive';
COMMENT ON COLUMN customers.customer_id IS 'Customer ID - 9 character identifier (primary key)';
COMMENT ON COLUMN customers.customer_data IS 'Customer data - 491 character data field from original COBOL structure';
COMMENT ON COLUMN transactions.card_number IS 'Card number - 16 digit card identifier (part of composite key)';
COMMENT ON COLUMN transactions.transaction_id IS 'Transaction ID - 16 character transaction identifier (part of composite key)';
COMMENT ON COLUMN card_cross_references.card_number IS 'Card number - 16 digit card identifier (primary key)';
COMMENT ON COLUMN card_cross_references.cross_reference_data IS 'Cross reference data - 34 character data field from original COBOL structure';