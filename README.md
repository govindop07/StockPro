# StockPro Inventory Management System - Project Report

## 1. Overall Project Architecture

StockPro is built on a modern distributed microservices architecture using Spring Boot for the backend and React for the frontend. The system relies on an API Gateway to route traffic, a Eureka Server for service discovery, and RabbitMQ for asynchronous event-driven communication (such as alerts).

```mermaid
graph TD
    UI[React Frontend App] --> |HTTP / REST| API[API Gateway :8080]
    
    API --> |Route /auth| AUTH[Auth Service :8081]
    API --> |Route /products| PROD[Product Service :8082]
    API --> |Route /warehouses| WH[Warehouse Service :8083]
    API --> |Route /purchases| PUR[Purchase Service :8084]
    API --> |Route /reports| REP[Report Service :8085]
    API --> |Route /alerts| ALRT[Alert Service :8086]

    AUTH -.-> |Registers| EUR[Eureka Server :8761]
    PROD -.-> |Registers| EUR
    WH -.-> |Registers| EUR
    PUR -.-> |Registers| EUR
    REP -.-> |Registers| EUR
    ALRT -.-> |Registers| EUR
    API -.-> |Discovers via| EUR

    WH --> |Publish Event| RMQ[RabbitMQ]
    PUR --> |Publish Event| RMQ
    RMQ --> |Consume Event| ALRT

    AUTH --> DB1[(MySQL: Auth DB)]
    PROD --> DB2[(MySQL: Product DB)]
    WH --> DB3[(MySQL: Warehouse DB)]
    PUR --> DB4[(MySQL: Purchase DB)]
    ALRT --> DB6[(MySQL: Alert DB)]

    REP --> |Feign Client| WH
    REP --> |Feign Client| PROD
    REP --> |Feign Client| PUR
```

## 2. Entity Relationship (ER) Diagrams

Since the architecture follows the Database-per-Service pattern, each microservice manages its own isolated schema. Below is the logical ER representation across the key services.

```mermaid
erDiagram
    %% Auth Service
    USER {
        Long user_id PK
        String username
        String password
        String email
        String role "ADMIN, MANAGER, STAFF, SUPPLIER"
        Boolean is_active
    }

    %% Product Service
    PRODUCT {
        Long product_id PK
        String sku UK
        String name
        String category
        String brand
        BigDecimal cost_price
        BigDecimal selling_price
        Integer reorder_level
        Boolean is_active
    }

    %% Warehouse Service
    WAREHOUSE {
        Long warehouse_id PK
        String name
        String location
        Integer capacity
        Integer used_capacity
        Boolean is_active
    }
    STOCK_LEVEL {
        Long stock_id PK
        Long warehouse_id FK
        Long product_id FK
        Integer quantity
        Integer reserved_quantity
        String location_ref
    }
    WAREHOUSE ||--o{ STOCK_LEVEL : "contains"
    PRODUCT ||--o{ STOCK_LEVEL : "is stored as"

    %% Purchase Service
    PURCHASE_ORDER {
        Long order_id PK
        String order_number UK
        Long supplier_id
        Long target_warehouse_id
        String status
        BigDecimal total_amount
    }
    PURCHASE_ORDER_ITEM {
        Long item_id PK
        Long order_id FK
        Long product_id
        Integer ordered_quantity
        Integer received_quantity
        BigDecimal unit_price
    }
    PURCHASE_ORDER ||--o{ PURCHASE_ORDER_ITEM : "contains"

    %% Alert Service
    ALERT {
        Long alert_id PK
        String reference_id
        String message
        String type "LOW_STOCK, PO_APPROVED, etc."
        Boolean is_read
    }
```

## 3. Microservices Use Cases

### 3.1 API Gateway & Eureka Server
- **API Gateway**: Acts as the single entry point for the frontend, routing requests to the appropriate downstream microservice. Handles CORS and forwards authorization headers.
- **Eureka Server**: Provides service registry and discovery, allowing microservices to locate each other dynamically without hardcoded URLs.

### 3.2 Auth Service (`auth-service`)
- **Authentication**: Validates user credentials and issues stateless JWT tokens.
- **User Management**: Allows admins to create users, assign roles, and activate/deactivate accounts.

### 3.3 Product Service (`product-service`)
- **Catalog Management**: Create, read, update, deactivate, and delete products.
- **Search & Filter**: Retrieve products by SKU, barcode, category, brand, or text search.
- **Low Stock Monitoring**: Provides specific endpoints to identify products nearing or below their reorder levels.

### 3.4 Warehouse Service (`warehouse-service`)
- **Warehouse Management**: Track locations, capacity, and current utilization.
- **Stock Tracking**: Maintain granular stock levels per warehouse and product. Tracks available vs. reserved quantities.
- **Stock Adjustments**: Process manual stock-ins and stock-outs, ensuring physical counts match system records. Emits low stock events via RabbitMQ when levels drop.

### 3.5 Purchase Service (`purchase-service`)
- **Order Lifecycle**: Create purchase orders in DRAFT state, submit for approval, approve, and mark as delivered.
- **Receiving**: Process item receipts against POs. As items are received, synchronous Feign calls are made to the Warehouse service to increment actual physical stock.
- **Supplier Tracking**: Group and filter POs by assigned supplier.

### 3.6 Report Service (`report-service`)
- **Live Valuation**: Communicates with the Product and Warehouse services in real-time to compute the total financial valuation of all physical inventory.
- **Turnover Calculation**: Aggregates total purchase spend over a selected time period and compares it against live valuation to calculate the Inventory Turnover Rate.

### 3.7 Alert Service (`alert-service`)
- **Event Consumption**: Listens to RabbitMQ queues for system events (e.g., stock falling below reorder level).
- **Notification Persistence**: Saves alerts to the database for users to view in the UI dashboard.
- **Alert Management**: Allows users to mark alerts as read or unread.

## 4. Role Responsibilities

The system implements Role-Based Access Control (RBAC). The following roles dictate authorization boundaries:

| Role | Core Responsibilities & Permissions |
|------|-------------------------------------|
| **ADMIN** | **Superuser Access.**<br/>- Full access to all modules.<br/>- Manage user accounts (create, activate, deactivate).<br/>- Configure system-wide settings.<br/>- Approve high-value Purchase Orders.<br/>- Access executive Reports and Analytics. |
| **MANAGER** | **Operational Oversight.**<br/>- Create and edit Product catalog entries.<br/>- Create and manage Warehouses and bin locations.<br/>- Initiate and submit Purchase Orders.<br/>- Perform global inventory checks and view alerts. |
| **STAFF** | **Day-to-day Execution.**<br/>- View product details and locate stock.<br/>- Perform manual Stock Adjustments (Stock In / Stock Out).<br/>- Receive deliveries against approved Purchase Orders.<br/>- Cannot create products or manage users. |
| **SUPPLIER** | **External Partner Access.**<br/>- Limited visibility restricted to their specific entity.<br/>- View Purchase Orders assigned to them.<br/>- Update expected delivery dates for their active orders. |
