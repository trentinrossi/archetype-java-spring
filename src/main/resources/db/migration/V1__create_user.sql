-- Create users table for User Security Profile Management
CREATE TABLE users (
    user_id VARCHAR(8) NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    password VARCHAR(8) NOT NULL,
    user_type VARCHAR(1) NOT NULL CHECK (user_type IN ('A', 'R')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_users_first_name ON users(first_name);
CREATE INDEX idx_users_last_name ON users(last_name);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Insert sample data for testing
INSERT INTO users (user_id, first_name, last_name, password, user_type, created_at, updated_at) VALUES
('ADMIN001', 'System', 'Administrator', 'admin123', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USER001', 'John', 'Doe', 'user123', 'R', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USER002', 'Jane', 'Smith', 'pass456', 'R', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADMIN002', 'Alice', 'Johnson', 'admin456', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USER003', 'Bob', 'Wilson', 'bob789', 'R', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);