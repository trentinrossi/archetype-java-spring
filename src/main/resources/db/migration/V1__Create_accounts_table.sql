-- V1__Create_accounts_table.sql
-- Migration script for creating the accounts table
-- Implements the Account entity structure with all required fields and constraints

CREATE TABLE accounts (
    -- Primary key: 11-digit account identifier
    acct_id BIGINT PRIMARY KEY,
    
    -- Account status: 'A' for active, 'I' for inactive
    acct_active_status VARCHAR(1) NOT NULL CHECK (acct_active_status IN ('A', 'I')),
    
    -- Financial fields with precision for currency
    acct_curr_bal NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    acct_credit_limit NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    acct_cash_credit_limit NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    
    -- Important dates
    acct_open_date DATE NOT NULL,
    acct_expiration_date DATE NOT NULL,
    acct_reissue_date DATE,
    
    -- Current cycle amounts
    acct_curr_cyc_credit NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    acct_curr_cyc_debit NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    
    -- Optional group identifier
    acct_group_id VARCHAR(50),
    
    -- Audit timestamps
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_acct_id_length CHECK (acct_id >= 10000000000 AND acct_id <= 99999999999),
    CONSTRAINT chk_expiration_after_open CHECK (acct_expiration_date > acct_open_date),
    CONSTRAINT chk_reissue_after_open CHECK (acct_reissue_date IS NULL OR acct_reissue_date >= acct_open_date),
    CONSTRAINT chk_cash_limit_not_exceed_credit CHECK (acct_cash_credit_limit <= acct_credit_limit),
    CONSTRAINT chk_positive_balances CHECK (
        acct_curr_bal >= 0 AND 
        acct_credit_limit >= 0 AND 
        acct_cash_credit_limit >= 0 AND
        acct_curr_cyc_credit >= 0 AND
        acct_curr_cyc_debit >= 0
    )
);

-- Create indexes for common query patterns
CREATE INDEX idx_accounts_active_status ON accounts(acct_active_status);
CREATE INDEX idx_accounts_group_id ON accounts(acct_group_id) WHERE acct_group_id IS NOT NULL;
CREATE INDEX idx_accounts_expiration_date ON accounts(acct_expiration_date);
CREATE INDEX idx_accounts_open_date ON accounts(acct_open_date);
CREATE INDEX idx_accounts_created_at ON accounts(created_at);

-- Create index for sequential processing (BR-001)
CREATE INDEX idx_accounts_sequential ON accounts(acct_id ASC);

-- Create index for accounts over credit limit queries
CREATE INDEX idx_accounts_balance_limit ON accounts(acct_curr_bal, acct_credit_limit);

-- Add comments for documentation
COMMENT ON TABLE accounts IS 'Customer accounts with financial and status information';
COMMENT ON COLUMN accounts.acct_id IS 'Unique 11-digit account identifier';
COMMENT ON COLUMN accounts.acct_active_status IS 'Account status: A=Active, I=Inactive';
COMMENT ON COLUMN accounts.acct_curr_bal IS 'Current account balance';
COMMENT ON COLUMN accounts.acct_credit_limit IS 'Maximum credit limit';
COMMENT ON COLUMN accounts.acct_cash_credit_limit IS 'Maximum cash credit limit';
COMMENT ON COLUMN accounts.acct_open_date IS 'Date when account was opened';
COMMENT ON COLUMN accounts.acct_expiration_date IS 'Date when account expires';
COMMENT ON COLUMN accounts.acct_reissue_date IS 'Date when account was reissued (optional)';
COMMENT ON COLUMN accounts.acct_curr_cyc_credit IS 'Current cycle credit amount';
COMMENT ON COLUMN accounts.acct_curr_cyc_debit IS 'Current cycle debit amount';
COMMENT ON COLUMN accounts.acct_group_id IS 'Account group identifier for categorization';
COMMENT ON COLUMN accounts.created_at IS 'Timestamp when record was created';
COMMENT ON COLUMN accounts.updated_at IS 'Timestamp when record was last updated';

-- Create function to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_accounts_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to call the function before updates
CREATE TRIGGER trigger_update_accounts_updated_at
    BEFORE UPDATE ON accounts
    FOR EACH ROW
    EXECUTE FUNCTION update_accounts_updated_at();
