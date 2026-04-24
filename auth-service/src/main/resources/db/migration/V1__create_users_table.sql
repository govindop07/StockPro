CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role ENUM('STAFF','MANAGER','OFFICER','ADMIN') NOT NULL DEFAULT 'STAFF',
    department VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP NULL,
    version INT DEFAULT 0
);

-- Insert a default admin user (password is 'admin123')
-- BCrypt hash of 'admin123'
INSERT INTO users (full_name, email, password_hash, role, is_active, created_at)
VALUES ('System Admin', 'admin@stockpro.com', '$2a$10$8.UnVuG9HLROJOsIjd+hZePj6cOQz2a9/H.WcRj8eT7PZ3bV/1qV6', 'ADMIN', true, CURRENT_TIMESTAMP);
