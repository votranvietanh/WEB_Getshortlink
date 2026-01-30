# Phase 1: Get Short Link Affiliate - Implementation Guide

## 🎯 Mục Tiêu Phase 1

Tạo chức năng cơ bản để **tạo link rút gọn từ URL affiliate Shopee** và **redirect khi click vào link**.

### Scope
- ✅ Tạo short link từ URL Shopee
- ✅ Lưu link vào database
- ✅ Redirect khi click vào short link
- ✅ Track số lượt click cơ bản
- ❌ Analytics nâng cao (Phase 2)
- ❌ Shopee API integration (Phase 2)
- ❌ User authentication (Phase 2)

---

## 🛠️ Technology Stack (Local Development)

### Backend
- **Java**: 8
- **Spring Boot**: 2.7.18
- **Database**: MySQL 8.0
- **Build Tool**: Maven 3.6+

### Frontend
- **Vue.js**: 2.6.14
- **UI Framework**: Element UI 2.15.x
- **State Management**: Vuex 3.x
- **Build Tool**: Vue CLI / Webpack

### Database
- **MySQL 8.0**: Dễ cài đặt, phổ biến, tốt cho local development
- **Alternative**: H2 Database (in-memory, không cần cài đặt)

---

## 📊 Database Schema (Phase 1 - Minimal)

### Table: `affiliate_links`

```sql
CREATE TABLE affiliate_links (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    short_code VARCHAR(20) UNIQUE NOT NULL,
    original_url TEXT NOT NULL,
    click_count BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_short_code (short_code),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### Sample Data

```sql
INSERT INTO affiliate_links (short_code, original_url) VALUES
('abc123', 'https://shopee.vn/product/123456?af_siteid=YOUR_ID'),
('xyz789', 'https://shopee.vn/product/789012?af_siteid=YOUR_ID');
```

---

## 🏗️ Backend Implementation (Spring Boot 2.7 + Java 8)

### 1. Project Setup

#### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.18</version>
        <relativePath/>
    </parent>
    
    <groupId>com.affiliate</groupId>
    <artifactId>shortlink</artifactId>
    <version>1.0.0</version>
    <name>Shopee Affiliate Link Shortener</name>
    
    <properties>
        <java.version>1.8</java.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Spring Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <!-- MySQL Driver -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.33</version>
        </dependency>
        
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- Swagger -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>3.0.0</version>
        </dependency>
        
        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

#### application.yml

```yaml
spring:
  application:
    name: shortlink-service
  
  datasource:
    url: jdbc:mysql://localhost:3306/affiliate_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true

server:
  port: 8080
  
# Base URL for short links
app:
  base-url: http://localhost:8080
  short-code-length: 6
```

### 2. Entity

```java
package com.affiliate.shortlink.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "affiliate_links")
public class AffiliateLink {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "short_code", unique = true, nullable = false, length = 20)
    private String shortCode;
    
    @Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
    private String originalUrl;
    
    @Column(name = "click_count")
    private Long clickCount = 0L;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

### 3. Repository

```java
package com.affiliate.shortlink.repository;

import com.affiliate.shortlink.entity.AffiliateLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AffiliateLinkRepository extends JpaRepository<AffiliateLink, Long> {
    
    Optional<AffiliateLink> findByShortCode(String shortCode);
    
    boolean existsByShortCode(String shortCode);
}
```

### 4. DTO

```java
package com.affiliate.shortlink.dto;

import lombok.Data;

@Data
public class CreateLinkRequest {
    private String originalUrl;
    private String customAlias; // Optional
}

@Data
public class LinkResponse {
    private Long id;
    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private Long clickCount;
    private String createdAt;
}

@Data
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
```

### 5. Service

```java
package com.affiliate.shortlink.service;

import com.affiliate.shortlink.dto.CreateLinkRequest;
import com.affiliate.shortlink.dto.LinkResponse;
import com.affiliate.shortlink.entity.AffiliateLink;
import com.affiliate.shortlink.repository.AffiliateLinkRepository;
import com.affiliate.shortlink.util.ShortCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LinkService {
    
    @Autowired
    private AffiliateLinkRepository linkRepository;
    
    @Value("${app.base-url}")
    private String baseUrl;
    
    @Value("${app.short-code-length}")
    private int shortCodeLength;
    
    @Transactional
    public LinkResponse createShortLink(CreateLinkRequest request) {
        // Validate URL
        if (!isValidShopeeUrl(request.getOriginalUrl())) {
            throw new IllegalArgumentException("Invalid Shopee URL");
        }
        
        // Generate or use custom short code
        String shortCode;
        if (request.getCustomAlias() != null && !request.getCustomAlias().isEmpty()) {
            if (linkRepository.existsByShortCode(request.getCustomAlias())) {
                throw new IllegalArgumentException("Custom alias already exists");
            }
            shortCode = request.getCustomAlias();
        } else {
            shortCode = generateUniqueShortCode();
        }
        
        // Create and save link
        AffiliateLink link = new AffiliateLink();
        link.setShortCode(shortCode);
        link.setOriginalUrl(request.getOriginalUrl());
        link.setClickCount(0L);
        
        AffiliateLink saved = linkRepository.save(link);
        
        return toResponse(saved);
    }
    
    @Transactional
    public String redirect(String shortCode) {
        AffiliateLink link = linkRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new IllegalArgumentException("Short link not found"));
        
        // Increment click count
        link.setClickCount(link.getClickCount() + 1);
        linkRepository.save(link);
        
        return link.getOriginalUrl();
    }
    
    public List<LinkResponse> getAllLinks() {
        return linkRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    public LinkResponse getLinkByShortCode(String shortCode) {
        AffiliateLink link = linkRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new IllegalArgumentException("Short link not found"));
        return toResponse(link);
    }
    
    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = ShortCodeGenerator.generate(shortCodeLength);
        } while (linkRepository.existsByShortCode(shortCode));
        return shortCode;
    }
    
    private boolean isValidShopeeUrl(String url) {
        return url != null && url.contains("shopee.vn");
    }
    
    private LinkResponse toResponse(AffiliateLink link) {
        LinkResponse response = new LinkResponse();
        response.setId(link.getId());
        response.setShortCode(link.getShortCode());
        response.setShortUrl(baseUrl + "/" + link.getShortCode());
        response.setOriginalUrl(link.getOriginalUrl());
        response.setClickCount(link.getClickCount());
        response.setCreatedAt(link.getCreatedAt()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }
}
```

### 6. Utility - Short Code Generator

```java
package com.affiliate.shortlink.util;

import java.security.SecureRandom;

public class ShortCodeGenerator {
    
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
```

### 7. Controller

```java
package com.affiliate.shortlink.controller;

import com.affiliate.shortlink.dto.ApiResponse;
import com.affiliate.shortlink.dto.CreateLinkRequest;
import com.affiliate.shortlink.dto.LinkResponse;
import com.affiliate.shortlink.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class LinkController {
    
    @Autowired
    private LinkService linkService;
    
    // Create short link
    @PostMapping("/api/links")
    public ResponseEntity<ApiResponse<LinkResponse>> createLink(
            @RequestBody CreateLinkRequest request) {
        try {
            LinkResponse link = linkService.createShortLink(request);
            return ResponseEntity.ok(ApiResponse.success(link));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // Get all links
    @GetMapping("/api/links")
    public ResponseEntity<ApiResponse<List<LinkResponse>>> getAllLinks() {
        List<LinkResponse> links = linkService.getAllLinks();
        return ResponseEntity.ok(ApiResponse.success(links));
    }
    
    // Get link by short code
    @GetMapping("/api/links/{shortCode}")
    public ResponseEntity<ApiResponse<LinkResponse>> getLink(
            @PathVariable String shortCode) {
        try {
            LinkResponse link = linkService.getLinkByShortCode(shortCode);
            return ResponseEntity.ok(ApiResponse.success(link));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // Redirect short link
    @GetMapping("/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {
        try {
            String originalUrl = linkService.redirect(shortCode);
            return new RedirectView(originalUrl);
        } catch (IllegalArgumentException e) {
            return new RedirectView("/404");
        }
    }
}
```

### 8. Main Application

```java
package com.affiliate.shortlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShortlinkApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ShortlinkApplication.class, args);
    }
}
```

---

## 🎨 Frontend Implementation (Vue 2 + Element UI)

### 1. Project Setup

```bash
# Install Vue CLI
npm install -g @vue/cli

# Create project
vue create frontend

# Select: Vue 2, Babel, Router, Vuex, CSS Pre-processors (Sass)

cd frontend

# Install Element UI
npm install element-ui

# Install Axios
npm install axios
```

### 2. Main.js

```javascript
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'

Vue.use(ElementUI)

// Configure Axios
axios.defaults.baseURL = 'http://localhost:8080'
Vue.prototype.$http = axios

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
```

### 3. API Service

```javascript
// src/services/api.js
import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

export default {
  // Create short link
  createLink(data) {
    return axios.post(`${API_BASE_URL}/links`, data)
  },
  
  // Get all links
  getAllLinks() {
    return axios.get(`${API_BASE_URL}/links`)
  },
  
  // Get link by short code
  getLink(shortCode) {
    return axios.get(`${API_BASE_URL}/links/${shortCode}`)
  }
}
```

### 4. Vuex Store

```javascript
// src/store/index.js
import Vue from 'vue'
import Vuex from 'vuex'
import api from '@/services/api'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    links: [],
    loading: false,
    error: null
  },
  
  mutations: {
    SET_LINKS(state, links) {
      state.links = links
    },
    SET_LOADING(state, loading) {
      state.loading = loading
    },
    SET_ERROR(state, error) {
      state.error = error
    },
    ADD_LINK(state, link) {
      state.links.unshift(link)
    }
  },
  
  actions: {
    async fetchLinks({ commit }) {
      commit('SET_LOADING', true)
      try {
        const response = await api.getAllLinks()
        commit('SET_LINKS', response.data.data)
      } catch (error) {
        commit('SET_ERROR', error.message)
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async createLink({ commit }, data) {
      commit('SET_LOADING', true)
      try {
        const response = await api.createLink(data)
        commit('ADD_LINK', response.data.data)
        return response.data.data
      } catch (error) {
        commit('SET_ERROR', error.message)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    }
  }
})
```

### 5. Create Link Component

```vue
<!-- src/components/CreateLink.vue -->
<template>
  <el-card class="create-link-card">
    <div slot="header">
      <h2>Tạo Link Rút Gọn</h2>
    </div>
    
    <el-form :model="form" :rules="rules" ref="form" label-width="120px">
      <el-form-item label="URL Shopee" prop="originalUrl">
        <el-input
          v-model="form.originalUrl"
          placeholder="https://shopee.vn/product/..."
        ></el-input>
      </el-form-item>
      
      <el-form-item label="Custom Alias">
        <el-input
          v-model="form.customAlias"
          placeholder="my-product (optional)"
        ></el-input>
      </el-form-item>
      
      <el-form-item>
        <el-button
          type="primary"
          @click="submitForm"
          :loading="loading"
        >
          Tạo Link
        </el-button>
      </el-form-item>
    </el-form>
    
    <!-- Result -->
    <el-alert
      v-if="result"
      :title="'Link đã tạo: ' + result.shortUrl"
      type="success"
      :closable="false"
      show-icon
    >
      <el-button
        size="small"
        @click="copyToClipboard(result.shortUrl)"
      >
        Copy
      </el-button>
    </el-alert>
  </el-card>
</template>

<script>
export default {
  name: 'CreateLink',
  
  data() {
    return {
      form: {
        originalUrl: '',
        customAlias: ''
      },
      rules: {
        originalUrl: [
          { required: true, message: 'Vui lòng nhập URL', trigger: 'blur' },
          { 
            pattern: /shopee\.vn/, 
            message: 'URL phải là link Shopee', 
            trigger: 'blur' 
          }
        ]
      },
      result: null,
      loading: false
    }
  },
  
  methods: {
    async submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            const link = await this.$store.dispatch('createLink', this.form)
            this.result = link
            this.$message.success('Tạo link thành công!')
            this.form.originalUrl = ''
            this.form.customAlias = ''
          } catch (error) {
            this.$message.error('Lỗi: ' + error.message)
          } finally {
            this.loading = false
          }
        }
      })
    },
    
    copyToClipboard(text) {
      navigator.clipboard.writeText(text)
      this.$message.success('Đã copy vào clipboard!')
    }
  }
}
</script>

<style scoped>
.create-link-card {
  max-width: 600px;
  margin: 20px auto;
}
</style>
```

### 6. Link List Component

```vue
<!-- src/components/LinkList.vue -->
<template>
  <el-card class="link-list-card">
    <div slot="header">
      <h2>Danh Sách Link</h2>
    </div>
    
    <el-table
      :data="links"
      v-loading="loading"
      style="width: 100%"
    >
      <el-table-column prop="shortCode" label="Short Code" width="120">
      </el-table-column>
      
      <el-table-column label="Short URL" width="200">
        <template slot-scope="scope">
          <el-link :href="scope.row.shortUrl" target="_blank">
            {{ scope.row.shortUrl }}
          </el-link>
        </template>
      </el-table-column>
      
      <el-table-column prop="originalUrl" label="Original URL" show-overflow-tooltip>
      </el-table-column>
      
      <el-table-column prop="clickCount" label="Clicks" width="80">
      </el-table-column>
      
      <el-table-column label="Actions" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="copyLink(scope.row.shortUrl)"
          >
            Copy
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
export default {
  name: 'LinkList',
  
  computed: {
    links() {
      return this.$store.state.links
    },
    loading() {
      return this.$store.state.loading
    }
  },
  
  mounted() {
    this.$store.dispatch('fetchLinks')
  },
  
  methods: {
    copyLink(url) {
      navigator.clipboard.writeText(url)
      this.$message.success('Đã copy link!')
    }
  }
}
</script>

<style scoped>
.link-list-card {
  margin: 20px;
}
</style>
```

### 7. Main View

```vue
<!-- src/views/Home.vue -->
<template>
  <div class="home">
    <h1>Shopee Affiliate Link Shortener</h1>
    <CreateLink />
    <LinkList />
  </div>
</template>

<script>
import CreateLink from '@/components/CreateLink.vue'
import LinkList from '@/components/LinkList.vue'

export default {
  name: 'Home',
  components: {
    CreateLink,
    LinkList
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}

h1 {
  text-align: center;
  color: #409EFF;
}
</style>
```

---

## 🚀 Quick Start

### 1. Setup MySQL

```sql
-- Create database
CREATE DATABASE affiliate_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use database
USE affiliate_db;

-- Table will be auto-created by Hibernate
```

### 2. Run Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend chạy tại: http://localhost:8080

### 3. Run Frontend

```bash
cd frontend
npm install
npm run serve
```

Frontend chạy tại: http://localhost:8081

---

## ✅ Testing Phase 1

### Test Create Link

```bash
curl -X POST http://localhost:8080/api/links \
  -H "Content-Type: application/json" \
  -d '{
    "originalUrl": "https://shopee.vn/product/123456?af_siteid=YOUR_ID"
  }'
```

### Test Redirect

```bash
# Visit in browser
http://localhost:8080/abc123
```

### Test Get All Links

```bash
curl http://localhost:8080/api/links
```

---

## 📋 Phase 1 Checklist

- [ ] MySQL database setup
- [ ] Backend project created
- [ ] Entity & Repository implemented
- [ ] Service layer implemented
- [ ] REST API endpoints working
- [ ] Frontend project created
- [ ] Create Link form working
- [ ] Link list display working
- [ ] Redirect functionality working
- [ ] Click counting working

---

## 🎯 Next Steps (Phase 2)

- [ ] User authentication (JWT)
- [ ] Shopee API integration
- [ ] Advanced analytics
- [ ] QR code generation
- [ ] Custom domain support

---

**Phase 1 Complete = Working Short Link System! 🎉**
