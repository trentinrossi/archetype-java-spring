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

-- Create index for better performance on user type queries
CREATE INDEX idx_users_user_type ON users(sec_usr_type);

-- Create index for better performance on name searches
CREATE INDEX idx_users_names ON users(sec_usr_fname, sec_usr_lname);