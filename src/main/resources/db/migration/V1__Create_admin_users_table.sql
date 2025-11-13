-- V1__Create_admin_users_table.sql
CREATE TABLE admin_users (
    user_id VARCHAR(8) NOT NULL,
    authentication_status BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);

CREATE UNIQUE INDEX idx_admin_users_user_id ON admin_users(user_id);
