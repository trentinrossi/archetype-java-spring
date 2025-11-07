-- Migration: Create users table
-- Business Rule BR001: Admin users can view all credit cards when no context is passed
-- Non-admin users can only view cards associated with their specific account

CREATE TABLE users (
    user_id VARCHAR(20) PRIMARY KEY,
    user_type VARCHAR(10) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_user_type CHECK (user_type IN ('ADMIN', 'REGULAR'))
);

-- Create indexes for performance
CREATE INDEX idx_user_id ON users(user_id);
CREATE INDEX idx_user_type ON users(user_type);

-- Add comments to table
COMMENT ON TABLE users IS 'System users with specific permissions and access rights';
COMMENT ON COLUMN users.user_id IS 'Unique user identifier (max 20 characters)';
COMMENT ON COLUMN users.user_type IS 'Type of user: ADMIN or REGULAR';
COMMENT ON COLUMN users.created_at IS 'Timestamp when the user was created';
COMMENT ON COLUMN users.updated_at IS 'Timestamp when the user was last updated';
