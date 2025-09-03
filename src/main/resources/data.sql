-- Sample data for testing
-- Insert sample users
INSERT INTO users (username, email, password, role, created_date, is_active) VALUES
('admin', 'admin@example.com', '$2a$10$DowJonesHhYJZjJlbwRjZOAk8qOQZqOoZ8x7vKkM6bwR5pLjqQj3C', 'ADMIN', CURRENT_TIMESTAMP, true),
('user1', 'user1@example.com', '$2a$10$DowJonesHhYJZjJlbwRjZOAk8qOQZqOoZ8x7vKkM6bwR5pLjqQj3C', 'USER', CURRENT_TIMESTAMP, true);

-- Insert sample products
INSERT INTO products (name, description, price, sku, current_stock, minimum_stock, created_by, created_date, is_active) VALUES
('Laptop', 'Dell Latitude 7420', 899.99, 'LAP001', 10, 3, 1, CURRENT_TIMESTAMP, true),
('Mouse', 'Wireless Optical Mouse', 19.99, 'MOU001', 50, 10, 1, CURRENT_TIMESTAMP, true),
('Keyboard', 'Mechanical Gaming Keyboard', 79.99, 'KEY001', 25, 5, 1, CURRENT_TIMESTAMP, true),
('Monitor', '24 inch LED Monitor', 199.99, 'MON001', 8, 2, 1, CURRENT_TIMESTAMP, true),
('Headphones', 'Noise Canceling Headphones', 129.99, 'HEAD001', 2, 5, 1, CURRENT_TIMESTAMP, true);

-- Insert sample stock transactions
INSERT INTO stock_transactions (product_id, transaction_type, quantity, transaction_date, notes, created_by, reference_number, previous_stock, new_stock) VALUES
(1, 'IN', 5, CURRENT_TIMESTAMP, 'Initial stock', 1, 'IN-001', 5, 10),
(2, 'IN', 30, CURRENT_TIMESTAMP, 'Initial stock', 1, 'IN-002', 20, 50),
(3, 'IN', 15, CURRENT_TIMESTAMP, 'Initial stock', 1, 'IN-003', 10, 25),
(4, 'IN', 5, CURRENT_TIMESTAMP, 'Initial stock', 1, 'IN-004', 3, 8),
(5, 'IN', 2, CURRENT_TIMESTAMP, 'Initial stock', 1, 'IN-005', 0, 2);
