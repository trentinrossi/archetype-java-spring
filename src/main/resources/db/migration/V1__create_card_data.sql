-- Flyway migration script for CardData entity
-- Based on COBOL CBACT02C.cbl business rules
-- Creates table for card data file processing

CREATE TABLE card_data (
    -- FDCARDNUM: 16-character alphanumeric field serving as the record key
    card_number VARCHAR(16) NOT NULL PRIMARY KEY,
    
    -- FDCARDDATA: 134-character alphanumeric field containing the card data
    card_data VARCHAR(134) NOT NULL,
    
    -- Audit fields for tracking record creation and modification
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance optimization
-- Primary key index is automatically created for card_number

-- Index for sequential access pattern (mimics COBOL sequential file reading)
CREATE INDEX idx_card_data_card_number_asc ON card_data (card_number ASC);

-- Index for audit fields
CREATE INDEX idx_card_data_created_at ON card_data (created_at);
CREATE INDEX idx_card_data_updated_at ON card_data (updated_at);

-- Index for searching within card data content
CREATE INDEX idx_card_data_content ON card_data (card_data);

-- Comments for documentation
COMMENT ON TABLE card_data IS 'Card data table based on COBOL CBACT02C.cbl CARDFILEFILE structure';
COMMENT ON COLUMN card_data.card_number IS 'FDCARDNUM - 16-character alphanumeric record key';
COMMENT ON COLUMN card_data.card_data IS 'FDCARDDATA - 134-character alphanumeric card data payload';
COMMENT ON COLUMN card_data.created_at IS 'Record creation timestamp';
COMMENT ON COLUMN card_data.updated_at IS 'Record last update timestamp';