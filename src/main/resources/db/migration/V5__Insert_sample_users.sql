-- V5__Insert_sample_users.sql
-- Insert sample user data for CardDemo User Management System
-- Provides test data for various user types and authentication states

-- Insert Regular Users (user_type = 'R')
INSERT INTO users (user_type, authenticated, user_id, password, first_name, last_name, created_at, updated_at) VALUES
('R', TRUE, 'USER0001', 'pass0001', 'John', 'Smith', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('R', TRUE, 'USER0002', 'pass0002', 'Sarah', 'Johnson', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('R', TRUE, 'USER0003', 'pass0003', 'Michael', 'Williams', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('R', TRUE, 'USER0004', 'pass0004', 'Emily', 'Brown', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('R', TRUE, 'USER0005', 'pass0005', 'David', 'Jones', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('R', FALSE, 'USER0006', 'pass0006', 'Jessica', 'Garcia', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('R', FALSE, 'USER0007', 'pass0007', 'Daniel', 'Martinez', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('R', TRUE, 'USER0008', 'pass0008', 'Amanda', 'Rodriguez', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('R', TRUE, 'USER0009', 'pass0009', 'Robert', 'Wilson', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('R', TRUE, 'USER0010', 'pass0010', 'Lisa', 'Anderson', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Admin Users (user_type = 'A')
INSERT INTO users (user_type, authenticated, user_id, password, first_name, last_name, created_at, updated_at) VALUES
('A', TRUE, 'ADMIN001', 'admin001', 'James', 'Thompson', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('A', TRUE, 'ADMIN002', 'admin002', 'Maria', 'Davis', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('A', TRUE, 'ADMIN003', 'admin003', 'Christopher', 'Miller', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('A', FALSE, 'ADMIN004', 'admin004', 'Jennifer', 'Taylor', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('A', TRUE, 'ADMIN005', 'admin005', 'Matthew', 'Moore', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Super Admin Users (user_type = 'S')
INSERT INTO users (user_type, authenticated, user_id, password, first_name, last_name, created_at, updated_at) VALUES
('S', TRUE, 'SUPER001', 'super001', 'Richard', 'Jackson', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('S', TRUE, 'SUPER002', 'super002', 'Patricia', 'White', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Guest Users (user_type = 'G')
INSERT INTO users (user_type, authenticated, user_id, password, first_name, last_name, created_at, updated_at) VALUES
('G', FALSE, 'GUEST001', 'guest001', 'Guest', 'User1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G', FALSE, 'GUEST002', 'guest002', 'Guest', 'User2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('G', FALSE, 'GUEST003', 'guest003', 'Guest', 'User3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Test Users with various states
INSERT INTO users (user_type, authenticated, user_id, password, first_name, last_name, created_at, updated_at) VALUES
('R', TRUE, 'TEST0001', 'test0001', 'Test', 'Regular', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('A', TRUE, 'TESTADM1', 'testadm1', 'Test', 'Admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('S', TRUE, 'TESTSUP1', 'testsup1', 'Test', 'Super', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('R', FALSE, 'INACTIV1', 'inactive', 'Inactive', 'Regular', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('R', TRUE, 'DEMO0001', 'demo0001', 'Demo', 'Account', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Comments for reference
COMMENT ON TABLE users IS 'Sample user data with various types: R=Regular, A=Admin, S=Super Admin, G=Guest';
