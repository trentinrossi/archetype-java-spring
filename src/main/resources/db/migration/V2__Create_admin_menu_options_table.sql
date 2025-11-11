-- Create admin_menu_options table
CREATE TABLE admin_menu_options (
    id BIGSERIAL PRIMARY KEY,
    option_number INTEGER NOT NULL,
    option_name VARCHAR(35) NOT NULL,
    program_name VARCHAR(8) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    admin_user_id VARCHAR(8) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_admin_menu_options_admin_user FOREIGN KEY (admin_user_id) REFERENCES admin_users(user_id) ON DELETE CASCADE,
    CONSTRAINT uk_admin_menu_options_option_number UNIQUE (option_number)
);

-- Create indexes for better query performance
CREATE INDEX idx_admin_menu_options_is_active ON admin_menu_options(is_active);
CREATE INDEX idx_admin_menu_options_option_number ON admin_menu_options(option_number);
CREATE INDEX idx_admin_menu_options_program_name ON admin_menu_options(program_name);
CREATE INDEX idx_admin_menu_options_admin_user_id ON admin_menu_options(admin_user_id);
