CREATE TABLE usrsec (
    user_id VARCHAR(8) NOT NULL PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    password VARCHAR(8) NOT NULL,
    user_type VARCHAR(1),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);