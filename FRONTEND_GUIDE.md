# 🚀 Hướng Dẫn Chạy Frontend

## Các Lệnh Có Thể Dùng

### ✅ Cả hai lệnh này đều HOẠT ĐỘNG:

```bash
npm run dev
```

**HOẶC**

```bash
npm run serve
```

Cả hai đều chạy Vue development server!

---

## 📝 Hướng Dẫn Chi Tiết

### Bước 1: Mở Terminal

```bash
# Mở PowerShell hoặc CMD
# Navigate đến thư mục frontend
cd j:\af\WEB_Getshortlink\frontend
```

### Bước 2: Cài Đặt Dependencies (Lần Đầu)

```bash
npm install
```

**Chờ khoảng 2-3 phút** để npm tải tất cả packages.

### Bước 3: Chạy Development Server

```bash
# Option 1
npm run dev

# Option 2
npm run serve
```

### Bước 4: Mở Browser

Sau khi thấy message:

```
  App running at:
  - Local:   http://localhost:8081/
  - Network: http://192.168.x.x:8081/
```

Mở browser và truy cập: **http://localhost:8081**

---

## 🎯 Tất Cả Lệnh NPM

```bash
# Development server (2 cách)
npm run dev      # ✅ Alias mới
npm run serve    # ✅ Vue CLI default

# Build production
npm run build

# Lint code
npm run lint

# Run tests
npm run test:unit
```

---

## 🔥 Quick Start (Copy & Paste)

```bash
# Một lệnh chạy tất cả
cd j:\af\WEB_Getshortlink\frontend && npm install && npm run dev
```

---

## ⚡ Hot Reload

Khi chạy `npm run dev` hoặc `npm run serve`:
- ✅ Code thay đổi → Auto reload
- ✅ Không cần restart server
- ✅ Thay đổi hiển thị ngay lập tức

---

## 🐛 Troubleshooting

### Lỗi: "npm: command not found"

**Giải pháp:** Cài Node.js
```bash
# Download từ: https://nodejs.org/
# Chọn LTS version (14.x hoặc 16.x)
```

### Lỗi: "Port 8081 already in use"

**Giải pháp 1:** Kill process đang dùng port
```bash
# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F
```

**Giải pháp 2:** Đổi port
```javascript
// Sửa trong vue.config.js
devServer: {
  port: 8082  // Đổi sang port khác
}
```

### Lỗi: "Module not found"

**Giải pháp:** Xóa và cài lại
```bash
rm -rf node_modules package-lock.json
npm install
```

### Lỗi: "EACCES permission denied"

**Giải pháp:** Chạy với quyền admin
```bash
# Windows: Mở PowerShell/CMD as Administrator
npm install
```

---

## 📊 So Sánh Lệnh

| Lệnh | Mô Tả | Khi Nào Dùng |
|------|-------|--------------|
| `npm run dev` | Development server | Development |
| `npm run serve` | Development server | Development |
| `npm run build` | Build production | Deploy |
| `npm run lint` | Check code style | Before commit |

---

## 🎨 Development Workflow

```bash
# 1. Start backend (Terminal 1)
cd backend
mvnw.cmd spring-boot:run

# 2. Start frontend (Terminal 2)
cd frontend
npm run dev

# 3. Code và test
# - Frontend: http://localhost:8081
# - Backend: http://localhost:8080
# - Swagger: http://localhost:8080/swagger-ui.html

# 4. Build khi xong
npm run build
```

---

## ✨ Tips

### Tăng Tốc Development

```bash
# Clear cache nếu lỗi lạ
npm cache clean --force

# Update dependencies
npm update

# Check outdated packages
npm outdated
```

### Environment Variables

```bash
# Development
npm run dev
# Sử dụng .env

# Production
npm run build
# Sử dụng .env.production
```

---

## 🚀 Production Build

```bash
# Build static files
npm run build

# Output: dist/ folder

# Serve với web server
# - Nginx
# - Apache
# - Node.js (serve package)
```

---

## 📞 Cần Giúp?

Nếu vẫn gặp lỗi:
1. Check Node.js version: `node -v` (cần >= 12.x)
2. Check NPM version: `npm -v` (cần >= 6.x)
3. Xem logs trong terminal
4. Check `package.json` có đúng không

---

**Happy Coding! 🎉**
