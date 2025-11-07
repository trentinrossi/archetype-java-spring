-- Migration: Create user_account_access junction table
-- Business Rule BR001: Non-admin users can only view cards associated with their specific account
-- This table manages the many-to-many relationship between users and accounts

CREATE TABLE user_account_access (
    user_id VARCHAR(20) NOT NULL,
    account_id VARCHAR(11) NOT NULL,
    PRIMARY KEY (user_id, account_id),
    CONSTRAINT fk_user_account_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_account_account FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- Create indexes for performance
CREATE INDEX idx_user_account_user_id ON user_account_access(user_id);
CREATE INDEX idx_user_account_account_id ON user_account_access(account_id);

-- Add comments to table
COMMENT ON TABLE user_account_access IS 'Junction table managing user access to accounts';
COMMENT ON COLUMN user_account_access.user_id IS 'User identifier';
COMMENT ON COLUMN user_account_access.account_id IS 'Account identifier the user has access to';
