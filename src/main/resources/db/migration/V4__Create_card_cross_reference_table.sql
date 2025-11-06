-- V4__Create_card_cross_reference_table.sql
CREATE TABLE card_cross_reference (
    id BIGSERIAL PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    cross_reference_data VARCHAR(34) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_card_cross_reference_card_number ON card_cross_reference(card_number);

COMMENT ON TABLE card_cross_reference IS 'Maintains relationships between card numbers, customer identifiers, and account identifiers';
COMMENT ON COLUMN card_cross_reference.card_number IS 'Primary key - card number linking to customer and account (must be exactly 16 characters)';
COMMENT ON COLUMN card_cross_reference.cross_reference_data IS 'Contains customer ID and account ID relationships (34 characters)';