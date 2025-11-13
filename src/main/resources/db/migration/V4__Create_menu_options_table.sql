-- V4__Create_menu_options_table.sql
CREATE TABLE menu_options (
    id BIGSERIAL PRIMARY KEY,
    option_number INTEGER NOT NULL,
    option_name VARCHAR(40) NOT NULL,
    program_name VARCHAR(8) NOT NULL,
    user_type_required VARCHAR(1) NOT NULL,
    option_count INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_menu_options_option_number ON menu_options(option_number);
CREATE INDEX idx_menu_options_user_type_required ON menu_options(user_type_required);
CREATE INDEX idx_menu_options_program_name ON menu_options(program_name);
CREATE UNIQUE INDEX idx_menu_options_option_number_unique ON menu_options(option_number);
