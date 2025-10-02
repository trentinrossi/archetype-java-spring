CREATE TABLE user_security (
    user_id VARCHAR(8) NOT NULL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password VARCHAR(8) NOT NULL,
    user_type VARCHAR(1) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add check constraint for user_type
ALTER TABLE user_security ADD CONSTRAINT chk_user_type CHECK (user_type IN ('A', 'R'));

-- Create index for case-insensitive user_id lookups
CREATE INDEX idx_user_security_user_id_upper ON user_security (UPPER(user_id));

-- Create index for user_type for filtering
CREATE INDEX idx_user_security_user_type ON user_security (user_type);

-- Create index for name searches
CREATE INDEX idx_user_security_names ON user_security (first_name, last_name);

-- Insert sample data for testing
INSERT INTO user_security (user_id, first_name, last_name, password, user_type) VALUES
('ADMIN001', 'System', 'Administrator', 'admin123', 'A'),
('USER001', 'John', 'Doe', 'user123', 'R'),
('USER002', 'Jane', 'Smith', 'pass123', 'R');