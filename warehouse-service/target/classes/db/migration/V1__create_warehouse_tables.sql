CREATE TABLE warehouses (
    warehouse_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    address VARCHAR(255),
    manager_id BIGINT,
    capacity INT,
    used_capacity INT DEFAULT 0,
    phone VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock_levels (
    stock_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    reserved_quantity INT NOT NULL DEFAULT 0,
    location_ref VARCHAR(50),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT DEFAULT 0,
    CONSTRAINT uk_warehouse_product UNIQUE (warehouse_id, product_id),
    CONSTRAINT fk_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id)
);

