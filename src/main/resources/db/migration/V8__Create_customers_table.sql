-- V8__Create_customers_table.sql

CREATE TABLE customers (
    customer_id BIGINT NOT NULL,
    cust_id BIGINT NOT NULL,
    cust_data VARCHAR(491) NOT NULL,
    ssn VARCHAR(9) NOT NULL,
    first_name VARCHAR(25) NOT NULL,
    middle_name VARCHAR(25),
    last_name VARCHAR(25) NOT NULL,
    address_line_1 VARCHAR(50) NOT NULL,
    address_line_2 VARCHAR(50),
    address_line_3 VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state_code VARCHAR(2) NOT NULL,
    country_code VARCHAR(3) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    phone_number_1 VARCHAR(15) NOT NULL,
    phone_number_2 VARCHAR(15),
    date_of_birth DATE NOT NULL,
    government_issued_id VARCHAR(20),
    government_id VARCHAR(20),
    eft_account_id VARCHAR(10),
    primary_holder_indicator VARCHAR(1) NOT NULL,
    primary_card_holder_indicator VARCHAR(1) NOT NULL,
    fico_score INTEGER NOT NULL,
    customer_number BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (customer_id)
);

CREATE INDEX idx_customers_cust_id ON customers(cust_id);
CREATE INDEX idx_customers_ssn ON customers(ssn);
CREATE INDEX idx_customers_last_name ON customers(last_name);
