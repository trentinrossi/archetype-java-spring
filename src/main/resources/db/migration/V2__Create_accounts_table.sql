-- Create accounts table
CREATE TABLE accounts (
    account_id VARCHAR(11) PRIMARY KEY,
    customer_id VARCHAR(9) NOT NULL,
    account_number VARCHAR(20) UNIQUE,
    account_type VARCHAR(20) NOT NULL,
    current_balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    available_balance DECIMAL(15, 2) DEFAULT 0.00,
    credit_limit DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    cash_advance_limit DECIMAL(15, 2),
    minimum_payment_due DECIMAL(15, 2) DEFAULT 0.00,
    payment_due_date DATE,
    last_payment_amount DECIMAL(15, 2),
    last_payment_date DATE,
    last_statement_balance DECIMAL(15, 2),
    last_statement_date DATE,
    next_statement_date DATE,
    account_open_date DATE NOT NULL,
    account_close_date DATE,
    interest_rate DECIMAL(5, 2),
    annual_fee DECIMAL(10, 2),
    late_fee DECIMAL(10, 2),
    overlimit_fee DECIMAL(10, 2),
    days_delinquent INTEGER DEFAULT 0,
    cycle_to_date_purchases DECIMAL(15, 2) DEFAULT 0.00,
    cycle_to_date_cash_advances DECIMAL(15, 2) DEFAULT 0.00,
    cycle_to_date_payments DECIMAL(15, 2) DEFAULT 0.00,
    year_to_date_purchases DECIMAL(15, 2) DEFAULT 0.00,
    year_to_date_cash_advances DECIMAL(15, 2) DEFAULT 0.00,
    year_to_date_interest DECIMAL(15, 2) DEFAULT 0.00,
    year_to_date_fees DECIMAL(15, 2) DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    reward_points INTEGER DEFAULT 0,
    reward_points_balance INTEGER DEFAULT 0,
    autopay_enabled BOOLEAN DEFAULT FALSE,
    paperless_statements BOOLEAN DEFAULT FALSE,
    fraud_alert BOOLEAN DEFAULT FALSE,
    temporary_hold BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_accounts_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- Create indexes for frequently queried columns
CREATE INDEX idx_accounts_customer_id ON accounts(customer_id);
CREATE INDEX idx_accounts_account_number ON accounts(account_number);
CREATE INDEX idx_accounts_status ON accounts(status);
CREATE INDEX idx_accounts_account_type ON accounts(account_type);
CREATE INDEX idx_accounts_payment_due_date ON accounts(payment_due_date);
CREATE INDEX idx_accounts_fraud_alert ON accounts(fraud_alert);
CREATE INDEX idx_accounts_temporary_hold ON accounts(temporary_hold);
CREATE INDEX idx_accounts_days_delinquent ON accounts(days_delinquent);
CREATE INDEX idx_accounts_created_at ON accounts(created_at);

-- Add comments
COMMENT ON TABLE accounts IS 'Account master data containing account balances, credit limits, dates, and status information';
COMMENT ON COLUMN accounts.account_id IS 'Primary key - unique account identification number (11 numeric digits)';
COMMENT ON COLUMN accounts.status IS 'Account status: ACTIVE, INACTIVE, SUSPENDED, CLOSED, CHARGED_OFF, FRAUD, PENDING_ACTIVATION, DELINQUENT, OVERLIMIT';
