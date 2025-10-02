-- Create USRSEC table for User Security Management
-- This table stores user information for authentication and authorization
-- Based on the COBOL USRSEC file structure from the CardDemo application

CREATE TABLE usrsec (
    user_id VARCHAR(8) NOT NULL PRIMARY KEY COMMENT 'Unique user identifier, max 8 characters',
    first_name VARCHAR(20) NOT NULL COMMENT 'User first name, max 20 characters',
    last_name VARCHAR(20) NOT NULL COMMENT 'User last name, max 20 characters',
    password VARCHAR(8) NOT NULL COMMENT 'User password, max 8 characters',
    user_type VARCHAR(1) NOT NULL COMMENT 'User type: A=Admin, R=Regular',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record last update timestamp'
);

-- Add constraints for user_type validation
ALTER TABLE usrsec ADD CONSTRAINT chk_user_type CHECK (user_type IN ('A', 'R'));

-- Create indexes for better performance
CREATE INDEX idx_usrsec_user_type ON usrsec(user_type);
CREATE INDEX idx_usrsec_created_at ON usrsec(created_at);

-- Insert default admin user for initial setup
INSERT INTO usrsec (user_id, first_name, last_name, password, user_type) 
VALUES ('ADMIN001', 'System', 'Administrator', 'admin123', 'A');

-- Insert default regular user for testing
INSERT INTO usrsec (user_id, first_name, last_name, password, user_type) 
VALUES ('USER001', 'Test', 'User', 'user123', 'R');