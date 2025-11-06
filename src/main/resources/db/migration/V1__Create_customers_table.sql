-- Create customers table
CREATE TABLE customers (
    customer_id VARCHAR(9) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    middle_initial VARCHAR(1),
    last_name VARCHAR(50) NOT NULL,
    address_line_1 VARCHAR(100),
    address_line_2 VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(2),
    zip_code VARCHAR(10),
    country VARCHAR(50),
    home_phone VARCHAR(20),
    work_phone VARCHAR(20),
    mobile_phone VARCHAR(20),
    email VARCHAR(100),
    ssn VARCHAR(11),
    date_of_birth DATE,
    credit_score INTEGER,
    fico_score INTEGER,
    government_id VARCHAR(50),
    government_id_type VARCHAR(20),
    customer_since DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    vip_status BOOLEAN DEFAULT FALSE,
    preferred_contact_method VARCHAR(20),
    occupation VARCHAR(100),
    annual_income DECIMAL(15, 2),
    employer_name VARCHAR(100),
    years_at_employer INTEGER,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for frequently queried columns
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_ssn ON customers(ssn);
CREATE INDEX idx_customers_status ON customers(status);
CREATE INDEX idx_customers_state ON customers(state);
CREATE INDEX idx_customers_vip_status ON customers(vip_status);
CREATE INDEX idx_customers_credit_score ON customers(credit_score);
CREATE INDEX idx_customers_created_at ON customers(created_at);

-- Add comments
COMMENT ON TABLE customers IS 'Customer master data including demographic information, addresses, phone numbers, SSN, and credit scores';
COMMENT ON COLUMN customers.customer_id IS 'Primary key - unique customer identification number in numeric format (9 characters)';
COMMENT ON COLUMN customers.status IS 'Customer status: ACTIVE, INACTIVE, SUSPENDED, PENDING, CLOSED, DECEASED, FRAUD';
