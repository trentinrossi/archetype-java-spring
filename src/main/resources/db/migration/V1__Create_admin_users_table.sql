-- Create admin_users table
CREATE TABLE admin_users (
    user_id VARCHAR(8) NOT NULL,
    authentication_status BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);

-- Create index on authentication_status for faster queries
CREATE INDEX idx_admin_users_authentication_status ON admin_users(authentication_status);
