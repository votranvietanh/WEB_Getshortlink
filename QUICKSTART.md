# Quick Start Guide - Shopee Affiliate Link Shortener

## Yêu Cầu Hệ Thống

### Backend
- **Java 8** (JDK 1.8.0_201 hoặc cao hơn)
- **Maven 3.6+** (hoặc sử dụng Maven Wrapper đã có sẵn)
- **H2 Database** (tự động, không cần cài đặt)
- **MySQL 5.7+** (chỉ cho production, optional)

### Frontend
- **Node.js 12.x** hoặc cao hơn (khuyến nghị 14.x hoặc 16.x)
- **NPM 6.x+** hoặc **Yarn 1.x+**

### Optional
- **Docker** và **Docker Compose** (nếu muốn chạy bằng container)
- **Git** (để clone repository)

---

## Cài Đặt Nhanh (Development)

### Bước 1: Clone Repository

```bash
git clone <repository-url>
cd WEB_Getshortlink
```

### Bước 2: Cấu Hình Backend

#### 2.1. Kiểm tra Java version
```bash
java -version
# Phải là Java 8 (1.8.x)
```

#### 2.2. Cấu hình application properties (Optional)
File `backend/src/main/resources/application-dev.yml` đã được cấu hình sẵn với H2 Database.

Nếu muốn thay đổi cấu hình:
```yaml
# H2 Database sẽ tự động chạy in-memory
# Không cần cấu hình gì thêm!

# Truy cập H2 Console tại: http://localhost:8080/api/h2-console
# JDBC URL: jdbc:h2:mem:affiliate_db
# Username: sa
# Password: (để trống)
```

#### 2.3. Chạy Backend
```bash
cd backend

# Sử dụng Maven Wrapper (khuyến nghị)
./mvnw spring-boot:run

# Hoặc nếu đã cài Maven
mvn spring-boot:run
```

Backend sẽ chạy tại: **http://localhost:8080**

API Swagger UI: **http://localhost:8080/swagger-ui.html**

H2 Console: **http://localhost:8080/api/h2-console**

### Bước 3: Cấu Hình Frontend

#### 3.1. Cài đặt dependencies
```bash
cd frontend
npm install

# Hoặc sử dụng Yarn
yarn install
```

#### 3.2. Cấu hình environment
File `.env` đã được tạo sẵn với cấu hình mặc định:
```env
VUE_APP_API_BASE_URL=http://localhost:8080/api
VUE_APP_TITLE=Shopee Affiliate Link Shortener
```

#### 3.3. Chạy Frontend
```bash
npm run serve

# Hoặc với Yarn
yarn serve
```

Frontend sẽ chạy tại: **http://localhost:8081**

---

## Kiểm Tra Ứng Dụng

1. Mở trình duyệt và truy cập: **http://localhost:8081**
2. Đăng ký tài khoản mới
3. Đăng nhập
4. Tạo link rút gọn đầu tiên

---

## Cấu Hình Shopee API (Bắt buộc để sử dụng tính năng Affiliate)

### 1. Lấy Shopee API Credentials

1. Đăng ký tài khoản Shopee Affiliate tại: https://affiliate.shopee.vn
2. Truy cập Shopee Open Platform: https://open.shopee.com
3. Tạo ứng dụng mới và lấy:
   - Partner ID
   - Partner Key

### 2. Cấu hình Backend

Tạo file `backend/src/main/resources/application-local.yml`:
```yaml
shopee:
  api:
    partner-id: YOUR_PARTNER_ID
    partner-key: YOUR_PARTNER_KEY
```

Hoặc sử dụng environment variables:
```bash
export SHOPEE_PARTNER_ID=your_partner_id
export SHOPEE_PARTNER_KEY=your_partner_key
```

---

## Chạy với Docker (Alternative)

### 1. Sử dụng Docker Compose

```bash
# Build và chạy tất cả services
docker-compose up -d

# Xem logs
docker-compose logs -f

# Dừng services
docker-compose down
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
