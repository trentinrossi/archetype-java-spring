-- V6__Create_disclosure_groups_table.sql

CREATE TABLE disclosure_groups (
    id BIGSERIAL PRIMARY KEY,
    dis_acct_group_id VARCHAR(10) NOT NULL,
    dis_tran_cat_cd VARCHAR(4) NOT NULL,
    dis_tran_type_cd VARCHAR(2) NOT NULL,
    dis_int_rate DECIMAL(5, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_disclosure_groups_group_id ON disclosure_groups(dis_acct_group_id);
CREATE INDEX idx_disclosure_groups_cat_cd ON disclosure_groups(dis_tran_cat_cd);
CREATE INDEX idx_disclosure_groups_type_cd ON disclosure_groups(dis_tran_type_cd);
