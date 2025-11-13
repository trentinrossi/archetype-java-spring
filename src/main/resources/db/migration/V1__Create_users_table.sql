-- V1__Create_users_table.sql
-- Create users table for CardDemo User Management System

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_type VARCHAR(1) NOT NULL,
    authenticated BOOLEAN NOT NULL DEFAULT FALSE,
    user_id VARCHAR(8) NOT NULL UNIQUE,
    password VARCHAR(8) NOT NULL,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_user_id ON users(user_id);
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_users_authenticated ON users(authenticated);
