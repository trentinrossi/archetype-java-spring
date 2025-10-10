-- Migration script for User Management System
-- Creates the users table based on COBOL USRSEC file structure

CREATE TABLE users (
    user_id VARCHAR(8) NOT NULL COMMENT 'User ID - Primary key, max 8 characters',
    first_name VARCHAR(20) NOT NULL COMMENT 'User first name, max 20 characters',
    last_name VARCHAR(20) NOT NULL COMMENT 'User last name, max 20 characters',
    password VARCHAR(8) NOT NULL COMMENT 'User password, max 8 characters',
    user_type VARCHAR(1) NOT NULL COMMENT 'User type: A=Admin, U=User',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record last update timestamp',
    PRIMARY KEY (user_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_users_first_name ON users(first_name);
CREATE INDEX idx_users_last_name ON users(last_name);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Insert sample data for testing (matching COBOL program expectations)
INSERT INTO users (user_id, first_name, last_name, password, user_type) VALUES
('USR00001', 'John', 'Doe', 'pass123', 'A'),
('USR00002', 'Jane', 'Smith', 'pass456', 'U'),
('USR00003', 'Bob', 'Johnson', 'pass789', 'U'),
('ADMIN001', 'Admin', 'User', 'admin123', 'A'),
('TEST0001', 'Test', 'User', 'test123', 'U');

-- Add constraints to ensure data integrity
ALTER TABLE users ADD CONSTRAINT chk_user_type CHECK (user_type IN ('A', 'U'));
ALTER TABLE users ADD CONSTRAINT chk_user_id_length CHECK (LENGTH(user_id) <= 8);
ALTER TABLE users ADD CONSTRAINT chk_first_name_length CHECK (LENGTH(first_name) <= 20);
ALTER TABLE users ADD CONSTRAINT chk_last_name_length CHECK (LENGTH(last_name) <= 20);
ALTER TABLE users ADD CONSTRAINT chk_password_length CHECK (LENGTH(password) <= 8);