CREATE TABLE usrsec (
    sec_usr_id VARCHAR(8) NOT NULL,
    sec_usr_fname VARCHAR(20) NOT NULL,
    sec_usr_lname VARCHAR(20) NOT NULL,
    sec_usr_pwd VARCHAR(8) NOT NULL,
    sec_usr_type VARCHAR(1) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (sec_usr_id)
);