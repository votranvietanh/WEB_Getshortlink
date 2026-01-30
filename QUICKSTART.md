# Quick Start Guide - Shopee Affiliate Link Shortener

## 🚀 Bắt Đầu Nhanh trong 5 Phút

### Bước 1: Clone & Setup (1 phút)

```bash
# Clone repository
git clone https://github.com/yourname/WEB_Getshortlink.git
cd WEB_Getshortlink

# Copy environment file
cp .env.example .env
```

### Bước 2: Cấu Hình Environment (2 phút)

Mở file `.env` và điền thông tin:

```env
# Database
DB_PASSWORD=postgres123

# Shopee API (Lấy từ https://open.shopee.com/)
SHOPEE_API_KEY=your_api_key_here
SHOPEE_API_SECRET=your_api_secret_here
SHOPEE_AFFILIATE_ID=your_affiliate_id_here

# JWT Secret
JWT_SECRET=change-this-to-random-string
```

### Bước 3: Chạy với Docker (2 phút)

```bash
# Start tất cả services
docker-compose up -d

# Kiểm tra status
docker-compose ps

# Xem logs
docker-compose logs -f
```

### Bước 4: Truy Cập Ứng Dụng

- **Frontend**: http://localhost
- **Backend API**: http://localhost:8080
- **API Docs**: http://localhost:8080/swagger-ui.html
- **Database Admin**: http://localhost:8082 (Adminer)
- **Redis Admin**: http://localhost:8083 (Redis Commander)

---

## 📋 Development Setup (Không dùng Docker)

### Prerequisites

```bash
# Kiểm tra Java
java -version  # Cần Java 17+

# Kiểm tra Node.js
node -v  # Cần Node 18+

# Kiểm tra PostgreSQL
psql --version  # Cần PostgreSQL 15+

# Kiểm tra Redis
redis-cli --version  # Cần Redis 7+
```

### 1. Setup Database

```bash
# Start PostgreSQL
sudo systemctl start postgresql

# Create database
sudo -u postgres psql
CREATE DATABASE affiliate_db;
CREATE USER postgres WITH PASSWORD 'postgres123';
GRANT ALL PRIVILEGES ON DATABASE affiliate_db TO postgres;
\q

# Import schema
psql -U postgres -d affiliate_db -f database/schema.sql
```

### 2. Setup Redis

```bash
# Start Redis
sudo systemctl start redis-server

# Set password
redis-cli
CONFIG SET requirepass "redis123"
CONFIG REWRITE
exit
```

### 3. Run Backend

```bash
cd backend

# Install dependencies & build
./mvnw clean install

# Run application
./mvnw spring-boot:run

# Backend sẽ chạy tại http://localhost:8080
```

### 4. Run Frontend

```bash
cd frontend

# Install dependencies
npm install

# Run dev server
npm run dev

# Frontend sẽ chạy tại http://localhost:5173
```

---

## 🧪 Test API

### 1. Register User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test123!",
    "fullName": "Test User"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test123!"
  }'
```

Lưu `accessToken` từ response.

### 3. Create Short Link

```bash
curl -X POST http://localhost:8080/api/v1/links \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -d '{
    "originalUrl": "https://shopee.vn/product/123456/789012",
    "title": "iPhone 15 Pro Max"
  }'
```

### 4. Search Shopee Products

```bash
curl -X GET "http://localhost:8080/api/v1/shopee/products/search?keyword=iphone&limit=10" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## 📚 Tài Liệu Chi Tiết

| Tài Liệu | Mô Tả |
|----------|-------|
| [README.md](./README.md) | Tổng quan dự án |
| [ARCHITECTURE.md](./ARCHITECTURE.md) | Kiến trúc hệ thống |
| [FEATURES.md](./FEATURES.md) | Danh sách chức năng |
| [API.md](./API.md) | API documentation |
| [DATABASE.md](./DATABASE.md) | Database schema |
| [DEPLOYMENT.md](./DEPLOYMENT.md) | Hướng dẫn deploy |
| [SHOPEE_INTEGRATION.md](./SHOPEE_INTEGRATION.md) | Tích hợp Shopee API |
| [PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md) | Cấu trúc dự án |

---

## 🔧 Troubleshooting

### Backend không start?

```bash
# Kiểm tra port 8080
netstat -tulpn | grep 8080

# Kiểm tra logs
tail -f logs/application.log

# Kiểm tra database connection
psql -U postgres -h localhost -d affiliate_db
```

### Frontend không kết nối được Backend?

```bash
# Kiểm tra VITE_API_URL trong .env
cat frontend/.env

# Kiểm tra CORS trong backend
# File: backend/src/main/resources/application.yml
```

### Docker containers không start?

```bash
# Xem logs
docker-compose logs

# Restart containers
docker-compose restart

# Rebuild containers
docker-compose down
docker-compose up -d --build
```

---

## 🎯 Next Steps

### 1. Cấu Hình Shopee API
- Đăng ký tài khoản tại https://open.shopee.com/
- Tạo application và lấy API credentials
- Cập nhật `.env` với API keys

### 2. Customize Frontend
- Thay đổi logo và branding
- Tùy chỉnh màu sắc trong `frontend/src/assets/styles/`
- Thêm các tính năng mới

### 3. Deploy to Production
- Đọc [DEPLOYMENT.md](./DEPLOYMENT.md)
- Setup SSL certificate
- Configure domain name
- Setup monitoring

### 4. Add Features
- Xem [FEATURES.md](./FEATURES.md) cho roadmap
- Implement A/B testing
- Add browser extension
- Create mobile app

---

## 📞 Support

### Gặp vấn đề?

1. **Check Documentation**: Đọc các file .md trong thư mục
2. **Check Logs**: Xem logs của backend, frontend, database
3. **GitHub Issues**: Tạo issue trên GitHub
4. **Email**: support@example.com

### Useful Commands

```bash
# Docker
docker-compose up -d          # Start all services
docker-compose down           # Stop all services
docker-compose logs -f        # View logs
docker-compose ps             # Check status

# Backend
./mvnw spring-boot:run        # Run backend
./mvnw test                   # Run tests
./mvnw clean package          # Build JAR

# Frontend
npm run dev                   # Run dev server
npm run build                 # Build for production
npm run test                  # Run tests

# Database
psql -U postgres -d affiliate_db    # Connect to DB
pg_dump affiliate_db > backup.sql   # Backup DB
psql affiliate_db < backup.sql      # Restore DB
```

---

## 🎓 Learning Resources

### Spring Boot
- https://spring.io/guides
- https://www.baeldung.com/spring-boot

### Vue.js
- https://vuejs.org/guide/
- https://vueschool.io/

### Shopee API
- https://open.shopee.com/documents
- https://open.shopee.com/developer-guide

### Docker
- https://docs.docker.com/
- https://www.docker.com/get-started

---

## ✅ Checklist

### Development
- [ ] Clone repository
- [ ] Setup environment variables
- [ ] Start database
- [ ] Start Redis
- [ ] Run backend
- [ ] Run frontend
- [ ] Test API endpoints
- [ ] Configure Shopee API

### Production
- [ ] Setup production server
- [ ] Configure domain & SSL
- [ ] Setup database backups
- [ ] Configure monitoring
- [ ] Setup CI/CD
- [ ] Security audit
- [ ] Performance testing
- [ ] Documentation review

---

## 🚀 Ready to Go!

Bạn đã sẵn sàng để bắt đầu phát triển!

```bash
# Quick start với Docker
docker-compose up -d

# Hoặc manual setup
cd backend && ./mvnw spring-boot:run &
cd frontend && npm run dev
```

**Happy Coding! 🎉**
