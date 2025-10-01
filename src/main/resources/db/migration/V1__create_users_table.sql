CREATE TABLE users (
    sec_usr_id VARCHAR(8) NOT NULL,
    sec_usr_fname VARCHAR(20) NOT NULL,
    sec_usr_lname VARCHAR(20) NOT NULL,
    sec_usr_pwd VARCHAR(8) NOT NULL,
    sec_usr_type VARCHAR(1) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (sec_usr_id)
);

-- Add check constraint for user type
ALTER TABLE users ADD CONSTRAINT chk_user_type CHECK (sec_usr_type IN ('A', 'U'));

-- Create index for better query performance
CREATE INDEX idx_users_type ON users(sec_usr_type);
CREATE INDEX idx_users_name ON users(sec_usr_fname, sec_usr_lname);

-- Insert sample data for testing
INSERT INTO users (sec_usr_id, sec_usr_fname, sec_usr_lname, sec_usr_pwd, sec_usr_type) VALUES
('ADMIN001', 'System', 'Administrator', 'admin123', 'A'),
('USER0001', 'John', 'Doe', 'user123', 'U'),
('USER0002', 'Jane', 'Smith', 'pass456', 'U'),
('USER0003', 'Bob', 'Johnson', 'bob789', 'U'),
('ADMIN002', 'Mary', 'Wilson', 'mary123', 'A');