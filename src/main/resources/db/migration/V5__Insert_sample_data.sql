-- V5__Insert_sample_data.sql
-- Migration script for inserting sample data
-- H2 database syntax

-- Insert sample customers
INSERT INTO customers (
    customer_id, first_name, middle_name, last_name, 
    address_line_1, address_line_2, address_line_3, 
    state_code, country_code, zip_code, 
    phone_number_1, phone_number_2, ssn, 
    government_issued_id, date_of_birth, eft_account_id, 
    primary_cardholder_indicator, fico_score, fico_credit_score, 
    city, primary_phone_number, secondary_phone_number,
    created_at, updated_at
) VALUES 
(
    100000001, 'John', 'Michael', 'Smith',
    '123 Main Street', 'Apt 4B', 'Building A',
    'CA', 'USA', '90210',
    '310-555-0101', '310-555-0102', 123456789,
    'DL-CA-12345678', '1985-03-15', 'EFT1001',
    'Y', 750, 752,
    'Beverly Hills', '310-555-0101', '310-555-0102',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    100000002, 'Sarah', 'Jane', 'Johnson',
    '456 Oak Avenue', NULL, 'Suite 200',
    'NY', 'USA', '10001',
    '212-555-0201', NULL, 234567890,
    'DL-NY-87654321', '1990-07-22', 'EFT1002',
    'Y', 720, 725,
    'New York', '212-555-0201', NULL,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    100000003, 'Michael', NULL, 'Davis',
    '789 Pine Road', 'Unit 5', 'Floor 3',
    'TX', 'USA', '75001',
    '214-555-0301', '214-555-0302', 345678901,
    'DL-TX-98765432', '1988-11-30', NULL,
    'N', 680, 685,
    'Dallas', '214-555-0301', '214-555-0302',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    100000004, 'Emily', 'Rose', 'Williams',
    '321 Maple Drive', NULL, 'Apartment 12',
    'FL', 'USA', '33101',
    '305-555-0401', NULL, 456789012,
    'DL-FL-11223344', '1992-05-18', 'EFT1003',
    'Y', 800, 805,
    'Miami', '305-555-0401', NULL,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    100000005, 'Robert', 'James', 'Brown',
    '654 Elm Street', 'Suite 101', 'Building B',
    'WA', 'USA', '98101',
    '206-555-0501', '206-555-0502', 567890123,
    'DL-WA-55667788', '1983-09-25', 'EFT1004',
    'Y', 765, 770,
    'Seattle', '206-555-0501', '206-555-0502',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- Insert sample accounts
INSERT INTO accounts (
    account_id, active_status, current_balance, credit_limit, cash_credit_limit,
    open_date, expiration_date, reissue_date,
    current_cycle_credit, current_cycle_debit, group_id, customer_id,
    account_status, version, created_at, updated_at
) VALUES
(
    10000000001, 'Y', 1250.50, 10000.00, 3000.00,
    '2020-01-15', '2027-01-31', '2025-01-15',
    500.00, 1750.50, 'GRP001', 100000001,
    'A', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    10000000002, 'Y', 3420.75, 15000.00, 4500.00,
    '2019-06-20', '2026-06-30', '2024-06-20',
    1200.00, 4620.75, 'GRP001', 100000002,
    'A', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    10000000003, 'Y', 567.30, 5000.00, 1500.00,
    '2021-03-10', '2028-03-31', '2026-03-10',
    300.00, 867.30, 'GRP002', 100000003,
    'A', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    10000000004, 'Y', 8950.00, 25000.00, 7500.00,
    '2018-11-05', '2025-11-30', '2023-11-05',
    2500.00, 11450.00, 'GRP003', 100000004,
    'A', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    10000000005, 'Y', 2145.80, 12000.00, 3600.00,
    '2020-08-22', '2027-08-31', '2025-08-22',
    800.00, 2945.80, 'GRP002', 100000005,
    'A', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    10000000006, 'N', 0.00, 8000.00, 2400.00,
    '2022-02-14', '2029-02-28', '2027-02-14',
    0.00, 0.00, 'GRP001', 100000001,
    'C', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- Insert sample card cross references
INSERT INTO card_cross_references (
    account_id, customer_id, created_at, updated_at
) VALUES
(10000000001, 100000001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10000000002, 100000002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10000000003, 100000003, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10000000004, 100000004, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10000000005, 100000005, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10000000006, 100000001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample cards
INSERT INTO cards (
    card_number, account_id, customer_id, created_at, updated_at
) VALUES
('4532123456789012', 10000000001, 100000001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('4532234567890123', 10000000002, 100000002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('4532345678901234', 10000000003, 100000003, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('4532456789012345', 10000000004, 100000004, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('4532567890123456', 10000000005, 100000005, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('4532678901234567', 10000000006, 100000001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
