-- V4__Insert_sample_data.sql
-- Insert sample data for testing the Bill Payment system

-- Insert sample accounts
INSERT INTO accounts (acct_id, acct_curr_bal, created_at, updated_at) VALUES
('ACC00001234', 1500.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC00005678', 2500.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC00009012', 750.25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC00003456', 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC00007890', 5000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample card cross-references
INSERT INTO card_cross_reference (xref_acct_id, xref_card_num, created_at, updated_at) VALUES
('ACC00001234', '4111111111111111', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC00001234', '4222222222222222', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC00005678', '4333333333333333', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC00009012', '4444444444444444', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC00007890', '4555555555555555', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample transactions (historical bill payments)
INSERT INTO transactions (
    tran_type_cd, tran_cat_cd, tran_source, tran_desc, tran_amt, 
    tran_card_num, tran_merchant_id, tran_merchant_name, tran_merchant_city, 
    tran_merchant_zip, tran_orig_ts, tran_proc_ts, acct_id, created_at, updated_at
) VALUES
(
    '02', 2, 'POS TERM', 'BILL PAYMENT - ONLINE', 1000.00,
    '4111111111111111', 999999999, 'BILL PAYMENT', 'N/A',
    'N/A', DATEADD('DAY', -30, CURRENT_TIMESTAMP), DATEADD('DAY', -30, CURRENT_TIMESTAMP), 
    'ACC00001234', DATEADD('DAY', -30, CURRENT_TIMESTAMP), DATEADD('DAY', -30, CURRENT_TIMESTAMP)
),
(
    '02', 2, 'POS TERM', 'BILL PAYMENT - ONLINE', 1500.00,
    '4333333333333333', 999999999, 'BILL PAYMENT', 'N/A',
    'N/A', DATEADD('DAY', -60, CURRENT_TIMESTAMP), DATEADD('DAY', -60, CURRENT_TIMESTAMP), 
    'ACC00005678', DATEADD('DAY', -60, CURRENT_TIMESTAMP), DATEADD('DAY', -60, CURRENT_TIMESTAMP)
),
(
    '02', 2, 'POS TERM', 'BILL PAYMENT - ONLINE', 500.00,
    '4444444444444444', 999999999, 'BILL PAYMENT', 'N/A',
    'N/A', DATEADD('DAY', -15, CURRENT_TIMESTAMP), DATEADD('DAY', -15, CURRENT_TIMESTAMP), 
    'ACC00009012', DATEADD('DAY', -15, CURRENT_TIMESTAMP), DATEADD('DAY', -15, CURRENT_TIMESTAMP)
),
(
    '02', 2, 'POS TERM', 'BILL PAYMENT - ONLINE', 3000.00,
    '4555555555555555', 999999999, 'BILL PAYMENT', 'N/A',
    'N/A', DATEADD('DAY', -45, CURRENT_TIMESTAMP), DATEADD('DAY', -45, CURRENT_TIMESTAMP), 
    'ACC00007890', DATEADD('DAY', -45, CURRENT_TIMESTAMP), DATEADD('DAY', -45, CURRENT_TIMESTAMP)
);

-- Add comments
COMMENT ON TABLE accounts IS 'Sample accounts for testing bill payment functionality';
COMMENT ON TABLE card_cross_reference IS 'Sample card cross-references linking cards to accounts';
COMMENT ON TABLE transactions IS 'Sample historical bill payment transactions';
