-- Create accounts table
CREATE TABLE accounts (
    account_id BIGINT NOT NULL,
    account_data VARCHAR(289) NOT NULL,
    customer_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id),
    CONSTRAINT fk_accounts_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- Create indexes for faster lookups
CREATE INDEX idx_accounts_customer_id ON accounts(customer_id);
CREATE INDEX idx_accounts_account_id ON accounts(account_id);

-- Add comment to table
COMMENT ON TABLE accounts IS 'Account master data containing account balances, credit limits, dates, and status information';
