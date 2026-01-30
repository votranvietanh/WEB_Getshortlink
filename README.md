# Shopee Affiliate Link Shortener

Hệ thống rút gọn và quản lý link affiliate Shopee với Vue 2 và Spring Boot (Java 8).

## 🚀 Quick Start - Chạy Local

### Yêu Cầu

- **Java 8** (JDK 1.8.0_201+)
- **Node.js 12+** (khuyến nghị 14.x hoặc 16.x)
- **Maven 3.6+** (hoặc dùng Maven Wrapper)

### Bước 1: Clone Project

```bash
git clone <repository-url>
cd WEB_Getshortlink
```

### Bước 2: Chạy Backend

```bash
cd backend

# Chạy với Maven Wrapper (Windows)
mvnw.cmd spring-boot:run

# Hoặc với Maven đã cài
mvn spring-boot:run
```

Backend sẽ chạy tại: **http://localhost:8080**

- API: http://localhost:8080/api/v1
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/api/h2-console

**H2 Console Login:**
- JDBC URL: `jdbc:h2:mem:affiliate_db`
- Username: `sa`
- Password: (để trống)

### Bước 3: Chạy Frontend

```bash
cd frontend

# Cài đặt dependencies
npm install

# Chạy dev server
npm run serve
```

Frontend sẽ chạy tại: **http://localhost:8081**

### Bước 4: Test Application

1. Mở trình duyệt: http://localhost:8081
2. Click "Đăng Ký Ngay"
3. Tạo tài khoản mới
4. Đăng nhập và sử dụng!

---

## 📁 Cấu Trúc Project

```
WEB_Getshortlink/
├── backend/                    # Spring Boot Application
│   ├── src/main/java/         # Java source code
│   │   └── com/affiliate/shortlink/
│   │       ├── controller/    # REST Controllers
│   │       ├── service/       # Business Logic
│   │       ├── repository/    # Data Access
│   │       ├── model/         # Entities & DTOs
│   │       ├── security/      # JWT Security
│   │       ├── config/        # Configuration
│   │       └── util/          # Utilities
│   ├── src/main/resources/    # Configuration files
│   └── pom.xml               # Maven dependencies
│
├── frontend/                  # Vue.js Application
│   ├── src/
│   │   ├── views/            # Page components
│   │   ├── components/       # Reusable components
│   │   ├── router/           # Vue Router
│   │   ├── store/            # Vuex Store
│   │   ├── services/         # API services
│   │   └── utils/            # Utilities
│   └── package.json          # NPM dependencies
│
└── database/                  # Database schemas
    ├── schema.sql            # MySQL/H2 schema
    └── schema-oracle.sql     # Oracle schema
```

---

## 🛠️ Technology Stack

### Backend
- **Java 8** - Programming language
- **Spring Boot 2.7.18** - Framework
- **Spring Security** - Authentication & Authorization
- **JWT** - Token-based auth
- **H2 Database** - Development (in-memory)
- **MySQL/Oracle** - Production
- **Swagger 2.x** - API documentation
- **Maven** - Build tool

### Frontend
- **Vue.js 2.6.14** - JavaScript framework
- **Vuex 3.x** - State management
- **Vue Router 3.x** - Routing
- **Element UI 2.15.x** - UI components
- **Axios** - HTTP client
- **Vue CLI 4.x** - Build tool

---

## 📝 API Endpoints

### Authentication
```
POST   /api/v1/auth/register  - Đăng ký user mới
POST   /api/v1/auth/login     - Đăng nhập
GET    /api/v1/auth/me        - Lấy thông tin user hiện tại
```

### Link Management
```
POST   /api/v1/links          - Tạo link mới
GET    /api/v1/links          - Lấy danh sách links
GET    /api/v1/links/{id}     - Lấy link theo ID
DELETE /api/v1/links/{id}     - Xóa link
```

### Redirect
```
GET    /r/{shortCode}         - Redirect đến original URL
```

---

## 🗄️ Database Support

Project hỗ trợ nhiều loại database:

### Development (Default)
```yaml
Database: H2 (in-memory)
Config: application-dev.yml
Lợi ích: Zero setup, tự động chạy
```

### Production Options

#### MySQL
```yaml
Database: MySQL 5.7+
Config: application-prod.yml
Command: export SPRING_PROFILES_ACTIVE=prod
```

#### Oracle
```yaml
Database: Oracle 12c+
Config: application-oracle.yml
Command: export SPRING_PROFILES_ACTIVE=oracle
```

Xem chi tiết: [DATABASE_COMPARISON.md](./DATABASE_COMPARISON.md)

---

## 🔐 Security

- **JWT Authentication** - Token-based auth
- **BCrypt Password Hashing** - Secure password storage
- **CORS Configuration** - Cross-origin requests
- **Spring Security** - Comprehensive security

---

## 📚 Documentation

- [ARCHITECTURE.md](./ARCHITECTURE.md) - Kiến trúc hệ thống
- [QUICKSTART.md](./QUICKSTART.md) - Hướng dẫn chi tiết
- [DATABASE_COMPARISON.md](./DATABASE_COMPARISON.md) - So sánh databases
- [MIGRATION_SUMMARY.md](./MIGRATION_SUMMARY.md) - Migration guide

---

## 🐛 Troubleshooting

### Backend không start?

**Java version không đúng:**
```bash
java -version  # Phải là Java 8 (1.8.x)
```

**Port 8080 đã được sử dụng:**
```yaml
# Sửa trong application.yml
server:
  port: 8081
```

### Frontend không start?

**Node modules lỗi:**
```bash
rm -rf node_modules package-lock.json
npm install
```

**Port 8081 đã được sử dụng:**
```javascript
// Sửa trong vue.config.js
devServer: {
  port: 8082
}
```

---

## 🎯 Features

- ✅ User Registration & Login
- ✅ JWT Authentication
- ✅ Create Short Links
- ✅ Click Tracking
- ✅ Link Management
- ✅ Dashboard với Statistics
- ⏳ Shopee API Integration (Coming soon)
- ⏳ Advanced Analytics (Coming soon)
- ⏳ QR Code Generation (Coming soon)

---

## 🚀 Deployment

### Build Production

**Backend:**
```bash
cd backend
mvn clean package -DskipTests
# JAR file: target/shortlink-1.0.0.jar
```

**Frontend:**
```bash
cd frontend
npm run build
# Static files: dist/
```

### Run Production

**Backend:**
```bash
java -jar target/shortlink-1.0.0.jar --spring.profiles.active=prod
```

**Frontend:**
Serve `dist/` folder với Nginx hoặc web server khác.

---

## 📄 License

MIT License

---

## 👥 Support

Nếu gặp vấn đề:
1. Kiểm tra logs của Backend và Frontend
2. Xem phần Troubleshooting
3. Đọc documentation files
4. Tạo issue trên GitHub

---

**Happy Coding! 🎉**
