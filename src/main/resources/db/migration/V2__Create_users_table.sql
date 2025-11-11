-- V2__Create_users_table.sql
-- Create users table for system user management
-- Business Rules: BR001

CREATE TABLE users (
    user_id VARCHAR(20) NOT NULL,
    user_type VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    CONSTRAINT chk_user_type CHECK (user_type IN ('ADMIN', 'REGULAR'))
);

-- Indexes for performance
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_users_created_at ON users(created_at);
CREATE INDEX idx_users_updated_at ON users(updated_at);

-- Comments
COMMENT ON TABLE users IS 'System users with specific permissions and access rights';
COMMENT ON COLUMN users.user_id IS 'Unique alphanumeric user identifier (max 20 characters)';
COMMENT ON COLUMN users.user_type IS 'Type of user: ADMIN or REGULAR (BR001)';
COMMENT ON COLUMN users.created_at IS 'Timestamp when the user was created';
COMMENT ON COLUMN users.updated_at IS 'Timestamp when the user was last updated';
