-- V003__Create_customers_table.sql

CREATE TABLE customers (
    customer_id VARCHAR(9) PRIMARY KEY,
    ssn VARCHAR(9) NOT NULL,
    first_name VARCHAR(25) NOT NULL,
    middle_name VARCHAR(25),
    last_name VARCHAR(25) NOT NULL,
    address_line_1 VARCHAR(50) NOT NULL,
    address_line_2 VARCHAR(50),
    city VARCHAR(50) NOT NULL,
    state_code VARCHAR(2) NOT NULL,
    country_code VARCHAR(3) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    phone_number_1 VARCHAR(15) NOT NULL,
    phone_number_2 VARCHAR(15),
    date_of_birth VARCHAR(8) NOT NULL,
    government_id VARCHAR(20),
    eft_account_id VARCHAR(10),
    primary_card_holder_indicator VARCHAR(1) NOT NULL,
    fico_credit_score INT NOT NULL,
    customer_data VARCHAR(491),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_customers_ssn ON customers(ssn);
CREATE INDEX idx_customers_last_name ON customers(last_name);
CREATE INDEX idx_customers_state_code ON customers(state_code);
CREATE INDEX idx_customers_city ON customers(city);
