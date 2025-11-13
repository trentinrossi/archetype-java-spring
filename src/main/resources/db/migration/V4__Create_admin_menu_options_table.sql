-- V4__Create_admin_menu_options_table.sql
-- Create admin_menu_options table for admin-specific menu options

CREATE TABLE admin_menu_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    option_number INT NOT NULL,
    option_name VARCHAR(35) NOT NULL,
    program_name VARCHAR(8) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_admin_menu_options_option_number ON admin_menu_options(option_number);
CREATE INDEX idx_admin_menu_options_is_active ON admin_menu_options(is_active);
CREATE INDEX idx_admin_menu_options_display_order ON admin_menu_options(display_order);

-- Insert default admin menu options
INSERT INTO admin_menu_options (option_number, option_name, program_name, is_active, display_order) VALUES
(1, 'User List', 'COUSR00C', TRUE, 1),
(2, 'Add User', 'COUSR01C', TRUE, 2),
(3, 'Update User', 'COUSR02C', TRUE, 3),
(4, 'Delete User', 'COUSR03C', TRUE, 4),
(5, 'System Reports', 'CORPT00C', TRUE, 5);
