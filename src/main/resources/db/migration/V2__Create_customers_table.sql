-- V2__Create_customers_table.sql
-- Create customers table for customer management

CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    customer_id VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add comments to table
COMMENT ON TABLE customers IS 'Customers who own accounts and credit cards';

-- Add comments to columns
COMMENT ON COLUMN customers.id IS 'Internal customer ID (auto-generated)';
COMMENT ON COLUMN customers.customer_id IS 'Unique customer identifier';
COMMENT ON COLUMN customers.first_name IS 'Customer first name';
COMMENT ON COLUMN customers.last_name IS 'Customer last name';
COMMENT ON COLUMN customers.email IS 'Customer email address';
COMMENT ON COLUMN customers.phone_number IS 'Customer phone number';
COMMENT ON COLUMN customers.created_at IS 'Timestamp when the customer was created';
COMMENT ON COLUMN customers.updated_at IS 'Timestamp when the customer was last updated';

-- Create indexes for faster lookups
CREATE INDEX idx_customers_customer_id ON customers(customer_id);
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_name ON customers(first_name, last_name);

-- Add customer_id foreign key to accounts table
ALTER TABLE accounts ADD COLUMN customer_id BIGINT;
ALTER TABLE accounts ADD CONSTRAINT fk_accounts_customer FOREIGN KEY (customer_id) REFERENCES customers(id);
CREATE INDEX idx_accounts_customer_id ON accounts(customer_id);
