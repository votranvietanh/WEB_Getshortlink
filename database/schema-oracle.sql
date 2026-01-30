# Oracle Database Schema for Shopee Affiliate Link Shortener
# Optimized for Oracle 12c+

-- Users Table
CREATE TABLE users (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR2(50) NOT NULL UNIQUE,
    email VARCHAR2(100) NOT NULL UNIQUE,
    password_hash VARCHAR2(255) NOT NULL,
    full_name VARCHAR2(100),
    shopee_affiliate_id VARCHAR2(100),
    shopee_partner_id VARCHAR2(100),
    shopee_partner_key VARCHAR2(255),
    is_active NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- Affiliate Links Table
CREATE TABLE affiliate_links (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id NUMBER(19) NOT NULL,
    short_code VARCHAR2(20) NOT NULL UNIQUE,
    original_url CLOB NOT NULL,
    shopee_product_id VARCHAR2(100),
    shopee_product_name VARCHAR2(500),
    shopee_shop_id VARCHAR2(100),
    title VARCHAR2(255),
    description CLOB,
    click_count NUMBER(19) DEFAULT 0,
    conversion_count NUMBER(19) DEFAULT 0,
    is_active NUMBER(1) DEFAULT 1,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_links_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_links_short_code ON affiliate_links(short_code);
CREATE INDEX idx_links_user_id ON affiliate_links(user_id);
CREATE INDEX idx_links_product_id ON affiliate_links(shopee_product_id);
CREATE INDEX idx_links_created_at ON affiliate_links(created_at);

-- Clicks Table
CREATE TABLE clicks (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    link_id NUMBER(19) NOT NULL,
    ip_address VARCHAR2(45),
    user_agent CLOB,
    referer CLOB,
    country VARCHAR2(100),
    city VARCHAR2(100),
    device_type VARCHAR2(50),
    browser VARCHAR2(100),
    os VARCHAR2(100),
    clicked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_clicks_link FOREIGN KEY (link_id) REFERENCES affiliate_links(id) ON DELETE CASCADE
);

CREATE INDEX idx_clicks_link_id ON clicks(link_id);
CREATE INDEX idx_clicks_clicked_at ON clicks(clicked_at);
CREATE INDEX idx_clicks_ip_address ON clicks(ip_address);

-- Trigger for updated_at on users
CREATE OR REPLACE TRIGGER trg_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- Trigger for updated_at on affiliate_links
CREATE OR REPLACE TRIGGER trg_links_updated_at
BEFORE UPDATE ON affiliate_links
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- Sample data
INSERT INTO users (username, email, password_hash, full_name, is_active) 
VALUES ('admin', 'admin@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Administrator', 1);

COMMIT;
