-- V4__Create_merchants_table.sql
-- Merchant table for merchant information

CREATE TABLE merchants (
    merchant_id VARCHAR(9) NOT NULL,
    merchant_name VARCHAR(30) NOT NULL,
    merchant_city VARCHAR(25) NOT NULL,
    merchant_zip VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (merchant_id)
);

CREATE INDEX idx_merchant_name ON merchants(merchant_name);
CREATE INDEX idx_merchant_city ON merchants(merchant_city);
CREATE INDEX idx_merchant_zip ON merchants(merchant_zip);
