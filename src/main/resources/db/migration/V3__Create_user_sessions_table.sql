-- Create user_sessions table
CREATE TABLE user_sessions (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(4) NOT NULL,
    program_name VARCHAR(8) NOT NULL,
    from_program VARCHAR(8),
    from_transaction VARCHAR(4),
    program_context BIGINT,
    reenter_flag BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_user_sessions_transaction_id ON user_sessions(transaction_id);
CREATE INDEX idx_user_sessions_program_name ON user_sessions(program_name);
CREATE INDEX idx_user_sessions_reenter_flag ON user_sessions(reenter_flag);
CREATE INDEX idx_user_sessions_from_program ON user_sessions(from_program);
CREATE INDEX idx_user_sessions_from_transaction ON user_sessions(from_transaction);
