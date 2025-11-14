-- V1__Create_customers_table.sql
-- Migration script for creating customers table
-- Supports H2 and PostgreSQL syntax

CREATE TABLE customers (
    customer_id BIGINT NOT NULL,
    first_name VARCHAR(25) NOT NULL,
    middle_name VARCHAR(25),
    last_name VARCHAR(25) NOT NULL,
    address_line_1 VARCHAR(50) NOT NULL,
    address_line_2 VARCHAR(50),
    address_line_3 VARCHAR(50) NOT NULL,
    state_code VARCHAR(2) NOT NULL,
    country_code VARCHAR(3) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    phone_number_1 VARCHAR(15) NOT NULL,
    phone_number_2 VARCHAR(15),
    ssn BIGINT NOT NULL,
    government_issued_id VARCHAR(20),
    date_of_birth DATE NOT NULL,
    eft_account_id VARCHAR(10),
    primary_cardholder_indicator VARCHAR(1) NOT NULL,
    fico_score INTEGER NOT NULL,
    fico_credit_score INTEGER,
    city VARCHAR(50) NOT NULL,
    primary_phone_number VARCHAR(13) NOT NULL,
    secondary_phone_number VARCHAR(13),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (customer_id)
);

-- Indexes for performance optimization
CREATE INDEX idx_customers_ssn ON customers(ssn);
CREATE INDEX idx_customers_last_name ON customers(last_name);
CREATE INDEX idx_customers_first_name ON customers(first_name);
CREATE INDEX idx_customers_date_of_birth ON customers(date_of_birth);
CREATE INDEX idx_customers_state_code ON customers(state_code);
CREATE INDEX idx_customers_country_code ON customers(country_code);
CREATE INDEX idx_customers_zip_code ON customers(zip_code);
CREATE INDEX idx_customers_city ON customers(city);
CREATE INDEX idx_customers_phone_number_1 ON customers(phone_number_1);
CREATE INDEX idx_customers_primary_phone_number ON customers(primary_phone_number);
CREATE INDEX idx_customers_fico_score ON customers(fico_score);
CREATE INDEX idx_customers_primary_cardholder_indicator ON customers(primary_cardholder_indicator);

-- Unique constraint on SSN
CREATE UNIQUE INDEX idx_customers_ssn_unique ON customers(ssn);

-- Comments for documentation
COMMENT ON TABLE customers IS 'Customer information for credit card holders';
COMMENT ON COLUMN customers.customer_id IS 'Customer Identification Number - Primary key (9 digits)';
COMMENT ON COLUMN customers.ssn IS 'Customer Social Security Number stored without hyphens (9 digits)';
COMMENT ON COLUMN customers.fico_score IS 'Customer FICO Credit Score - Range: 300-850';
COMMENT ON COLUMN customers.primary_cardholder_indicator IS 'Primary Cardholder Indicator - Y for primary, N for secondary';
COMMENT ON COLUMN customers.date_of_birth IS 'Customer Date of Birth - Must be at least 18 years old';
