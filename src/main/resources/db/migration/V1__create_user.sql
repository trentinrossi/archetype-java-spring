-- Create USRSEC table for User Security Management
-- Based on COSGN00C and COUSR01C business rules

CREATE TABLE usrsec (
    user_id VARCHAR(8) NOT NULL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password VARCHAR(8) NOT NULL,
    user_type VARCHAR(1) NOT NULL CHECK (user_type IN ('A', 'R')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index on user_type for performance
CREATE INDEX idx_usrsec_user_type ON usrsec(user_type);

-- Create index on full name for search performance
CREATE INDEX idx_usrsec_name ON usrsec(first_name, last_name);

-- Insert sample data for testing
INSERT INTO usrsec (user_id, first_name, last_name, password, user_type) VALUES
('ADMIN001', 'System', 'Administrator', 'ADMIN123', 'A'),
('USER001', 'John', 'Doe', 'USER123', 'R'),
('USER002', 'Jane', 'Smith', 'PASS123', 'R');

-- Add comments for documentation
COMMENT ON TABLE usrsec IS 'User security table storing user credentials and access information';
COMMENT ON COLUMN usrsec.user_id IS 'Unique user identifier, max 8 characters, uppercase';
COMMENT ON COLUMN usrsec.first_name IS 'User first name';
COMMENT ON COLUMN usrsec.last_name IS 'User last name';
COMMENT ON COLUMN usrsec.password IS 'User password, max 8 characters, uppercase';
COMMENT ON COLUMN usrsec.user_type IS 'User type: A=Admin (COADM01C), R=Regular (COMEN01C)';
COMMENT ON COLUMN usrsec.created_at IS 'Timestamp when user was created';
COMMENT ON COLUMN usrsec.updated_at IS 'Timestamp when user was last updated';