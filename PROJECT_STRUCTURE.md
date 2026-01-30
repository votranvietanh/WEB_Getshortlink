# Project Structure - Shopee Affiliate Link Shortener

## Cấu Trúc Thư Mục Tổng Thể

```
WEB_Getshortlink/
│
├── backend/                          # Spring Boot Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/affiliate/shortlink/
│   │   │   │   ├── config/          # Configuration classes
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   ├── RedisConfig.java
│   │   │   │   │   ├── SwaggerConfig.java
│   │   │   │   │   ├── CorsConfig.java
│   │   │   │   │   └── ShopeeApiConfig.java
│   │   │   │   │
│   │   │   │   ├── controller/      # REST Controllers
│   │   │   │   │   ├── AuthController.java
│   │   │   │   │   ├── LinkController.java
│   │   │   │   │   ├── ShopeeController.java
│   │   │   │   │   ├── AnalyticsController.java
│   │   │   │   │   ├── UserController.java
│   │   │   │   │   └── RedirectController.java
│   │   │   │   │
│   │   │   │   ├── service/         # Business Logic
│   │   │   │   │   ├── impl/
│   │   │   │   │   │   ├── AuthServiceImpl.java
│   │   │   │   │   │   ├── LinkServiceImpl.java
│   │   │   │   │   │   ├── ShopeeApiServiceImpl.java
│   │   │   │   │   │   ├── AnalyticsServiceImpl.java
│   │   │   │   │   │   └── UserServiceImpl.java
│   │   │   │   │   ├── AuthService.java
│   │   │   │   │   ├── LinkService.java
│   │   │   │   │   ├── ShopeeApiService.java
│   │   │   │   │   ├── AnalyticsService.java
│   │   │   │   │   └── UserService.java
│   │   │   │   │
│   │   │   │   ├── repository/      # Data Access Layer
│   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   ├── LinkRepository.java
│   │   │   │   │   ├── ClickRepository.java
│   │   │   │   │   ├── ProductRepository.java
│   │   │   │   │   ├── ConversionRepository.java
│   │   │   │   │   ├── CampaignRepository.java
│   │   │   │   │   └── TagRepository.java
│   │   │   │   │
│   │   │   │   ├── model/           # Domain Models
│   │   │   │   │   ├── entity/      # JPA Entities
│   │   │   │   │   │   ├── User.java
│   │   │   │   │   │   ├── AffiliateLink.java
│   │   │   │   │   │   ├── Click.java
│   │   │   │   │   │   ├── Product.java
│   │   │   │   │   │   ├── Conversion.java
│   │   │   │   │   │   ├── Campaign.java
│   │   │   │   │   │   ├── Tag.java
│   │   │   │   │   │   └── ApiKey.java
│   │   │   │   │   │
│   │   │   │   │   ├── dto/         # Data Transfer Objects
│   │   │   │   │   │   ├── request/
│   │   │   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   │   │   ├── CreateLinkRequest.java
│   │   │   │   │   │   │   └── UpdateLinkRequest.java
│   │   │   │   │   │   │
│   │   │   │   │   │   └── response/
│   │   │   │   │   │       ├── AuthResponse.java
│   │   │   │   │   │       ├── LinkResponse.java
│   │   │   │   │   │       ├── ProductResponse.java
│   │   │   │   │   │       ├── AnalyticsResponse.java
│   │   │   │   │   │       └── ApiResponse.java
│   │   │   │   │   │
│   │   │   │   │   └── enums/       # Enumerations
│   │   │   │   │       ├── UserRole.java
│   │   │   │   │       ├── LinkStatus.java
│   │   │   │   │       ├── DeviceType.java
│   │   │   │   │       └── ConversionStatus.java
│   │   │   │   │
│   │   │   │   ├── security/        # Security Components
│   │   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   │   │
│   │   │   │   ├── exception/       # Exception Handling
│   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   │   ├── BadRequestException.java
│   │   │   │   │   ├── UnauthorizedException.java
│   │   │   │   │   └── ShopeeApiException.java
│   │   │   │   │
│   │   │   │   ├── util/            # Utility Classes
│   │   │   │   │   ├── LinkGenerator.java
│   │   │   │   │   ├── UrlValidator.java
│   │   │   │   │   ├── QRCodeGenerator.java
│   │   │   │   │   ├── DateUtils.java
│   │   │   │   │   └── IpUtils.java
│   │   │   │   │
│   │   │   │   ├── aspect/          # AOP Aspects
│   │   │   │   │   ├── LoggingAspect.java
│   │   │   │   │   └── RateLimitAspect.java
│   │   │   │   │
│   │   │   │   └── Application.java # Main Application Class
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml
│   │   │       ├── application-prod.yml
│   │   │       ├── application-docker.yml
│   │   │       ├── db/
│   │   │       │   └── migration/   # Flyway Migrations
│   │   │       │       ├── V1__create_users_table.sql
│   │   │       │       ├── V2__create_links_table.sql
│   │   │       │       ├── V3__create_clicks_table.sql
│   │   │       │       └── V4__create_products_table.sql
│   │   │       │
│   │   │       ├── static/
│   │   │       └── templates/
│   │   │
│   │   └── test/
│   │       └── java/com/affiliate/shortlink/
│   │           ├── controller/
│   │           ├── service/
│   │           ├── repository/
│   │           └── integration/
│   │
│   ├── pom.xml                      # Maven Configuration
│   ├── Dockerfile                   # Docker Build File
│   └── .dockerignore
│
├── frontend/                        # Vue.js Application
│   ├── public/
│   │   ├── favicon.ico
│   │   └── index.html
│   │
│   ├── src/
│   │   ├── assets/                  # Static Assets
│   │   │   ├── images/
│   │   │   ├── fonts/
│   │   │   └── styles/
│   │   │       ├── main.css
│   │   │       ├── variables.css
│   │   │       └── utilities.css
│   │   │
│   │   ├── components/              # Vue Components
│   │   │   ├── common/              # Reusable Components
│   │   │   │   ├── Button.vue
│   │   │   │   ├── Input.vue
│   │   │   │   ├── Modal.vue
│   │   │   │   ├── Card.vue
│   │   │   │   ├── Table.vue
│   │   │   │   ├── Pagination.vue
│   │   │   │   └── Loading.vue
│   │   │   │
│   │   │   ├── layout/              # Layout Components
│   │   │   │   ├── Header.vue
│   │   │   │   ├── Sidebar.vue
│   │   │   │   ├── Footer.vue
│   │   │   │   └── MainLayout.vue
│   │   │   │
│   │   │   ├── auth/                # Authentication Components
│   │   │   │   ├── LoginForm.vue
│   │   │   │   ├── RegisterForm.vue
│   │   │   │   └── ForgotPassword.vue
│   │   │   │
│   │   │   ├── link/                # Link Management Components
│   │   │   │   ├── LinkForm.vue
│   │   │   │   ├── LinkCard.vue
│   │   │   │   ├── LinkList.vue
│   │   │   │   ├── LinkDetail.vue
│   │   │   │   └── QRCodeDisplay.vue
│   │   │   │
│   │   │   ├── shopee/              # Shopee Integration Components
│   │   │   │   ├── ProductSearch.vue
│   │   │   │   ├── ProductCard.vue
│   │   │   │   └── ProductDetail.vue
│   │   │   │
│   │   │   └── analytics/           # Analytics Components
│   │   │       ├── Dashboard.vue
│   │   │       ├── ClickChart.vue
│   │   │       ├── ConversionChart.vue
│   │   │       ├── GeographicMap.vue
│   │   │       └── StatCard.vue
│   │   │
│   │   ├── views/                   # Page Views
│   │   │   ├── Home.vue
│   │   │   ├── Login.vue
│   │   │   ├── Register.vue
│   │   │   ├── Dashboard.vue
│   │   │   ├── LinkManager.vue
│   │   │   ├── CreateLink.vue
│   │   │   ├── Analytics.vue
│   │   │   ├── Profile.vue
│   │   │   ├── Settings.vue
│   │   │   └── NotFound.vue
│   │   │
│   │   ├── router/                  # Vue Router
│   │   │   └── index.js
│   │   │
│   │   ├── store/                   # Vuex Store
│   │   │   ├── index.js            # Store configuration
│   │   │   ├── modules/
│   │   │   │   ├── auth.js
│   │   │   │   ├── link.js
│   │   │   │   ├── shopee.js
│   │   │   │   ├── analytics.js
│   │   │   │   └── ui.js
│   │   │
│   │   ├── services/                # API Services
│   │   │   ├── api.js               # Axios Instance
│   │   │   ├── authService.js
│   │   │   ├── linkService.js
│   │   │   ├── shopeeService.js
│   │   │   ├── analyticsService.js
│   │   │   └── userService.js
│   │   │
│   │   ├── utils/                   # Utility Functions
│   │   │   ├── validators.js
│   │   │   ├── formatters.js
│   │   │   ├── constants.js
│   │   │   ├── helpers.js
│   │   │   └── storage.js
│   │   │
│   │   ├── mixins/                  # Vue Mixins
│   │   │   ├── authMixin.js
│   │   │   ├── linkMixin.js
│   │   │   └── notificationMixin.js
│   │   │
│   │   ├── App.vue                  # Root Component
│   │   └── main.js                  # Entry Point
│   │
│   ├── package.json
│   ├── vue.config.js                # Vue CLI Configuration
│   ├── babel.config.js              # Babel Configuration
│   ├── .eslintrc.js                 # ESLint Configuration
│   ├── Dockerfile
│   ├── .env.example
│   └── .dockerignore
│
├── database/                        # Database Scripts
│   ├── schema.sql                   # Complete Schema
│   ├── init.sql                     # Initialization Script
│   ├── seed.sql                     # Sample Data
│   └── migrations/                  # Manual Migrations
│
├── nginx/                           # Nginx Configuration
│   ├── nginx.conf
│   ├── ssl/
│   │   ├── cert.pem
│   │   └── key.pem
│   └── conf.d/
│       └── default.conf
│
├── scripts/                         # Utility Scripts
│   ├── backup-db.sh
│   ├── restore-db.sh
│   ├── deploy.sh
│   └── health-check.sh
│
├── docs/                            # Documentation
│   ├── api/
│   │   └── swagger.json
│   ├── guides/
│   │   ├── getting-started.md
│   │   └── deployment.md
│   └── diagrams/
│       ├── architecture.png
│       └── database-erd.png
│
├── .github/                         # GitHub Configuration
│   └── workflows/
│       ├── deploy.yml
│       ├── test.yml
│       └── build.yml
│
├── docker-compose.yml               # Docker Compose Config
├── docker-compose.prod.yml          # Production Docker Compose
├── .env.example                     # Environment Template
├── .gitignore                       # Git Ignore Rules
│
├── ARCHITECTURE.md                  # Architecture Documentation
├── FEATURES.md                      # Features Documentation
├── API.md                           # API Documentation
├── DATABASE.md                      # Database Documentation
├── DEPLOYMENT.md                    # Deployment Guide
├── SHOPEE_INTEGRATION.md            # Shopee Integration Guide
├── README.md                        # Main README
└── LICENSE                          # License File
```

---

## Chi Tiết Các Module

### Backend Modules

#### 1. Config Module
- **SecurityConfig**: JWT, CORS, Security rules
- **RedisConfig**: Redis connection, cache configuration
- **SwaggerConfig**: API documentation
- **ShopeeApiConfig**: Shopee API credentials

#### 2. Controller Module
- **AuthController**: Login, Register, Logout
- **LinkController**: CRUD operations for links
- **ShopeeController**: Shopee API integration
- **AnalyticsController**: Statistics and reports
- **RedirectController**: Short link redirection

#### 3. Service Module
- **AuthService**: Authentication logic
- **LinkService**: Link management logic
- **ShopeeApiService**: Shopee API calls
- **AnalyticsService**: Data analysis
- **UserService**: User management

#### 4. Repository Module
- **UserRepository**: User data access
- **LinkRepository**: Link data access
- **ClickRepository**: Click tracking
- **ProductRepository**: Product cache

#### 5. Security Module
- **JwtTokenProvider**: JWT generation/validation
- **JwtAuthenticationFilter**: Request authentication
- **UserDetailsServiceImpl**: User loading

---

### Frontend Modules

#### 1. Components
- **common/**: Reusable UI components
- **layout/**: Page layout components
- **auth/**: Authentication forms
- **link/**: Link management UI
- **shopee/**: Shopee product search
- **analytics/**: Charts and statistics

#### 2. Views
- **Home**: Landing page
- **Dashboard**: Main dashboard
- **LinkManager**: Link management page
- **Analytics**: Analytics page

#### 3. Store (Vuex)
- **auth**: Authentication state and actions
- **link**: Links state and mutations
- **shopee**: Shopee products state
- **analytics**: Analytics data state
- **ui**: UI state (modals, loading, etc.)

#### 4. Services
- **api.js**: Axios configuration
- **authService.js**: Auth API calls
- **linkService.js**: Link API calls
- **shopeeService.js**: Shopee API calls

---

## File Naming Conventions

### Backend (Java)
- **Classes**: PascalCase (e.g., `UserService.java`)
- **Interfaces**: PascalCase with 'I' prefix optional
- **Constants**: UPPER_SNAKE_CASE
- **Variables**: camelCase

### Frontend (JavaScript/Vue)
- **Components**: PascalCase (e.g., `LinkCard.vue`)
- **Files**: camelCase (e.g., `authService.js`)
- **Constants**: UPPER_SNAKE_CASE
- **Variables**: camelCase

---

## Technology Stack Summary

### Backend
- **Java 8** (LTS version, widely compatible)
- **Spring Boot 2.7.x** (latest version supporting Java 8)
- **Spring Security** (JWT authentication)
- **Spring Data JPA** (ORM layer)
- **H2 Database** (Development - in-memory, zero config)
- **MySQL 5.7+** (Production - reliable and popular)
- **Redis** (Optional - for caching in production)
- **JWT** (JSON Web Tokens for auth)
- **Swagger 2.x** (API documentation)

### Frontend
- **Vue.js 2.6.14** (stable, mature ecosystem)
- **Vue CLI 4.x** (project scaffolding and build)
- **Vuex 3.x** (state management)
- **Vue Router 3.x** (routing)
- **Axios** (HTTP client)
- **Element UI 2.15.x** (UI component library)
- **Chart.js** (data visualization)

### DevOps
- **Docker** (containerization)
- **Docker Compose** (multi-container orchestration)
- **Nginx** (reverse proxy)
- **Maven** (Java build tool)
- **NPM** (JavaScript package manager)

---

## Development Workflow

1. **Backend Development**
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

2. **Frontend Development**
   ```bash
   cd frontend
   npm run dev
   ```

3. **Database Setup**
   ```bash
   # Development: H2 runs automatically, no setup needed
   
   # Production (MySQL):
   mysql -u root -p -e "CREATE DATABASE affiliate_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
   mysql -u root -p affiliate_db < database/schema.sql
   ```

4. **Docker Development**
   ```bash
   docker-compose up -d
   ```

---

## Build & Deploy

### Development Build
```bash
# Backend
cd backend && ./mvnw clean package

# Frontend
cd frontend && npm run build
```

### Production Deploy
```bash
# Using Docker
docker-compose -f docker-compose.prod.yml up -d

# Manual Deploy
./scripts/deploy.sh
```

---

## Testing Structure

### Backend Tests
```
src/test/java/
├── controller/          # Controller tests
├── service/            # Service tests
├── repository/         # Repository tests
└── integration/        # Integration tests
```

### Frontend Tests
```
src/
├── __tests__/
│   ├── unit/          # Unit tests
│   ├── integration/   # Integration tests
│   └── e2e/           # End-to-end tests
```

---

## Monitoring & Logs

### Log Locations
- **Backend**: `logs/application.log`
- **Nginx**: `/var/log/nginx/`
- **PostgreSQL**: `/var/log/postgresql/`
- **Docker**: `docker-compose logs`

### Monitoring Endpoints
- **Health**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Info**: `/actuator/info`

---

## Security Considerations

1. **Environment Variables**: Never commit `.env` files
2. **API Keys**: Store in environment variables
3. **Passwords**: Use BCrypt hashing
4. **JWT**: Secure secret key
5. **HTTPS**: Always use in production
6. **CORS**: Configure allowed origins
7. **Rate Limiting**: Prevent abuse

---

## Maintenance

### Daily Tasks
- Check logs for errors
- Monitor API usage
- Review analytics

### Weekly Tasks
- Database backup
- Security updates
- Performance review

### Monthly Tasks
- Full system backup
- Security audit
- Dependency updates

---

## Support & Resources

- **Documentation**: `/docs`
- **API Docs**: `http://localhost:8080/swagger-ui.html`
- **GitHub**: Repository URL
- **Issues**: GitHub Issues
- **Email**: support@example.com
