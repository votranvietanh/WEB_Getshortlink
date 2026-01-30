# Shopee Affiliate Link Shortener

## 📋 Giới Thiệu

Hệ thống **Shopee Affiliate Link Shortener** là nền tảng web giúp affiliate marketers tạo, quản lý và theo dõi hiệu suất các link affiliate Shopee một cách chuyên nghiệp.

## 🏗️ Kiến Trúc Công Nghệ

### Backend
- **Spring Boot 2.7.x** - Framework chính
- **Java 8** - Programming language
- **MySQL 8.0** - Database (dễ setup cho local)
- **Redis** - Caching (optional cho Phase 1)
- **JWT** - Authentication (Phase 2)
- **Swagger** - API documentation

### Frontend
- **Vue.js 2.6.x** - Framework UI
- **Element UI** - Component library
- **Vuex** - State management
- **Webpack** - Build tool
- **Axios** - HTTP client

## 📁 Cấu Trúc Dự Án

```
WEB_Getshortlink/
├── backend/                 # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
├── frontend/                # Vue.js application
│   ├── src/
│   │   ├── components/
│   │   ├── views/
│   │   ├── router/
│   │   ├── store/
│   │   └── services/
│   └── package.json
├── docker-compose.yml       # Docker configuration
├── ARCHITECTURE.md          # Tài liệu kiến trúc
├── FEATURES.md              # Tài liệu chức năng
└── README.md                # File này
```

## 🚀 Tính Năng Chính

### 1. Quản Lý Link
- ✅ Tạo link rút gọn tự động
- ✅ Custom alias
- ✅ QR Code generation
- ✅ Bulk creation
- ✅ Link expiration

### 2. Tích Hợp Shopee
- ✅ Tìm kiếm sản phẩm
- ✅ Lấy thông tin chi tiết
- ✅ Tạo affiliate link
- ✅ Sync dữ liệu tự động

### 3. Analytics
- ✅ Click tracking
- ✅ Conversion tracking
- ✅ Geographic analytics
- ✅ Device analytics
- ✅ Real-time dashboard

### 4. Bảo Mật
- ✅ JWT Authentication
- ✅ Password encryption
- ✅ Rate limiting
- ✅ HTTPS/SSL

## 📦 Cài Đặt

### Yêu Cầu
- Java 17+
- Node.js 18+
- PostgreSQL 15+
- Redis 7+
- Docker (optional)

### Backend Setup
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```

### Docker Setup
```bash
docker-compose up -d
```

## 🔧 Cấu Hình

### Backend Configuration
File: `backend/src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/affiliate_db
    username: your_username
    password: your_password
  
shopee:
  api:
    url: https://open-api.shopee.com
    key: your_api_key
    secret: your_api_secret
```

### Frontend Configuration
File: `frontend/.env`
```env
VITE_API_URL=http://localhost:8080/api/v1
VITE_APP_NAME=Shopee Link Shortener
```

## 📚 API Documentation

API documentation có sẵn tại: `http://localhost:8080/swagger-ui.html`

### Ví dụ API Endpoints

#### Authentication
```
POST /api/v1/auth/register
POST /api/v1/auth/login
```

#### Links
```
POST /api/v1/links
GET /api/v1/links
GET /api/v1/links/{id}
DELETE /api/v1/links/{id}
```

#### Shopee
```
GET /api/v1/shopee/products/search?keyword=iphone
POST /api/v1/shopee/affiliate/create
```

## 🧪 Testing

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd frontend
npm run test
```

## 📈 Deployment

### Production Build

#### Backend
```bash
./mvnw clean package
java -jar target/shortlink-0.0.1-SNAPSHOT.jar
```

#### Frontend
```bash
npm run build
# Deploy dist/ folder to web server
```

### Docker Deployment
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## 🔐 Security Best Practices

1. **Environment Variables**: Không commit sensitive data
2. **HTTPS**: Luôn sử dụng HTTPS trong production
3. **Rate Limiting**: Cấu hình rate limiting phù hợp
4. **Database Backup**: Backup định kỳ
5. **Update Dependencies**: Cập nhật thư viện thường xuyên

## 📊 Monitoring

- **Health Check**: `http://localhost:8080/actuator/health`
- **Metrics**: `http://localhost:8080/actuator/metrics`
- **Logs**: Check `logs/` directory

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License.

## 👥 Team

- **Solution Architect**: [Your Name]
- **Backend Developer**: [Team Member]
- **Frontend Developer**: [Team Member]

## 📞 Support

- Email: support@example.com
- Documentation: [Link to docs]
- Issues: [GitHub Issues]

## 🗺️ Roadmap

- [x] Phase 1: MVP (Core features)
- [ ] Phase 2: Advanced analytics
- [ ] Phase 3: Mobile app
- [ ] Phase 4: AI recommendations

## 📖 Tài Liệu Liên Quan

- [ARCHITECTURE.md](./ARCHITECTURE.md) - Kiến trúc chi tiết
- [FEATURES.md](./FEATURES.md) - Danh sách chức năng
- [API.md](./API.md) - API documentation
- [DEPLOYMENT.md](./DEPLOYMENT.md) - Hướng dẫn deployment

---

**Made with ❤️ for Affiliate Marketers**
