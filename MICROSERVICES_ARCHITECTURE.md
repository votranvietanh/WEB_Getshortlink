# 🏗️ Microservices Architecture - Shopee Affiliate System

## 📐 **System Architecture**

```
                    ┌──────────────────────────────────┐
                    │      Frontend (Vue.js)           │
                    │   - Dashboard                    │
                    │   - Link Management              │
                    │   - Analytics                    │
                    └────────────┬─────────────────────┘
                                 │
                                 │ HTTPS
                                 ▼
                    ┌──────────────────────────────────┐
                    │    API Gateway (Kong/Nginx)      │
                    │  - Load Balancing                │
                    │  - Rate Limiting                 │
                    │  - Authentication                │
                    │  - Request Routing               │
                    └────────────┬─────────────────────┘
                                 │
                ┌────────────────┼────────────────┐
                │                │                │
                ▼                ▼                ▼
    ┌───────────────────┐ ┌──────────────┐ ┌──────────────┐
    │  Auth Service     │ │Link Service  │ │Order Service │
    │  :8081            │ │:8082         │ │:8083         │
    │                   │ │              │ │              │
    │ - Register        │ │- Create Link │ │- Track Order │
    │ - Login           │ │- Shorten URL │ │- Cashback    │
    │ - JWT Token       │ │- Click Track │ │- Status      │
    │ - OAuth           │ │- Analytics   │ │- Shopee API  │
    └─────────┬─────────┘ └──────┬───────┘ └──────┬───────┘
              │                  │                │
              │                  │                │
    ┌─────────▼──────────────────▼────────────────▼───────┐
    │                                                      │
    │              Message Queue (RabbitMQ)               │
    │                                                      │
    │  Exchanges:                                          │
    │  - link.events    → link.created, link.clicked      │
    │  - order.events   → order.created, order.completed  │
    │  - payment.events → withdraw.requested              │
    │                                                      │
    └──────────┬───────────────────┬───────────────┬──────┘
               │                   │               │
               ▼                   ▼               ▼
    ┌──────────────────┐ ┌─────────────────┐ ┌──────────────┐
    │Analytics Service │ │Payment Service  │ │Notification  │
    │:8084             │ │:8085            │ │Service :8086 │
    │                  │ │                 │ │              │
    │- Click Metrics   │ │- Withdraw       │ │- Email       │
    │- Revenue Stats   │ │- Bank Transfer  │ │- SMS         │
    │- Reports         │ │- Transaction    │ │- Push        │
    │- Dashboard Data  │ │- Balance        │ │- Webhooks    │
    └────────┬─────────┘ └────────┬────────┘ └──────┬───────┘
             │                    │                  │
             └────────────────────┼──────────────────┘
                                  │
                ┌─────────────────┼─────────────────┐
                │                 │                 │
                ▼                 ▼                 ▼
    ┌───────────────────┐ ┌──────────────┐ ┌──────────────┐
    │   PostgreSQL      │ │    Redis     │ │ Elasticsearch│
    │   (Primary DB)    │ │   (Cache)    │ │   (Logs)     │
    │                   │ │              │ │              │
    │ - users           │ │- Sessions    │ │- App Logs    │
    │ - links           │ │- Link Cache  │ │- Audit Trail │
    │ - orders          │ │- Metrics     │ │- Analytics   │
    │ - transactions    │ │- Rate Limit  │ │- Search      │
    │ - analytics       │ │              │ │              │
    └───────────────────┘ └──────────────┘ └──────────────┘
```

---

## 🎯 **Service Details**

### 1. Auth Service (Port 8081)

**Responsibilities:**
- User authentication & authorization
- JWT token generation & validation
- Password encryption & management
- OAuth 2.0 integration (Google, Facebook)
- Session management

**Endpoints:**
```
POST   /api/auth/register
POST   /api/auth/login
POST   /api/auth/logout
POST   /api/auth/refresh-token
GET    /api/auth/me
PUT    /api/auth/change-password
POST   /api/auth/forgot-password
POST   /api/auth/oauth/google
```

**Database Tables:**
```sql
users (id, email, password_hash, name, role, created_at)
refresh_tokens (id, user_id, token, expires_at)
oauth_accounts (id, user_id, provider, provider_id)
```

**Tech Stack:**
- Spring Boot 3.x
- Spring Security 6.x
- JWT (jjwt library)
- BCrypt for password hashing
- PostgreSQL

---

### 2. Link Service (Port 8082)

**Responsibilities:**
- Create & manage short links
- URL shortening algorithm
- Click tracking
- Link analytics
- QR code generation

**Endpoints:**
```
POST   /api/links                    # Create link
GET    /api/links                    # List user's links
GET    /api/links/{id}               # Get link details
PUT    /api/links/{id}               # Update link
DELETE /api/links/{id}               # Delete link
GET    /api/links/{shortCode}/stats  # Link statistics
GET    /r/{shortCode}                # Redirect endpoint
```

**Database Tables:**
```sql
links (id, user_id, original_url, short_code, title, created_at, expires_at)
clicks (id, link_id, ip_address, user_agent, referer, clicked_at, country, device)
```

**Tech Stack:**
- Spring Boot
- Redis (link cache, rate limiting)
- PostgreSQL
- Base62 encoding for short codes

---

### 3. Order Service (Port 8083)

**Responsibilities:**
- Track Shopee orders
- Calculate cashback
- Order status management
- Shopee GraphAPI integration
- Commission calculation

**Endpoints:**
```
POST   /api/orders                # Create order
GET    /api/orders                # List orders
GET    /api/orders/{id}           # Order details
PUT    /api/orders/{id}/status    # Update status
GET    /api/orders/stats          # Order statistics
POST   /api/orders/sync-shopee    # Sync with Shopee
```

**Database Tables:**
```sql
orders (id, user_id, link_id, order_code, product_name, shop_name, 
        order_value, cashback, status, shopee_order_id, created_at, 
        purchased_at, completed_at)
```

**Tech Stack:**
- Spring Boot
- Shopee GraphAPI Client
- RabbitMQ (order events)
- PostgreSQL

---

### 4. Analytics Service (Port 8084)

**Responsibilities:**
- Aggregate click data
- Revenue analytics
- Performance metrics
- Dashboard statistics
- Report generation

**Endpoints:**
```
GET    /api/analytics/dashboard      # Dashboard stats
GET    /api/analytics/revenue        # Revenue report
GET    /api/analytics/clicks         # Click analytics
GET    /api/analytics/top-links      # Top performing links
GET    /api/analytics/export         # Export to Excel
POST   /api/analytics/custom-report  # Custom report
```

**Database Tables:**
```sql
daily_stats (date, user_id, total_clicks, total_orders, total_revenue)
link_performance (link_id, clicks, conversions, revenue, last_updated)
```

**Tech Stack:**
- Spring Boot
- PostgreSQL (aggregated data)
- Redis (metrics cache)
- Elasticsearch (log analytics)

---

### 5. Payment Service (Port 8085)

**Responsibilities:**
- Withdraw requests
- Bank transfer integration
- Transaction history
- Balance management
- Payment verification

**Endpoints:**
```
POST   /api/payments/withdraw         # Request withdraw
GET    /api/payments/transactions     # Transaction history
GET    /api/payments/balance          # Current balance
PUT    /api/payments/bank-account     # Update bank info
GET    /api/payments/withdraw-history # Withdraw history
```

**Database Tables:**
```sql
balances (user_id, available, pending, total, updated_at)
transactions (id, user_id, type, amount, status, bank_account, created_at, completed_at)
bank_accounts (id, user_id, bank_name, account_number, account_name)
```

**Tech Stack:**
- Spring Boot
- Bank API integration
- RabbitMQ (payment events)
- PostgreSQL

---

### 6. Notification Service (Port 8086)

**Responsibilities:**
- Email notifications
- SMS alerts
- Push notifications
- Event subscribers
- Template management

**Endpoints:**
```
POST   /api/notifications/send-email
POST   /api/notifications/send-sms
POST   /api/notifications/send-push
GET    /api/notifications/templates
POST   /api/notifications/subscribe
```

**Event Subscriptions:**
```
- user.registered       → Welcome email
- order.completed       → Cashback notification
- withdraw.requested    → Confirmation email
- withdraw.completed    → Success notification
- link.clicked          → Real-time alert (optional)
```

**Tech Stack:**
- Spring Boot
- RabbitMQ (event consumer)
- SendGrid (email)
- Twilio (SMS)
- Firebase (push notifications)

---

## 🔄 **Event-Driven Communication**

### Event Flow Examples

#### 1. Link Created Event
```
User creates link
    ↓
Link Service saves to DB
    ↓
Publish: link.created
    ↓
Analytics Service subscribes
    ↓
Update link count metrics
```

#### 2. Order Completed Event
```
Shopee webhook received
    ↓
Order Service updates status
    ↓
Publish: order.completed
    ↓
├─→ Payment Service: Update balance
├─→ Analytics Service: Update revenue
└─→ Notification Service: Send email
```

#### 3. Withdraw Requested Event
```
User requests withdraw
    ↓
Payment Service validates
    ↓
Publish: withdraw.requested
    ↓
├─→ Notification Service: Send confirmation
└─→ Analytics Service: Log transaction
```

---

## 🛡️ **Security & Resilience**

### API Gateway Features
```
1. Rate Limiting
   - Per user: 100 req/min
   - Per IP: 1000 req/min
   
2. Authentication
   - JWT validation
   - API key verification
   
3. Request Filtering
   - SQL injection prevention
   - XSS protection
   
4. Load Balancing
   - Round-robin
   - Least connections
```

### Circuit Breaker Pattern
```java
@CircuitBreaker(name = "shopeeAPI", fallbackMethod = "fallbackGetOrder")
public Order getOrderFromShopee(String orderId) {
    // Call Shopee API
}

public Order fallbackGetOrder(String orderId, Exception e) {
    // Return cached data or default response
}
```

### Retry Mechanism
```java
@Retry(name = "paymentService", maxAttempts = 3)
public void processPayment(Transaction transaction) {
    // Process payment
}
```

---

## 📊 **Monitoring & Observability**

### Metrics Collection
```
Prometheus scrapes:
- /actuator/prometheus (each service)

Metrics:
- http_requests_total
- http_request_duration_seconds
- db_connection_pool_size
- rabbitmq_queue_depth
- cache_hit_rate
```

### Distributed Tracing
```
Jaeger traces:
- Request ID propagation
- Service call chain
- Performance bottlenecks
- Error tracking
```

### Logging
```
ELK Stack:
- Elasticsearch: Store logs
- Logstash: Process logs
- Kibana: Visualize logs

Log Format:
{
  "timestamp": "2026-01-30T15:00:00Z",
  "service": "link-service",
  "level": "INFO",
  "traceId": "abc123",
  "message": "Link created",
  "userId": "user123"
}
```

---

## 🚀 **Deployment Strategy**

### Docker Compose (Development)
```yaml
version: '3.8'
services:
  auth-service:
    build: ./auth-service
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - DB_HOST=postgres
      - REDIS_HOST=redis
    ports:
      - "8081:8081"
```

### Kubernetes (Production)
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: link-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: link-service
  template:
    metadata:
      labels:
        app: link-service
    spec:
      containers:
      - name: link-service
        image: affiliate/link-service:1.0
        ports:
        - containerPort: 8082
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
```

---

## ✅ **Migration Path**

### Current: Monolith
```
Single Spring Boot app
All features in one codebase
Single database
```

### Step 1: Extract Auth Service
```
1. Create new Spring Boot project
2. Move auth code
3. Setup separate database
4. Update API Gateway routing
5. Test & deploy
```

### Step 2: Extract Link Service
```
1. Create link-service project
2. Move link management code
3. Setup Redis cache
4. Update frontend API calls
5. Deploy alongside monolith
```

### Step 3: Setup Event Bus
```
1. Install RabbitMQ
2. Implement event publishers
3. Create event consumers
4. Test async communication
```

### Step 4: Complete Migration
```
1. Extract remaining services
2. Decommission monolith
3. Full microservices architecture
```

---

**Created**: 2026-01-30  
**Status**: 📐 ARCHITECTURE DESIGN  
**Next**: Implementation Phase 1
