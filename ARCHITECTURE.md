# Kiến Trúc Hệ Thống - Shopee Affiliate Link Shortener

## 1. Tổng Quan Hệ Thống

### 1.1. Mô Tả
Hệ thống **Shopee Affiliate Link Shortener** là một nền tảng web cho phép người dùng:
- Tạo link rút gọn cho các sản phẩm affiliate Shopee
- Quản lý và theo dõi hiệu suất các link affiliate
- Tích hợp với Shopee GraphAPI để lấy thông tin sản phẩm
- Phân tích số liệu click, conversion và doanh thu

### 1.2. Công Nghệ Stack

#### Backend
- **Framework**: Spring Boot 2.7.x (compatible with Java 8)
- **Language**: Java 8
- **Database**: 
  - **Development**: H2 Database (in-memory, zero configuration)
  - **Production**: MySQL 5.7+ hoặc PostgreSQL 12+
- **Cache**: Redis (optional for production)
- **API Documentation**: Swagger/OpenAPI 2.x
- **Security**: Spring Security + JWT

#### Frontend
- **Framework**: Vue.js 2.6.14
- **UI Library**: Element UI 2.15.x
- **State Management**: Vuex 3.x
- **Routing**: Vue Router 3.x
- **HTTP Client**: Axios
- **Build Tool**: Vue CLI 4.x / Webpack 4.x

---

## 2. Kiến Trúc Tổng Thể

```
CLIENT LAYER (Vue.js)
        ↓
API GATEWAY (Nginx)
        ↓
APPLICATION LAYER (Spring Boot)
├── Auth Service
├── Link Service
├── Shopee Service
└── Analytics Service
        ↓
DATA LAYER
├── H2 Database (Development - In-Memory)
├── MySQL 5.7+ (Production - Primary DB)
└── Redis (Production - Cache, Optional)
        ↓
EXTERNAL SERVICES
└── Shopee GraphAPI
```

---

## 3. Cấu Trúc Thư Mục

### Backend (Spring Boot)
```
backend/
├── src/main/java/com/affiliate/shortlink/
│   ├── config/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   ├── security/
│   └── util/
└── src/main/resources/
```

### Frontend (Vue.js)
```
frontend/
├── src/
│   ├── components/
│   ├── views/
│   ├── router/
│   ├── store/
│   └── services/
└── public/
```

---

## 4. Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    shopee_affiliate_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### Affiliate Links Table
```sql
CREATE TABLE affiliate_links (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    short_code VARCHAR(20) UNIQUE NOT NULL,
    original_url TEXT NOT NULL,
    shopee_product_id VARCHAR(100),
    click_count BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_short_code (short_code),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## 5. API Endpoints

### Authentication
- POST /api/v1/auth/register
- POST /api/v1/auth/login
- POST /api/v1/auth/refresh

### Link Management
- POST /api/v1/links
- GET /api/v1/links
- GET /api/v1/links/{id}
- DELETE /api/v1/links/{id}

### Shopee Integration
- GET /api/v1/shopee/products/search
- POST /api/v1/shopee/affiliate/create

### Analytics
- GET /api/v1/analytics/dashboard
- GET /api/v1/analytics/links/{id}

---

## 6. Security

- JWT Authentication
- HTTPS Only
- CORS Configuration
- Rate Limiting (Redis)
- Password Hashing (BCrypt)

---

## 7. Deployment

### Docker Compose
```yaml
services:
  frontend:
    build: ./frontend
    ports: ["8081:80"]
  backend:
    build: ./backend
    ports: ["8080:8080"]
    environment:
      - SPRING_PROFILES_ACTIVE=dev
  mysql:
    image: mysql:5.7
    ports: ["3306:3306"]
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: affiliate_db
  redis:
    image: redis:6-alpine
    ports: ["6379:6379"]
```

**Lưu ý**: Khi chạy local development, backend sẽ tự động sử dụng H2 in-memory database, không cần MySQL.
