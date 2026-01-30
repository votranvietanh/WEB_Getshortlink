# Database Schema - Shopee Affiliate Link Shortener

## Entity Relationship Diagram

```
┌─────────────┐         ┌──────────────────┐         ┌─────────────┐
│    Users    │────────<│ Affiliate Links  │>────────│   Clicks    │
└─────────────┘         └──────────────────┘         └─────────────┘
                               │
                               │
                               ▼
                        ┌─────────────┐
                        │  Products   │
                        └─────────────┘
                               │
                               ▼
                        ┌─────────────┐
                        │ Conversions │
                        └─────────────┘
```

---

## Tables

### 1. users
Lưu trữ thông tin người dùng

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    shopee_affiliate_id VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN', 'PREMIUM')),
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),
    email_verified BOOLEAN DEFAULT false,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_status ON users(status);
```

### 2. affiliate_links
Lưu trữ các link rút gọn

```sql
CREATE TABLE affiliate_links (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    short_code VARCHAR(20) UNIQUE NOT NULL,
    original_url TEXT NOT NULL,
    shopee_product_id VARCHAR(100),
    custom_alias VARCHAR(50) UNIQUE,
    title VARCHAR(255),
    description TEXT,
    qr_code_url TEXT,
    click_count BIGINT DEFAULT 0,
    unique_click_count BIGINT DEFAULT 0,
    conversion_count BIGINT DEFAULT 0,
    revenue DECIMAL(15,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'EXPIRED')),
    expires_at TIMESTAMP,
    last_clicked_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_links_short_code ON affiliate_links(short_code);
CREATE INDEX idx_links_user_id ON affiliate_links(user_id);
CREATE INDEX idx_links_status ON affiliate_links(status);
CREATE INDEX idx_links_created_at ON affiliate_links(created_at);
CREATE INDEX idx_links_shopee_product_id ON affiliate_links(shopee_product_id);
```

### 3. clicks
Lưu trữ thông tin mỗi lần click

```sql
CREATE TABLE clicks (
    id BIGSERIAL PRIMARY KEY,
    link_id BIGINT NOT NULL REFERENCES affiliate_links(id) ON DELETE CASCADE,
    ip_address VARCHAR(45),
    user_agent TEXT,
    referrer TEXT,
    country VARCHAR(50),
    city VARCHAR(100),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    device_type VARCHAR(20) CHECK (device_type IN ('DESKTOP', 'MOBILE', 'TABLET')),
    browser VARCHAR(50),
    os VARCHAR(50),
    is_unique BOOLEAN DEFAULT true,
    is_bot BOOLEAN DEFAULT false,
    clicked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_clicks_link_id ON clicks(link_id);
CREATE INDEX idx_clicks_clicked_at ON clicks(clicked_at);
CREATE INDEX idx_clicks_country ON clicks(country);
CREATE INDEX idx_clicks_device_type ON clicks(device_type);
CREATE INDEX idx_clicks_is_unique ON clicks(is_unique);
```

### 4. products
Cache thông tin sản phẩm từ Shopee

```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    shopee_product_id VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(500),
    price DECIMAL(15,2),
    discount_price DECIMAL(15,2),
    image_url TEXT,
    category VARCHAR(100),
    shop_id VARCHAR(100),
    shop_name VARCHAR(200),
    rating DECIMAL(3,2),
    review_count BIGINT,
    sold_count BIGINT,
    commission_rate DECIMAL(5,2),
    data_json JSONB,
    last_synced_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_shopee_id ON products(shopee_product_id);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_shop_id ON products(shop_id);
CREATE INDEX idx_products_last_synced ON products(last_synced_at);
```

### 5. conversions
Theo dõi chuyển đổi và hoa hồng

```sql
CREATE TABLE conversions (
    id BIGSERIAL PRIMARY KEY,
    link_id BIGINT NOT NULL REFERENCES affiliate_links(id) ON DELETE CASCADE,
    click_id BIGINT REFERENCES clicks(id) ON DELETE SET NULL,
    order_id VARCHAR(100) UNIQUE,
    product_id VARCHAR(100),
    order_amount DECIMAL(15,2),
    commission_amount DECIMAL(15,2),
    commission_rate DECIMAL(5,2),
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'PAID', 'CANCELLED')),
    converted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP,
    paid_at TIMESTAMP
);

CREATE INDEX idx_conversions_link_id ON conversions(link_id);
CREATE INDEX idx_conversions_order_id ON conversions(order_id);
CREATE INDEX idx_conversions_status ON conversions(status);
CREATE INDEX idx_conversions_converted_at ON conversions(converted_at);
```

### 6. link_tags
Tags cho links (Many-to-Many)

```sql
CREATE TABLE tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    color VARCHAR(7) DEFAULT '#3498db',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE link_tags (
    link_id BIGINT NOT NULL REFERENCES affiliate_links(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (link_id, tag_id)
);

CREATE INDEX idx_link_tags_link_id ON link_tags(link_id);
CREATE INDEX idx_link_tags_tag_id ON link_tags(tag_id);
```

### 7. campaigns
Nhóm các links theo campaign

```sql
CREATE TABLE campaigns (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    budget DECIMAL(15,2),
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'PAUSED', 'COMPLETED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_campaigns_user_id ON campaigns(user_id);
CREATE INDEX idx_campaigns_status ON campaigns(status);

-- Link to campaign relationship
ALTER TABLE affiliate_links ADD COLUMN campaign_id BIGINT REFERENCES campaigns(id) ON DELETE SET NULL;
CREATE INDEX idx_links_campaign_id ON affiliate_links(campaign_id);
```

### 8. api_keys
API keys cho tích hợp

```sql
CREATE TABLE api_keys (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    key_hash VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(100),
    permissions JSONB,
    last_used_at TIMESTAMP,
    expires_at TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'REVOKED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_api_keys_user_id ON api_keys(user_id);
CREATE INDEX idx_api_keys_key_hash ON api_keys(key_hash);
CREATE INDEX idx_api_keys_status ON api_keys(status);
```

### 9. audit_logs
Logs hoạt động hệ thống

```sql
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    changes JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
```

---

## Views

### v_link_statistics
View tổng hợp thống kê link

```sql
CREATE VIEW v_link_statistics AS
SELECT 
    al.id,
    al.short_code,
    al.title,
    al.user_id,
    al.click_count,
    al.unique_click_count,
    al.conversion_count,
    al.revenue,
    COALESCE(SUM(c.commission_amount), 0) as total_commission,
    CASE 
        WHEN al.click_count > 0 
        THEN ROUND((al.conversion_count::DECIMAL / al.click_count) * 100, 2)
        ELSE 0 
    END as conversion_rate,
    al.created_at,
    al.last_clicked_at
FROM affiliate_links al
LEFT JOIN conversions c ON al.id = c.link_id AND c.status = 'CONFIRMED'
GROUP BY al.id;
```

### v_user_dashboard
View dashboard người dùng

```sql
CREATE VIEW v_user_dashboard AS
SELECT 
    u.id as user_id,
    u.username,
    COUNT(DISTINCT al.id) as total_links,
    COALESCE(SUM(al.click_count), 0) as total_clicks,
    COALESCE(SUM(al.conversion_count), 0) as total_conversions,
    COALESCE(SUM(al.revenue), 0) as total_revenue,
    COUNT(DISTINCT CASE WHEN al.created_at >= CURRENT_DATE - INTERVAL '30 days' THEN al.id END) as links_last_30_days
FROM users u
LEFT JOIN affiliate_links al ON u.id = al.user_id
GROUP BY u.id, u.username;
```

---

## Triggers

### Update timestamp trigger
```sql
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_links_updated_at BEFORE UPDATE ON affiliate_links
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_campaigns_updated_at BEFORE UPDATE ON campaigns
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
```

### Update click count trigger
```sql
CREATE OR REPLACE FUNCTION update_link_click_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE affiliate_links 
    SET 
        click_count = click_count + 1,
        unique_click_count = unique_click_count + CASE WHEN NEW.is_unique THEN 1 ELSE 0 END,
        last_clicked_at = NEW.clicked_at
    WHERE id = NEW.link_id;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trigger_update_click_count AFTER INSERT ON clicks
    FOR EACH ROW EXECUTE FUNCTION update_link_click_count();
```

---

## Sample Data

### Insert sample users
```sql
INSERT INTO users (username, email, password_hash, full_name, shopee_affiliate_id, role)
VALUES 
    ('admin', 'admin@example.com', '$2a$10$...', 'Admin User', 'AFF123', 'ADMIN'),
    ('john_doe', 'john@example.com', '$2a$10$...', 'John Doe', 'AFF456', 'USER');
```

### Insert sample links
```sql
INSERT INTO affiliate_links (user_id, short_code, original_url, title, shopee_product_id)
VALUES 
    (2, 'abc123', 'https://shopee.vn/product/123456', 'iPhone 15 Pro Max', '123456'),
    (2, 'xyz789', 'https://shopee.vn/product/789012', 'Samsung Galaxy S24', '789012');
```

---

## Backup & Maintenance

### Daily backup script
```bash
#!/bin/bash
pg_dump -U postgres affiliate_db > backup_$(date +%Y%m%d).sql
```

### Cleanup old clicks (keep last 90 days)
```sql
DELETE FROM clicks WHERE clicked_at < CURRENT_DATE - INTERVAL '90 days';
```

### Vacuum and analyze
```sql
VACUUM ANALYZE;
```
