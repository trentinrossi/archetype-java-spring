-- Create USRSEC table for User Security Administration
CREATE TABLE usrsec (
    user_id VARCHAR(8) NOT NULL PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    password VARCHAR(8) NOT NULL,
    user_type VARCHAR(1) NOT NULL CHECK (user_type IN ('A', 'R')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_usrsec_user_type ON usrsec(user_type);
CREATE INDEX idx_usrsec_name ON usrsec(last_name, first_name);
CREATE INDEX idx_usrsec_created_at ON usrsec(created_at);

-- Insert initial admin user
INSERT INTO usrsec (user_id, first_name, last_name, password, user_type, created_at, updated_at) 
VALUES ('ADMIN001', 'System', 'Administrator', 'ADMIN123', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample regular user
INSERT INTO usrsec (user_id, first_name, last_name, password, user_type, created_at, updated_at) 
VALUES ('USER001', 'John', 'Doe', 'USER123', 'R', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);