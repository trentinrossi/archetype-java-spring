-- Create transactions table for Transaction Management
CREATE TABLE transactions (
    transaction_id VARCHAR(16) NOT NULL PRIMARY KEY,
    transaction_date TIMESTAMP NOT NULL,
    transaction_description VARCHAR(26),
    transaction_amount DECIMAL(12, 2) NOT NULL,
    user_id VARCHAR(8),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Create index on transaction_date for date range queries
CREATE INDEX idx_transactions_date ON transactions(transaction_date);

-- Create index on user_id for user-specific queries
CREATE INDEX idx_transactions_user_id ON transactions(user_id);

-- Create index on transaction_id for pagination
CREATE INDEX idx_transactions_id ON transactions(transaction_id);

-- Insert sample transactions
INSERT INTO transactions (transaction_id, transaction_date, transaction_description, transaction_amount, user_id) 
VALUES 
    ('TXN0000000000001', '2024-01-15 10:30:00', 'Purchase at Store A', 125.50, 'USER0001'),
    ('TXN0000000000002', '2024-01-16 14:20:00', 'Online Payment', 89.99, 'USER0001'),
    ('TXN0000000000003', '2024-01-17 09:15:00', 'ATM Withdrawal', 200.00, 'USER0002'),
    ('TXN0000000000004', '2024-01-18 16:45:00', 'Restaurant Bill', 67.25, 'USER0002'),
    ('TXN0000000000005', '2024-01-19 11:30:00', 'Gas Station', 45.00, 'USER0003'),
    ('TXN0000000000006', '2024-02-01 13:00:00', 'Grocery Shopping', 156.75, 'USER0001'),
    ('TXN0000000000007', '2024-02-05 10:20:00', 'Movie Tickets', 32.00, 'USER0003'),
    ('TXN0000000000008', '2024-02-10 15:30:00', 'Pharmacy', 28.50, 'USER0002'),
    ('TXN0000000000009', '2024-02-15 12:45:00', 'Coffee Shop', 15.75, 'USER0001'),
    ('TXN0000000000010', '2024-02-20 09:00:00', 'Book Store', 42.99, 'USER0003');
