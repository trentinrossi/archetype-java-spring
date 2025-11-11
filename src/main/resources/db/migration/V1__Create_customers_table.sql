-- V1__Create_customers_table.sql
-- Create customers table for card account transaction management system

CREATE TABLE customers (
    customer_id BIGINT PRIMARY KEY,
    first_name VARCHAR(25) NOT NULL,
    middle_name VARCHAR(25),
    last_name VARCHAR(25) NOT NULL,
    address_line_1 VARCHAR(50) NOT NULL,
    address_line_2 VARCHAR(50),
    address_line_3 VARCHAR(50),
    state_code VARCHAR(2) NOT NULL,
    country_code VARCHAR(3) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    fico_credit_score INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_customer_id CHECK (customer_id >= 100000000 AND customer_id <= 999999999),
    CONSTRAINT chk_fico_score CHECK (fico_credit_score >= 0 AND fico_credit_score <= 999)
);

CREATE INDEX idx_customers_customer_id ON customers(customer_id);
CREATE INDEX idx_customers_state_code ON customers(state_code);
CREATE INDEX idx_customers_country_code ON customers(country_code);
CREATE INDEX idx_customers_fico_score ON customers(fico_credit_score);
