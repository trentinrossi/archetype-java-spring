-- Insert sample accounts
INSERT INTO accounts (account_id, created_at, updated_at) VALUES
('12345678901', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('12345678902', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('12345678903', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample users
INSERT INTO users (username, user_type, account_id, email, first_name, last_name, active, created_at, updated_at) VALUES
('admin', 'ADMIN', NULL, 'admin@example.com', 'Admin', 'User', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('john.doe', 'REGULAR', '12345678901', 'john.doe@example.com', 'John', 'Doe', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jane.smith', 'REGULAR', '12345678902', 'jane.smith@example.com', 'Jane', 'Smith', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('bob.wilson', 'REGULAR', '12345678903', 'bob.wilson@example.com', 'Bob', 'Wilson', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample credit cards
INSERT INTO credit_cards (card_number, account_id, card_status, cardholder_name, expiry_month, expiry_year, card_type, credit_limit, available_credit, created_at, updated_at) VALUES
('1234567890123456', '12345678901', 'A', 'John Doe', '12', '2025', 'VISA', 5000.00, 4500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('1234567890123457', '12345678901', 'A', 'John Doe', '06', '2026', 'MASTERCARD', 3000.00, 3000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2345678901234567', '12345678902', 'A', 'Jane Smith', '09', '2025', 'VISA', 10000.00, 8500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('2345678901234568', '12345678902', 'I', 'Jane Smith', '03', '2024', 'AMEX', 7500.00, 7500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('3456789012345678', '12345678903', 'A', 'Bob Wilson', '11', '2026', 'VISA', 15000.00, 12000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('3456789012345679', '12345678903', 'B', 'Bob Wilson', '08', '2025', 'MASTERCARD', 5000.00, 4000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Add comments
COMMENT ON TABLE accounts IS 'Sample data: 3 customer accounts';
COMMENT ON TABLE users IS 'Sample data: 1 admin user and 3 regular users';
COMMENT ON TABLE credit_cards IS 'Sample data: 6 credit cards with various statuses';
