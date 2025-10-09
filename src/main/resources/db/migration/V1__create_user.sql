-- User Security Administration Table
-- Based on COBOL USRSEC file structure from CardDemo application

CREATE TABLE usrsec (
    user_id VARCHAR(8) NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    password VARCHAR(8) NOT NULL,
    user_type VARCHAR(1) NOT NULL CHECK (user_type IN ('A', 'R')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);

-- Create indexes for better performance
CREATE INDEX idx_usrsec_user_type ON usrsec(user_type);
CREATE INDEX idx_usrsec_last_name ON usrsec(last_name);
CREATE INDEX idx_usrsec_created_at ON usrsec(created_at);

-- Insert sample data for testing
INSERT INTO usrsec (user_id, first_name, last_name, password, user_type) VALUES
('ADMIN001', 'System', 'Administrator', 'admin123', 'A'),
('USER001', 'John', 'Doe', 'user123', 'R'),
('USER002', 'Jane', 'Smith', 'pass456', 'R'),
('ADMIN002', 'Mary', 'Johnson', 'admin456', 'A'),
('USER003', 'Bob', 'Wilson', 'bob789', 'R');

-- Comments for documentation
COMMENT ON TABLE usrsec IS 'User Security table for CardDemo application - stores user authentication and authorization data';
COMMENT ON COLUMN usrsec.user_id IS 'Unique user identifier, max 8 characters';
COMMENT ON COLUMN usrsec.first_name IS 'User first name, max 20 characters';
COMMENT ON COLUMN usrsec.last_name IS 'User last name, max 20 characters';
COMMENT ON COLUMN usrsec.password IS 'User password, max 8 characters (should be encrypted in production)';
COMMENT ON COLUMN usrsec.user_type IS 'User type: A=Admin, R=Regular';
COMMENT ON COLUMN usrsec.created_at IS 'Timestamp when user was created';
COMMENT ON COLUMN usrsec.updated_at IS 'Timestamp when user was last updated';