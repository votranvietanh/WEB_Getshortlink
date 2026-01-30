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
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Database**: PostgreSQL
- **Cache**: Redis
- **API Documentation**: Swagger/OpenAPI
- **Security**: Spring Security + JWT

#### Frontend
- **Framework**: Vue.js 3 (Composition API)
- **UI Library**: Element Plus
- **State Management**: Pinia
- **Routing**: Vue Router
- **HTTP Client**: Axios
- **Build Tool**: Vite

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
├── PostgreSQL (Primary DB)
├── Redis (Cache)
└── MongoDB (Logs)
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
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    shopee_affiliate_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Affiliate Links Table
```sql
CREATE TABLE affiliate_links (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    short_code VARCHAR(20) UNIQUE NOT NULL,
    original_url TEXT NOT NULL,
    shopee_product_id VARCHAR(100),
    click_count BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
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
    ports: ["80:80"]
  backend:
    build: ./backend
    ports: ["8080:8080"]
  postgres:
    image: postgres:15
  redis:
    image: redis:7-alpine
```
