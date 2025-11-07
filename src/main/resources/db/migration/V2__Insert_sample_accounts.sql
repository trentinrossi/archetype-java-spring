-- V2__Insert_sample_accounts.sql
-- Sample data for testing the accounts system
-- This migration inserts test accounts with various scenarios

-- Active accounts with normal balances
INSERT INTO accounts (
    acct_id, acct_active_status, acct_curr_bal, acct_credit_limit, acct_cash_credit_limit,
    acct_open_date, acct_expiration_date, acct_reissue_date,
    acct_curr_cyc_credit, acct_curr_cyc_debit, acct_group_id
) VALUES
-- Account 1: Standard active account
(10000000001, 'A', 1500.50, 5000.00, 1000.00,
 '2024-01-15', '2027-01-15', NULL,
 250.00, 150.00, 'GRP001'),

-- Account 2: Active account with higher balance
(10000000002, 'A', 3200.75, 10000.00, 2000.00,
 '2023-06-20', '2026-06-20', '2024-06-20',
 500.00, 300.00, 'GRP001'),

-- Account 3: Active account with low balance
(10000000003, 'A', 250.00, 3000.00, 500.00,
 '2024-03-10', '2027-03-10', NULL,
 100.00, 50.00, 'GRP002'),

-- Account 4: Inactive account
(10000000004, 'I', 0.00, 2000.00, 400.00,
 '2022-01-01', '2025-01-01', NULL,
 0.00, 0.00, 'GRP002'),

-- Account 5: Active account near credit limit
(10000000005, 'A', 4800.00, 5000.00, 1000.00,
 '2023-09-15', '2026-09-15', NULL,
 800.00, 600.00, 'GRP003'),

-- Account 6: Active account over cash credit limit
(10000000006, 'A', 1200.00, 5000.00, 1000.00,
 '2024-02-01', '2027-02-01', NULL,
 300.00, 200.00, 'GRP003'),

-- Account 7: Active account with reissue date
(10000000007, 'A', 2100.00, 7500.00, 1500.00,
 '2022-05-10', '2025-05-10', '2023-05-10',
 450.00, 350.00, 'GRP001'),

-- Account 8: Inactive account with balance
(10000000008, 'I', 500.00, 3000.00, 600.00,
 '2021-11-20', '2024-11-20', NULL,
 0.00, 0.00, NULL),

-- Account 9: Active account expiring soon
(10000000009, 'A', 1800.00, 6000.00, 1200.00,
 '2022-12-01', '2025-12-01', NULL,
 350.00, 250.00, 'GRP002'),

-- Account 10: Active account with high credit limit
(10000000010, 'A', 5500.00, 15000.00, 3000.00,
 '2023-08-15', '2026-08-15', '2024-08-15',
 1000.00, 800.00, 'GRP004'),

-- Account 11: Active account with zero balance
(10000000011, 'A', 0.00, 4000.00, 800.00,
 '2024-04-01', '2027-04-01', NULL,
 0.00, 0.00, 'GRP004'),

-- Account 12: Active account with equal credit and debit
(10000000012, 'A', 2500.00, 8000.00, 1600.00,
 '2023-10-10', '2026-10-10', NULL,
 400.00, 400.00, 'GRP001'),

-- Account 13: Inactive account with no group
(10000000013, 'I', 0.00, 2500.00, 500.00,
 '2021-07-15', '2024-07-15', NULL,
 0.00, 0.00, NULL),

-- Account 14: Active account with recent open date
(10000000014, 'A', 500.00, 3500.00, 700.00,
 '2024-05-01', '2027-05-01', NULL,
 150.00, 100.00, 'GRP003'),

-- Account 15: Active account over credit limit (edge case)
(10000000015, 'A', 5100.00, 5000.00, 1000.00,
 '2023-03-20', '2026-03-20', NULL,
 900.00, 700.00, 'GRP002');

-- Add comments for the sample data
COMMENT ON TABLE accounts IS 'Sample accounts include various scenarios: active/inactive, different balances, groups, and edge cases';
