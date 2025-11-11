-- V4__Create_user_account_access_table.sql
-- Create user_account_access junction table for many-to-many relationship
-- Business Rules: BR001

CREATE TABLE user_account_access (
    user_id VARCHAR(20) NOT NULL,
    account_id VARCHAR(11) NOT NULL,
    granted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, account_id),
    CONSTRAINT fk_user_account_access_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_account_access_account FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_user_account_access_user_id ON user_account_access(user_id);
CREATE INDEX idx_user_account_access_account_id ON user_account_access(account_id);
CREATE INDEX idx_user_account_access_granted_at ON user_account_access(granted_at);

-- Comments
COMMENT ON TABLE user_account_access IS 'Junction table for user-account access control (BR001)';
COMMENT ON COLUMN user_account_access.user_id IS 'User ID with access to the account';
COMMENT ON COLUMN user_account_access.account_id IS 'Account ID accessible by the user';
COMMENT ON COLUMN user_account_access.granted_at IS 'Timestamp when access was granted';
