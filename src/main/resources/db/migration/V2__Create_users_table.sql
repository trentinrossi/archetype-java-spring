-- Create users table
-- User type determines permissions (ADMIN can view all, REGULAR restricted to account)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    user_type VARCHAR(10) NOT NULL,
    account_id VARCHAR(11),
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_user_type CHECK (user_type IN ('ADMIN', 'REGULAR')),
    CONSTRAINT chk_account_id_format CHECK (account_id IS NULL OR account_id ~ '^[0-9]{11}$'),
    CONSTRAINT fk_users_account FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE SET NULL
);

-- Create indexes for faster lookups
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_users_account_id ON users(account_id);
CREATE INDEX idx_users_active ON users(active);

-- Add comments to table
COMMENT ON TABLE users IS 'System users with specific permissions and access rights';
COMMENT ON COLUMN users.user_type IS 'Type of user: ADMIN (can view all cards) or REGULAR (restricted to account)';
COMMENT ON COLUMN users.account_id IS 'Associated account ID for regular users (11 digits)';
