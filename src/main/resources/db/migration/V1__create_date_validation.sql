CREATE TABLE date_validation (
    validation_id BIGSERIAL PRIMARY KEY,
    input_date VARCHAR(50) NOT NULL,
    format_pattern VARCHAR(50) NOT NULL,
    validation_result BOOLEAN NOT NULL,
    severity_code VARCHAR(10) NOT NULL,
    message_number INTEGER NOT NULL,
    result_message VARCHAR(500) NOT NULL,
    lillian_date_output BIGINT,
    error_type VARCHAR(50),
    test_date VARCHAR(50),
    mask_used VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);