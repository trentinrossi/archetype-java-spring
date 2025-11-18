-- V3__Create_users_table.sql
-- Create users table for system user management with authorization controls

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(20) UNIQUE NOT NULL,
    user_type VARCHAR(10) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add comments to table
COMMENT ON TABLE users IS 'System users with specific access privileges for card viewing';

-- Add comments to columns
COMMENT ON COLUMN users.id IS 'Internal user ID (auto-generated)';
COMMENT ON COLUMN users.user_id IS 'Unique user identifier';
COMMENT ON COLUMN users.user_type IS 'Type of user (ADMIN or REGULAR) - controls card viewing authorization';
COMMENT ON COLUMN users.username IS 'Username for login';
COMMENT ON COLUMN users.email IS 'User email address';
COMMENT ON COLUMN users.first_name IS 'User first name';
COMMENT ON COLUMN users.last_name IS 'User last name';
COMMENT ON COLUMN users.created_at IS 'Timestamp when the user was created';
COMMENT ON COLUMN users.updated_at IS 'Timestamp when the user was last updated';

-- Create indexes for faster lookups
CREATE INDEX idx_users_user_id ON users(user_id);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_type ON users(user_type);

-- Add user_id foreign key to accounts table
ALTER TABLE accounts ADD COLUMN user_id BIGINT;
ALTER TABLE accounts ADD CONSTRAINT fk_accounts_user FOREIGN KEY (user_id) REFERENCES users(id);
CREATE INDEX idx_accounts_user_id ON accounts(user_id);
