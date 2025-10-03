-- Create USRSEC table for User Security Management
CREATE TABLE usrsec (
    user_id VARCHAR(8) NOT NULL PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    password VARCHAR(8) NOT NULL,
    user_type VARCHAR(1) NOT NULL CHECK (user_type IN ('A', 'R')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index on user_type for performance
CREATE INDEX idx_usrsec_user_type ON usrsec(user_type);

-- Create index on full name for search functionality
CREATE INDEX idx_usrsec_name ON usrsec(first_name, last_name);

-- Insert sample admin user
INSERT INTO usrsec (user_id, first_name, last_name, password, user_type, created_at, updated_at) 
VALUES ('ADMIN001', 'System', 'Administrator', 'admin123', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample regular user
INSERT INTO usrsec (user_id, first_name, last_name, password, user_type, created_at, updated_at) 
VALUES ('USER001', 'John', 'Doe', 'user123', 'R', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);