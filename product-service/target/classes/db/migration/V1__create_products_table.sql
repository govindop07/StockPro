CREATE TABLE products (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    category VARCHAR(100),
    brand VARCHAR(100),
    unit_of_measure VARCHAR(50),
    cost_price DECIMAL(10, 2),
    selling_price DECIMAL(10, 2),
    reorder_level INT,
    max_stock_level INT,
    lead_time_days INT,
    barcode VARCHAR(100),
    image_url VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);
