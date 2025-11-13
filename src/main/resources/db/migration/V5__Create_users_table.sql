-- V5__Create_users_table.sql
CREATE TABLE users (
    user_id VARCHAR(8) NOT NULL,
    user_type VARCHAR(1) NOT NULL,
    authenticated BOOLEAN NOT NULL,
    password VARCHAR(8) NOT NULL,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);

CREATE UNIQUE INDEX idx_users_user_id ON users(user_id);
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_users_authenticated ON users(authenticated);
CREATE INDEX idx_users_first_name ON users(first_name);
CREATE INDEX idx_users_last_name ON users(last_name);
