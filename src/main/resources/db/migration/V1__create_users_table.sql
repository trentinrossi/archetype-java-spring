-- Create users table for User and Security Administration
CREATE TABLE users (
    user_id VARCHAR(8) NOT NULL PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    password VARCHAR(8) NOT NULL,
    user_type VARCHAR(1) NOT NULL CHECK (user_type IN ('A', 'R')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index on user_type for filtering
CREATE INDEX idx_users_user_type ON users(user_type);

-- Create index on created_at for sorting
CREATE INDEX idx_users_created_at ON users(created_at);

-- Insert default admin user
INSERT INTO users (user_id, first_name, last_name, password, user_type) 
VALUES ('ADMIN001', 'System', 'Administrator', 'ADMIN123', 'A');

-- Insert sample regular users
INSERT INTO users (user_id, first_name, last_name, password, user_type) 
VALUES 
    ('USER0001', 'John', 'Doe', 'PASS1234', 'R'),
    ('USER0002', 'Jane', 'Smith', 'PASS5678', 'R'),
    ('USER0003', 'Bob', 'Johnson', 'PASS9012', 'R');
