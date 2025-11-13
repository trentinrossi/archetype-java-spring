-- V7__Create_user_selections_table.sql
CREATE TABLE user_selections (
    id BIGSERIAL PRIMARY KEY,
    selection_flag VARCHAR(1) NOT NULL,
    selected_user_id VARCHAR(8) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_selections_selection_flag ON user_selections(selection_flag);
CREATE INDEX idx_user_selections_selected_user_id ON user_selections(selected_user_id);
