# 📊 Dashboard Enhancements - Date Filter & Export Excel

## ✅ **Đã Thêm**

### 1. **Date Range Filter** - Lọc Theo Thời Gian
### 2. **Export Excel** - Xuất Báo Cáo Excel
### 3. **Microservices Architecture** - Đề xuất thiết kế

---

## 📅 **Date Range Filter**

### UI Component
```vue
<el-date-picker
  v-model="dateRange"
  type="daterange"
  range-separator="-"
  start-placeholder="Từ ngày"
  end-placeholder="Đến ngày"
  format="dd/MM/yyyy"
  value-format="yyyy-MM-dd"
  size="small"
  @change="filterByDate"
  style="width: 280px;"
>
</el-date-picker>
```

### Filter Logic
```javascript
computed: {
  filteredOrders() {
    if (!this.dateRange || this.dateRange.length !== 2) {
      return this.orders
    }
    
    const [startDate, endDate] = this.dateRange
    return this.orders.filter(order => {
      return order.date >= startDate && order.date <= endDate
    })
  }
}
```

### Filter Summary Tag
```vue
<el-tag closable @close="clearDateFilter">
  <i class="el-icon-date"></i> 
  {{ formatDate(dateRange[0]) }} - {{ formatDate(dateRange[1]) }}
  ({{ filteredOrders.length }} đơn)
</el-tag>
```

---

## 📥 **Export Excel**

### Install Package
```bash
npm install xlsx file-saver --save
```

### Import Libraries
```javascript
import * as XLSX from 'xlsx'
import { saveAs } from 'file-saver'
```

### Export Method
```javascript
exportToExcel() {
  this.exporting = true
  
  try {
    // Prepare data
    const data = this.filteredOrders.map(order => ({
      'Mã Đơn': order.orderCode,
      'Sản Phẩm': order.productName,
      'Shop': order.shopName,
      'Giá Trị Đơn': order.orderValue,
      'Hoàn Tiền': order.cashback,
      'Trạng Thái': this.getStatusText(order.status),
      'Ngày': order.date,
      'Ngày Tạo': order.createdAt,
      'Ngày Mua': order.purchasedAt || '',
      'Ngày Hoàn': order.completedAt || ''
    }))
    
    // Create workbook
    const ws = XLSX.utils.json_to_sheet(data)
    const wb = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(wb, ws, 'Đơn Hàng')
    
    // Generate file
    const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'array' })
    const blob = new Blob([wbout], { type: 'application/octet-stream' })
    
    // Download
    const fileName = `don-hang-${this.getExportFileName()}.xlsx`
    saveAs(blob, fileName)
    
    this.$message.success(`Đã xuất ${data.length} đơn hàng`)
  } catch (error) {
    this.$message.error('Lỗi khi xuất file: ' + error.message)
  } finally {
    this.exporting = false
  }
}
```

### Helper Methods
```javascript
getExportFileName() {
  if (this.dateRange && this.dateRange.length === 2) {
    return `${this.dateRange[0]}_${this.dateRange[1]}`
  }
  return new Date().toISOString().split('T')[0]
},

formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getDate()}/${d.getMonth() + 1}/${d.getFullYear()}`
},

clearDateFilter() {
  this.dateRange = null
},

filterByDate() {
  // Automatically filters via computed property
  this.$message.info(`Lọc ${this.filteredOrders.length} đơn hàng`)
}
```

---

## 🎨 **UI Updates**

### Header Actions Group
```vue
<div class="header-actions-group">
  <!-- Date Picker -->
  <el-date-picker ... />
  
  <!-- Export Button -->
  <el-button 
    type="success" 
    icon="el-icon-download"
    @click="exportToExcel"
    :loading="exporting"
  >
    Xuất Excel
  </el-button>
  
  <!-- Create Link -->
  <el-button @click="$router.push('/links/create')">
    Tạo Link Mới
  </el-button>
</div>
```

### CSS
```css
.header-actions-group {
  display: flex;
  align-items: center;
  gap: 12px;
}

.filter-summary {
  padding: 12px 0;
}

@media (max-width: 768px) {
  .header-actions-group {
    flex-direction: column;
    align-items: stretch;
  }
  
  .header-actions-group .el-date-picker,
  .header-actions-group .el-button {
    width: 100%;
  }
}
```

---

## 🏗️ **Microservices Architecture**

### Proposed Architecture

```
┌─────────────────────────────────────────────────┐
│           API Gateway (Kong/Nginx)              │
│         Load Balancer & Rate Limiting           │
└─────────────────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
┌───────▼──────┐ ┌───▼────────┐ ┌──▼──────────┐
│ Auth Service │ │Link Service│ │Order Service│
│  (Port 8081) │ │(Port 8082) │ │ (Port 8083) │
└──────────────┘ └────────────┘ └─────────────┘
        │             │             │
        └─────────────┼─────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
┌───────▼──────┐ ┌───▼────────┐ ┌──▼──────────┐
│Analytics Svc │ │Payment Svc │ │Notification │
│ (Port 8084)  │ │(Port 8085) │ │ (Port 8086) │
└──────────────┘ └────────────┘ └─────────────┘
        │             │             │
        └─────────────┼─────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
┌───────▼──────┐ ┌───▼────────┐ ┌──▼──────────┐
│  PostgreSQL  │ │   Redis    │ │  RabbitMQ   │
│  (Database)  │ │  (Cache)   │ │  (Queue)    │
└──────────────┘ └────────────┘ └─────────────┘
```

---

## 🎯 **Services Breakdown**

### 1. **Auth Service** (Port 8081)
```
Responsibilities:
- User registration/login
- JWT token generation
- Password management
- OAuth integration

Tech Stack:
- Spring Boot + Spring Security
- JWT
- PostgreSQL (users table)
- Redis (session cache)
```

### 2. **Link Service** (Port 8082)
```
Responsibilities:
- Create short links
- Manage affiliate links
- Track clicks
- URL validation

Tech Stack:
- Spring Boot
- PostgreSQL (links table)
- Redis (link cache)
```

### 3. **Order Service** (Port 8083)
```
Responsibilities:
- Track orders
- Calculate cashback
- Order status management
- Integration with Shopee API

Tech Stack:
- Spring Boot
- PostgreSQL (orders table)
- RabbitMQ (order events)
```

### 4. **Analytics Service** (Port 8084)
```
Responsibilities:
- Click tracking
- Revenue analytics
- Performance metrics
- Report generation

Tech Stack:
- Spring Boot
- PostgreSQL (analytics table)
- Redis (metrics cache)
- Elasticsearch (logs)
```

### 5. **Payment Service** (Port 8085)
```
Responsibilities:
- Withdraw requests
- Bank integration
- Transaction history
- Balance management

Tech Stack:
- Spring Boot
- PostgreSQL (transactions table)
- RabbitMQ (payment events)
```

### 6. **Notification Service** (Port 8086)
```
Responsibilities:
- Email notifications
- SMS alerts
- Push notifications
- Event subscribers

Tech Stack:
- Spring Boot
- RabbitMQ (consumer)
- SendGrid/Twilio APIs
```

---

## 🔄 **Communication Patterns**

### Synchronous (REST)
```
Frontend → API Gateway → Service
```

### Asynchronous (Events)
```
Order Created → RabbitMQ → [Analytics, Notification]
Payment Completed → RabbitMQ → [Order, Notification]
```

### Example Event Flow
```
1. User creates link
   ↓
2. Link Service saves to DB
   ↓
3. Publish event: "link.created"
   ↓
4. Analytics Service subscribes
   ↓
5. Update metrics
```

---

## 📦 **Docker Compose**

```yaml
version: '3.8'

services:
  api-gateway:
    image: kong:latest
    ports:
      - "8080:8000"
    
  auth-service:
    build: ./auth-service
    ports:
      - "8081:8081"
    depends_on:
      - postgres
      - redis
    
  link-service:
    build: ./link-service
    ports:
      - "8082:8082"
    depends_on:
      - postgres
      - redis
    
  order-service:
    build: ./order-service
    ports:
      - "8083:8083"
    depends_on:
      - postgres
      - rabbitmq
    
  analytics-service:
    build: ./analytics-service
    ports:
      - "8084:8084"
    
  payment-service:
    build: ./payment-service
    ports:
      - "8085:8085"
    
  notification-service:
    build: ./notification-service
    ports:
      - "8086:8086"
    depends_on:
      - rabbitmq
    
  postgres:
    image: postgres:14
    environment:
      POSTGRES_DB: affiliate_db
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: password
    
  redis:
    image: redis:7
    
  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"
      - "15672:15672"
```

---

## 🚀 **Benefits of Microservices**

### 1. **Scalability**
```
- Scale individual services independently
- Handle high traffic on specific services
- Cost-effective resource allocation
```

### 2. **Resilience**
```
- Service failure doesn't crash entire system
- Circuit breakers prevent cascade failures
- Graceful degradation
```

### 3. **Development Speed**
```
- Teams work independently
- Deploy services separately
- Faster iteration cycles
```

### 4. **Technology Flexibility**
```
- Use best tool for each service
- Easy to upgrade/replace services
- Polyglot persistence
```

---

## 📊 **Monitoring & Observability**

### Tools
```
- Prometheus: Metrics collection
- Grafana: Visualization
- ELK Stack: Logging
- Jaeger: Distributed tracing
- Spring Boot Actuator: Health checks
```

### Key Metrics
```
- Request rate
- Error rate
- Response time
- Service availability
- Database connections
- Queue depth
```

---

## ✅ **Implementation Roadmap**

### Phase 1: Monolith (Current)
```
✅ Single Spring Boot application
✅ All features in one codebase
✅ Single database
```

### Phase 2: Service Extraction
```
1. Extract Auth Service
2. Extract Link Service
3. Setup API Gateway
4. Implement service discovery
```

### Phase 3: Event-Driven
```
1. Setup RabbitMQ
2. Implement event publishers
3. Create event subscribers
4. Async communication
```

### Phase 4: Full Microservices
```
1. Extract remaining services
2. Implement monitoring
3. Setup CI/CD pipelines
4. Load testing & optimization
```

---

**Created**: 2026-01-30  
**Version**: 2.0  
**Status**: ✅ READY FOR IMPLEMENTATION
