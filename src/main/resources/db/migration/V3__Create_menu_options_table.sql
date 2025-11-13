-- V3__Create_menu_options_table.sql
-- Create menu_options table for managing menu navigation options

CREATE TABLE menu_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    option_number INT NOT NULL,
    option_name VARCHAR(40) NOT NULL,
    program_name VARCHAR(8) NOT NULL,
    user_type_required VARCHAR(1) NOT NULL,
    option_count INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_menu_options_option_number ON menu_options(option_number);
CREATE INDEX idx_menu_options_user_type_required ON menu_options(user_type_required);
CREATE INDEX idx_menu_options_is_active ON menu_options(is_active);
CREATE INDEX idx_menu_options_display_order ON menu_options(display_order);
CREATE INDEX idx_menu_options_program_name ON menu_options(program_name);

-- Insert default menu options for CardDemo Admin Menu
INSERT INTO menu_options (option_number, option_name, program_name, user_type_required, option_count, is_active, display_order) VALUES
(1, 'User Management', 'COUSR00C', 'A', 10, TRUE, 1),
(2, 'Account Management', 'COACC00C', 'A', 10, TRUE, 2),
(3, 'Transaction Management', 'COTRN00C', 'A', 10, TRUE, 3),
(4, 'Reports', 'CORPT00C', 'A', 10, TRUE, 4),
(5, 'System Settings', 'COSYS00C', 'A', 10, TRUE, 5),
(6, 'View Account', 'COACV00C', 'U', 10, TRUE, 6),
(7, 'View Transactions', 'COTRV00C', 'U', 10, TRUE, 7),
(8, 'Update Profile', 'COUPR00C', 'U', 10, TRUE, 8),
(9, 'Help', 'COHLP00C', 'U', 10, TRUE, 9),
(10, 'Exit', 'COEXIT0C', 'U', 10, TRUE, 10);
