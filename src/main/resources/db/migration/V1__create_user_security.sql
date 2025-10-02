CREATE TABLE user_security (
    user_id VARCHAR(8) NOT NULL,
    password VARCHAR(8) NOT NULL,
    user_type VARCHAR(255) NOT NULL,
    program_name VARCHAR(8) NOT NULL DEFAULT 'COSGN00C',
    transaction_id VARCHAR(4) NOT NULL DEFAULT 'CC00',
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id)
);

-- Insert sample data for testing
INSERT INTO user_security (user_id, password, user_type, program_name, transaction_id, active, created_at, updated_at) VALUES
('ADMIN001', 'ADMIN123', 'ADMIN', 'COSGN00C', 'CC00', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USER001', 'USER123', 'GENERAL', 'COSGN00C', 'CC00', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TESTUSER', 'TEST123', 'GENERAL', 'COSGN00C', 'CC00', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);