-- Create USRSEC table for User Security Management
CREATE TABLE usrsec (
    user_id VARCHAR(8) NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    password VARCHAR(8) NOT NULL,
    user_type VARCHAR(1) NOT NULL CHECK (user_type IN ('A', 'U')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);

-- Create index for case-insensitive user ID lookups
CREATE INDEX idx_usrsec_user_id_upper ON usrsec (UPPER(user_id));

-- Create index for user type filtering
CREATE INDEX idx_usrsec_user_type ON usrsec (user_type);

-- Create index for name searches
CREATE INDEX idx_usrsec_names ON usrsec (first_name, last_name);

-- Insert sample data for testing
INSERT INTO usrsec (user_id, first_name, last_name, password, user_type) VALUES
('ADMIN001', 'System', 'Administrator', 'admin123', 'A'),
('USER001', 'John', 'Doe', 'user123', 'U'),
('USER002', 'Jane', 'Smith', 'pass456', 'U');

-- Add comments for documentation
COMMENT ON TABLE usrsec IS 'User Security table storing user authentication and profile information';
COMMENT ON COLUMN usrsec.user_id IS 'Unique user identifier, maximum 8 characters';
COMMENT ON COLUMN usrsec.first_name IS 'User first name, maximum 20 characters';
COMMENT ON COLUMN usrsec.last_name IS 'User last name, maximum 20 characters';
COMMENT ON COLUMN usrsec.password IS 'User password, maximum 8 characters';
COMMENT ON COLUMN usrsec.user_type IS 'User type: A for Admin, U for regular User';
COMMENT ON COLUMN usrsec.created_at IS 'Timestamp when the user record was created';
COMMENT ON COLUMN usrsec.updated_at IS 'Timestamp when the user record was last updated';