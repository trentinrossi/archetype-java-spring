-- V6__Create_user_list_pages_table.sql
CREATE TABLE user_list_pages (
    id BIGSERIAL PRIMARY KEY,
    page_number BIGINT NOT NULL,
    first_user_id VARCHAR(8),
    last_user_id VARCHAR(8),
    has_next_page BOOLEAN NOT NULL,
    records_per_page INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_list_pages_page_number ON user_list_pages(page_number);
CREATE INDEX idx_user_list_pages_has_next_page ON user_list_pages(has_next_page);
CREATE UNIQUE INDEX idx_user_list_pages_page_number_unique ON user_list_pages(page_number);
