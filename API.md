# API Documentation - Shopee Affiliate Link Shortener

## Base URL
```
Development: http://localhost:8080/api/v1
Production: https://api.yourdom ain.com/api/v1
```

## Authentication

All protected endpoints require JWT token in header:
```
Authorization: Bearer <your_jwt_token>
```

---

## 1. Authentication APIs

### 1.1. Register
**POST** `/auth/register`

Request:
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "fullName": "John Doe"
}
```

Response:
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "userId": 1,
    "username": "john_doe",
    "email": "john@example.com"
  }
}
```

### 1.2. Login
**POST** `/auth/login`

Request:
```json
{
  "username": "john_doe",
  "password": "SecurePass123!"
}
```

Response:
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
}
```

---

## 2. Link Management APIs

### 2.1. Create Short Link
**POST** `/links`

Request:
```json
{
  "originalUrl": "https://shopee.vn/product/123456",
  "customAlias": "iphone15",
  "title": "iPhone 15 Pro Max",
  "description": "Sản phẩm hot",
  "expiresAt": "2024-12-31T23:59:59"
}
```

Response:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "shortCode": "abc123",
    "shortUrl": "https://short.link/abc123",
    "originalUrl": "https://shopee.vn/product/123456",
    "qrCodeUrl": "https://api.qrserver.com/v1/create-qr-code/?data=...",
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

### 2.2. Get User Links
**GET** `/links?page=0&size=20&sort=createdAt,desc`

Response:
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "shortCode": "abc123",
        "shortUrl": "https://short.link/abc123",
        "title": "iPhone 15 Pro Max",
        "clickCount": 150,
        "conversionCount": 5,
        "status": "ACTIVE",
        "createdAt": "2024-01-15T10:30:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 5,
    "currentPage": 0
  }
}
```

### 2.3. Get Link Details
**GET** `/links/{id}`

Response:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "shortCode": "abc123",
    "originalUrl": "https://shopee.vn/product/123456",
    "title": "iPhone 15 Pro Max",
    "clickCount": 150,
    "uniqueClicks": 120,
    "conversionCount": 5,
    "revenue": 1500000,
    "product": {
      "name": "iPhone 15 Pro Max",
      "price": 30000000,
      "image": "https://...",
      "rating": 4.8
    }
  }
}
```

### 2.4. Delete Link
**DELETE** `/links/{id}`

Response:
```json
{
  "success": true,
  "message": "Link deleted successfully"
}
```

---

## 3. Shopee Integration APIs

### 3.1. Search Products
**GET** `/shopee/products/search?keyword=iphone&limit=20&offset=0`

Response:
```json
{
  "success": true,
  "data": {
    "items": [
      {
        "productId": "123456",
        "name": "iPhone 15 Pro Max 256GB",
        "price": 30000000,
        "discountPrice": 28000000,
        "image": "https://...",
        "soldCount": 1500,
        "rating": 4.8,
        "shopName": "Apple Official Store"
      }
    ],
    "total": 500
  }
}
```

### 3.2. Get Product Details
**GET** `/shopee/products/{productId}`

Response:
```json
{
  "success": true,
  "data": {
    "productId": "123456",
    "name": "iPhone 15 Pro Max 256GB",
    "price": 30000000,
    "discountPrice": 28000000,
    "images": ["https://...", "https://..."],
    "description": "...",
    "rating": 4.8,
    "reviewCount": 350,
    "soldCount": 1500,
    "commissionRate": 3.5,
    "shop": {
      "name": "Apple Official Store",
      "rating": 4.9
    }
  }
}
```

### 3.3. Create Affiliate Link
**POST** `/shopee/affiliate/create`

Request:
```json
{
  "productUrl": "https://shopee.vn/product/123456",
  "affiliateId": "your_affiliate_id"
}
```

Response:
```json
{
  "success": true,
  "data": {
    "affiliateUrl": "https://shopee.vn/product/123456?af_siteid=...",
    "shortUrl": "https://short.link/xyz789"
  }
}
```

---

## 4. Analytics APIs

### 4.1. Dashboard Overview
**GET** `/analytics/dashboard?startDate=2024-01-01&endDate=2024-01-31`

Response:
```json
{
  "success": true,
  "data": {
    "totalLinks": 150,
    "totalClicks": 5000,
    "totalConversions": 120,
    "totalRevenue": 15000000,
    "clickTrend": [
      {"date": "2024-01-01", "clicks": 150},
      {"date": "2024-01-02", "clicks": 200}
    ],
    "topLinks": [
      {
        "id": 1,
        "title": "iPhone 15",
        "clicks": 500,
        "conversions": 15
      }
    ]
  }
}
```

### 4.2. Link Analytics
**GET** `/analytics/links/{id}?period=7d`

Response:
```json
{
  "success": true,
  "data": {
    "linkId": 1,
    "totalClicks": 500,
    "uniqueClicks": 400,
    "clicksByDate": [...],
    "clicksByCountry": {
      "Vietnam": 450,
      "Thailand": 30,
      "Singapore": 20
    },
    "clicksByDevice": {
      "mobile": 350,
      "desktop": 120,
      "tablet": 30
    },
    "clicksByBrowser": {
      "Chrome": 300,
      "Safari": 150,
      "Firefox": 50
    },
    "topReferrers": [
      {"source": "facebook.com", "clicks": 200},
      {"source": "instagram.com", "clicks": 150}
    ]
  }
}
```

---

## 5. Error Responses

### Standard Error Format
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid input data",
    "details": [
      {
        "field": "email",
        "message": "Email is required"
      }
    ]
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### Common Error Codes
- `UNAUTHORIZED` - 401: Invalid or missing token
- `FORBIDDEN` - 403: Insufficient permissions
- `NOT_FOUND` - 404: Resource not found
- `VALIDATION_ERROR` - 400: Invalid input
- `RATE_LIMIT_EXCEEDED` - 429: Too many requests
- `INTERNAL_ERROR` - 500: Server error

---

## 6. Rate Limiting

- **Free Plan**: 100 requests/hour
- **Pro Plan**: 1000 requests/hour
- **Enterprise**: Unlimited

Headers:
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1642234567
```
