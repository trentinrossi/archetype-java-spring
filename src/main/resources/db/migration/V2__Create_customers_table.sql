-- V2__Create_customers_table.sql
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    customer_id VARCHAR(9) NOT NULL UNIQUE,
    customer_data VARCHAR(491) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_customers_customer_id ON customers(customer_id);

COMMENT ON TABLE customers IS 'Customer master data including demographic information, addresses, phone numbers, SSN, and credit scores';
COMMENT ON COLUMN customers.customer_id IS 'Primary key - unique customer identification number in numeric format (9 characters)';
COMMENT ON COLUMN customers.customer_data IS 'Complete customer profile including demographics, addresses, contact information, SSN, and credit scores';