-- V2__Create_user_sessions_table.sql
-- Create user_sessions table for tracking user session information and program navigation

CREATE TABLE user_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id VARCHAR(4) NOT NULL,
    program_name VARCHAR(8) NOT NULL,
    from_program VARCHAR(8),
    from_transaction VARCHAR(4),
    program_context BIGINT,
    reenter_flag BOOLEAN NOT NULL,
    to_program VARCHAR(8) NOT NULL,
    program_reenter_flag VARCHAR(1) NOT NULL,
    user_type VARCHAR(1) NOT NULL,
    from_transaction_id VARCHAR(4) NOT NULL,
    user_id VARCHAR(8) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_sessions_user_id ON user_sessions(user_id);
CREATE INDEX idx_user_sessions_transaction_id ON user_sessions(transaction_id);
CREATE INDEX idx_user_sessions_program_name ON user_sessions(program_name);
CREATE INDEX idx_user_sessions_user_type ON user_sessions(user_type);
CREATE INDEX idx_user_sessions_reenter_flag ON user_sessions(reenter_flag);
