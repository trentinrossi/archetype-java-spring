-- Create customers table
CREATE TABLE customers (
    customer_id BIGINT NOT NULL,
    customer_data VARCHAR(491) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (customer_id)
);

-- Create index on customer_id for faster lookups
CREATE INDEX idx_customers_customer_id ON customers(customer_id);

-- Add comment to table
COMMENT ON TABLE customers IS 'Customer master data including demographic information, addresses, phone numbers, SSN, and credit scores';
