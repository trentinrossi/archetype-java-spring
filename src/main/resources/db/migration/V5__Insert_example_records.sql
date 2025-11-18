-- V5__Insert_example_records.sql
-- Insert example records for testing and demonstration purposes

-- Insert example customers
INSERT INTO customers (customer_id, first_name, last_name, email, phone_number, created_at, updated_at) VALUES
('CUST00000001', 'John', 'Smith', 'john.smith@example.com', '555-0101', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CUST00000002', 'Sarah', 'Johnson', 'sarah.johnson@example.com', '555-0102', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CUST00000003', 'Michael', 'Williams', 'michael.williams@example.com', '555-0103', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CUST00000004', 'Emily', 'Brown', 'emily.brown@example.com', '555-0104', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CUST00000005', 'David', 'Jones', 'david.jones@example.com', '555-0105', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert example users
INSERT INTO users (user_id, user_type, username, email, first_name, last_name, created_at, updated_at) VALUES
('USER00000001', 'ADMIN', 'admin1', 'admin1@example.com', 'Admin', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USER00000002', 'REGULAR', 'user1', 'user1@example.com', 'Regular', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USER00000003', 'ADMIN', 'admin2', 'admin2@example.com', 'Alice', 'Administrator', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USER00000004', 'REGULAR', 'user2', 'user2@example.com', 'Bob', 'Regular', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert example accounts
-- Getting customer and user IDs from the inserted records
INSERT INTO accounts (account_id, customer_id, user_id, created_at, updated_at) VALUES
(12345678901, (SELECT id FROM customers WHERE customer_id = 'CUST00000001'), (SELECT id FROM users WHERE user_id = 'USER00000001'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12345678902, (SELECT id FROM customers WHERE customer_id = 'CUST00000002'), (SELECT id FROM users WHERE user_id = 'USER00000002'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12345678903, (SELECT id FROM customers WHERE customer_id = 'CUST00000003'), (SELECT id FROM users WHERE user_id = 'USER00000003'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12345678904, (SELECT id FROM customers WHERE customer_id = 'CUST00000004'), (SELECT id FROM users WHERE user_id = 'USER00000004'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12345678905, (SELECT id FROM customers WHERE customer_id = 'CUST00000005'), (SELECT id FROM users WHERE user_id = 'USER00000001'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert example credit cards
INSERT INTO credit_cards (
    card_number, 
    account_id, 
    customer_id, 
    embossed_name, 
    expiration_date, 
    expiry_month, 
    expiry_year, 
    active_status, 
    card_status, 
    expiration_month, 
    expiration_year, 
    expiration_day, 
    cvv_code, 
    version, 
    last_modified_by, 
    created_at, 
    updated_at
) VALUES
-- John Smith's cards
('4532123456789012', 12345678901, (SELECT id FROM customers WHERE customer_id = 'CUST00000001'), 'JOHN SMITH', '2026-12-31', '12', '2026', 'Y', 'Y', 12, 2026, 31, '123', 0, 'USER00000001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('4532123456789013', 12345678901, (SELECT id FROM customers WHERE customer_id = 'CUST00000001'), 'JOHN SMITH', '2027-06-30', '06', '2027', 'Y', 'Y', 6, 2027, 30, '456', 0, 'USER00000001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Sarah Johnson's card
('4532234567890123', 12345678902, (SELECT id FROM customers WHERE customer_id = 'CUST00000002'), 'SARAH JOHNSON', '2026-03-31', '03', '2026', 'Y', 'Y', 3, 2026, 31, '789', 0, 'USER00000002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Michael Williams' card (expired)
('4532345678901234', 12345678903, (SELECT id FROM customers WHERE customer_id = 'CUST00000003'), 'MICHAEL WILLIAMS', '2024-12-31', '12', '2024', 'N', 'N', 12, 2024, 31, '234', 0, 'USER00000003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Emily Brown's cards
('4532456789012345', 12345678904, (SELECT id FROM customers WHERE customer_id = 'CUST00000004'), 'EMILY BROWN', '2027-09-30', '09', '2027', 'Y', 'Y', 9, 2027, 30, '567', 0, 'USER00000004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('4532456789012346', 12345678904, (SELECT id FROM customers WHERE customer_id = 'CUST00000004'), 'EMILY BROWN', '2028-01-31', '01', '2028', 'Y', 'Y', 1, 2028, 31, '890', 0, 'USER00000004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- David Jones' card
('4532567890123456', 12345678905, (SELECT id FROM customers WHERE customer_id = 'CUST00000005'), 'DAVID JONES', '2026-08-31', '08', '2026', 'Y', 'Y', 8, 2026, 31, '321', 0, 'USER00000001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Add comments
COMMENT ON TABLE customers IS 'Example records: 5 customers with diverse contact information';
COMMENT ON TABLE users IS 'Example records: 4 users (2 ADMIN, 2 REGULAR) for authorization testing';
COMMENT ON TABLE accounts IS 'Example records: 5 accounts linked to customers and users';
COMMENT ON TABLE credit_cards IS 'Example records: 7 credit cards with various expiration dates and statuses';
