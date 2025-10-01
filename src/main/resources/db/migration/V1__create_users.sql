-- Create users table for CardDemo User Management System
-- Based on USRSEC file structure from COBOL business rules

CREATE TABLE users (
    sec_usr_id VARCHAR(8) NOT NULL COMMENT 'User ID - Primary Key (8 characters)',
    sec_usr_fname VARCHAR(20) NOT NULL COMMENT 'User First Name (20 characters)',
    sec_usr_lname VARCHAR(20) NOT NULL COMMENT 'User Last Name (20 characters)', 
    sec_usr_pwd VARCHAR(8) NOT NULL COMMENT 'User Password (8 characters)',
    sec_usr_type VARCHAR(1) NOT NULL COMMENT 'User Type - A=Admin, U=User (1 character)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record last update timestamp',
    PRIMARY KEY (sec_usr_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User Security Table - equivalent to USRSEC file';

-- Create indexes for better query performance
CREATE INDEX idx_users_type ON users(sec_usr_type);
CREATE INDEX idx_users_fname ON users(sec_usr_fname);
CREATE INDEX idx_users_lname ON users(sec_usr_lname);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Insert sample data for testing
INSERT INTO users (sec_usr_id, sec_usr_fname, sec_usr_lname, sec_usr_pwd, sec_usr_type) VALUES
('ADMIN001', 'System', 'Administrator', 'admin123', 'A'),
('USER0001', 'John', 'Doe', 'user123', 'U'),
('USER0002', 'Jane', 'Smith', 'pass456', 'U'),
('USER0003', 'Bob', 'Johnson', 'bob789', 'U'),
('USER0004', 'Alice', 'Williams', 'alice01', 'U'),
('USER0005', 'Charlie', 'Brown', 'charlie2', 'U'),
('USER0006', 'Diana', 'Davis', 'diana123', 'U'),
('USER0007', 'Edward', 'Miller', 'edward45', 'U'),
('USER0008', 'Fiona', 'Wilson', 'fiona678', 'U'),
('USER0009', 'George', 'Moore', 'george90', 'U'),
('USER0010', 'Helen', 'Taylor', 'helen123', 'U'),
('USER0011', 'Ian', 'Anderson', 'ian456', 'U'),
('USER0012', 'Julia', 'Thomas', 'julia789', 'U');