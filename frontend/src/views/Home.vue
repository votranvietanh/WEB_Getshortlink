<template>
  <div class="home">
    <!-- Shopee-style Header -->
    <div class="shopee-hero">
      <div class="shopee-container">
        <div class="hero-content">
          <h1 class="hero-title">
            <i class="el-icon-link"></i>
            Shopee Affiliate Link Shortener
          </h1>
          <p class="hero-subtitle">
            Tạo và quản lý link rút gọn cho Shopee Affiliate một cách dễ dàng
          </p>
          <div class="hero-buttons">
            <el-button type="primary" size="large" @click="$router.push('/dashboard')">
              <i class="el-icon-s-home"></i> Vào Dashboard
            </el-button>
            <el-button size="large" plain @click="$router.push('/links/create')">
              <i class="el-icon-plus"></i> Tạo Link Ngay
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- Features Section -->
    <div class="features-section">
      <div class="shopee-container">
        <h2 class="section-title">Tính Năng Nổi Bật</h2>
        <div class="shopee-grid shopee-grid-3">
          <div class="feature-card">
            <div class="shopee-stat-icon">
              <i class="el-icon-link"></i>
            </div>
            <h3>Rút Gọn Link Nhanh</h3>
            <p>Tạo link rút gọn dễ nhớ cho sản phẩm Shopee chỉ trong vài giây</p>
          </div>

          <div class="feature-card">
            <div class="shopee-stat-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <h3>Theo Dõi Analytics</h3>
            <p>Xem thống kê clicks, conversions và hiệu suất chi tiết</p>
          </div>

          <div class="feature-card">
            <div class="shopee-stat-icon">
              <i class="el-icon-shopping-cart-2"></i>
            </div>
            <h3>Tích Hợp Shopee</h3>
            <p>Kết nối trực tiếp với Shopee GraphAPI để quản lý affiliate</p>
          </div>

          <div class="feature-card">
            <div class="shopee-stat-icon">
              <i class="el-icon-mobile-phone"></i>
            </div>
            <h3>Responsive Design</h3>
            <p>Hoạt động mượt mà trên mọi thiết bị: Desktop, Tablet, Mobile</p>
          </div>

          <div class="feature-card">
            <div class="shopee-stat-icon">
              <i class="el-icon-pie-chart"></i>
            </div>
            <h3>Dashboard Trực Quan</h3>
            <p>Giao diện thân thiện, dễ sử dụng với biểu đồ và thống kê</p>
          </div>

          <div class="feature-card">
            <div class="shopee-stat-icon">
              <i class="el-icon-lock"></i>
            </div>
            <h3>Bảo Mật Cao</h3>
            <p>JWT Authentication và mã hóa dữ liệu an toàn</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Stats Section -->
    <div class="stats-section">
      <div class="shopee-container">
        <div class="shopee-grid shopee-grid-4">
          <div class="stat-box">
            <div class="stat-number">1000+</div>
            <div class="stat-label">Links Đã Tạo</div>
          </div>
          <div class="stat-box">
            <div class="stat-number">50K+</div>
            <div class="stat-label">Clicks</div>
          </div>
          <div class="stat-box">
            <div class="stat-number">500+</div>
            <div class="stat-label">Users</div>
          </div>
          <div class="stat-box">
            <div class="stat-number">99.9%</div>
            <div class="stat-label">Uptime</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home'
}
</script>

<style scoped>
.home {
  min-height: 100vh;
  background: #f5f5f5;
}

.shopee-hero {
  background: linear-gradient(135deg, #ee4d2d 0%, #ff6b35 100%);
  color: white;
  padding: 80px 20px;
  text-align: center;
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
}

.hero-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 20px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.hero-title i {
  margin-right: 12px;
}

.hero-subtitle {
  font-size: 20px;
  margin-bottom: 40px;
  opacity: 0.95;
}

.hero-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.features-section {
  padding: 60px 20px;
  background: white;
}

.section-title {
  text-align: center;
  font-size: 32px;
  color: #ee4d2d;
  margin-bottom: 40px;
  font-weight: bold;
}

.feature-card {
  background: white;
  padding: 30px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(238, 77, 45, 0.2);
  border-color: #ee4d2d;
}

.feature-card h3 {
  font-size: 20px;
  margin: 16px 0 12px;
  color: #333;
}

.feature-card p {
  color: #757575;
  line-height: 1.6;
}

.stats-section {
  padding: 60px 20px;
  background: linear-gradient(135deg, #ee4d2d 0%, #ff6b35 100%);
  color: white;
}

.stat-box {
  text-align: center;
  padding: 20px;
}

.stat-number {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 8px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.stat-label {
  font-size: 16px;
  opacity: 0.9;
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 32px;
  }

  .hero-subtitle {
    font-size: 16px;
  }

  .stat-number {
    font-size: 36px;
  }
}
</style>
