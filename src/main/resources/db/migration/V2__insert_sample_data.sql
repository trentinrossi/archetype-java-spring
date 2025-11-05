-- Sample data for Card Transaction Lifecycle Management System
-- This migration provides test data for development and testing purposes

-- Insert sample customers
INSERT INTO customers (customer_id, first_name, last_name, email, phone, address, city, state, zip_code, created_at, updated_at)
VALUES 
    ('000000001', 'John', 'Doe', 'john.doe@example.com', '555-0101', '123 Main St', 'New York', 'NY', '10001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('000000002', 'Jane', 'Smith', 'jane.smith@example.com', '555-0102', '456 Oak Ave', 'Los Angeles', 'CA', '90001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('000000003', 'Robert', 'Johnson', 'robert.j@example.com', '555-0103', '789 Pine Rd', 'Chicago', 'IL', '60601', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample accounts
INSERT INTO accounts (account_id, customer_id, current_balance, credit_limit, current_cycle_credit, current_cycle_debit, expiration_date, account_status, created_at, updated_at)
VALUES 
    ('00000000001', '000000001', 1500.00, 10000.00, 2500.00, 1000.00, '2025-12-31', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('00000000002', '000000002', 3200.50, 15000.00, 5000.00, 1800.00, '2026-06-30', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('00000000003', '000000003', 500.00, 5000.00, 1000.00, 500.00, '2025-09-30', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample cards
INSERT INTO cards (card_number, account_id, customer_id, card_status, expiration_date, card_type, created_at, updated_at)
VALUES 
    ('4532123456789012', '00000000001', '000000001', 'ACTIVE', '2025-12-31', 'VISA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('5412345678901234', '00000000002', '000000002', 'ACTIVE', '2026-06-30', 'MASTERCARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('3782822463100005', '00000000003', '000000003', 'ACTIVE', '2025-09-30', 'AMEX', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert card cross-references
INSERT INTO card_cross_reference (card_number, account_id, customer_id, created_at, updated_at)
VALUES 
    ('4532123456789012', '00000000001', '000000001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('5412345678901234', '00000000002', '000000002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('3782822463100005', '00000000003', '000000003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample transactions
INSERT INTO transactions (transaction_id, card_number, account_id, transaction_type_code, transaction_category_code, transaction_source, transaction_description, transaction_amount, merchant_id, merchant_name, merchant_city, merchant_zip, original_timestamp, processing_timestamp, created_at, updated_at)
VALUES 
    ('0000000000000001', '4532123456789012', '00000000001', '01', 5411, 'POS', 'Grocery Store Purchase', 125.50, 123456789, 'SuperMart', 'New York', '10001', '2024-01-15 10:30:00', '2024-01-15 10:31:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('0000000000000002', '4532123456789012', '00000000001', '01', 5812, 'ONLINE', 'Restaurant Payment', 85.75, 234567890, 'Fine Dining', 'New York', '10002', '2024-01-16 19:45:00', '2024-01-16 19:46:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('0000000000000003', '5412345678901234', '00000000002', '02', 5999, 'ATM', 'Cash Withdrawal', -200.00, 345678901, 'City Bank ATM', 'Los Angeles', '90001', '2024-01-17 14:20:00', '2024-01-17 14:21:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('0000000000000004', '5412345678901234', '00000000002', '01', 5311, 'POS', 'Department Store', 450.25, 456789012, 'Fashion Plaza', 'Los Angeles', '90002', '2024-01-18 16:10:00', '2024-01-18 16:11:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('0000000000000005', '3782822463100005', '00000000003', '01', 5541, 'ONLINE', 'Gas Station', 65.00, 567890123, 'QuickFuel', 'Chicago', '60601', '2024-01-19 08:15:00', '2024-01-19 08:16:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert transaction category balances
INSERT INTO transaction_category_balance (account_id, transaction_type_code, transaction_category_code, category_balance, created_at, updated_at)
VALUES 
    ('00000000001', '01', 5411, 125.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('00000000001', '01', 5812, 85.75, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('00000000002', '02', 5999, -200.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('00000000002', '01', 5311, 450.25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('00000000003', '01', 5541, 65.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Add comments for reference
COMMENT ON TABLE customers IS 'Sample customer data for testing';
COMMENT ON TABLE accounts IS 'Sample account data with various balances and limits';
COMMENT ON TABLE cards IS 'Sample credit cards linked to accounts';
COMMENT ON TABLE transactions IS 'Sample transaction history for testing reports';
