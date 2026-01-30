<template>
  <div class="dashboard">
    <!-- Shopee Header -->
    <div class="shopee-header">
      <div class="shopee-container">
        <div class="header-content">
          <h2><i class="el-icon-wallet"></i> Dashboard Hoàn Tiền</h2>
          <div class="header-actions">
            <el-button type="success" size="small" @click="showWithdrawDialog = true" :disabled="!canWithdraw">
              <i class="el-icon-money"></i> Rút Tiền
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="shopee-container" style="padding-top: 24px;">

      <!-- Balance Cards -->
      <div class="balance-cards-grid">
        <!-- Total Balance -->
        <el-card class="balance-card balance-total">
          <div class="balance-icon">
            <i class="el-icon-wallet"></i>
          </div>
          <div class="balance-info">
            <div class="balance-label">Tổng Số Dư</div>
            <div class="balance-amount">{{ formatMoney(totalBalance) }}</div>
            <div class="balance-note">Đã hoàn + Chờ hoàn</div>
          </div>
        </el-card>

        <!-- Available Balance -->
        <el-card class="balance-card balance-available">
          <div class="balance-icon">
            <i class="el-icon-success"></i>
          </div>
          <div class="balance-info">
            <div class="balance-label">Đã Hoàn</div>
            <div class="balance-amount success">{{ formatMoney(availableBalance) }}</div>
            <div class="balance-note">Có thể rút từ {{ formatMoney(minWithdraw) }}</div>
          </div>
        </el-card>

        <!-- Pending Balance -->
        <el-card class="balance-card balance-pending">
          <div class="balance-icon">
            <i class="el-icon-time"></i>
          </div>
          <div class="balance-info">
            <div class="balance-label">Chờ Hoàn</div>
            <div class="balance-amount pending">{{ formatMoney(pendingBalance) }}</div>
            <div class="balance-note">{{ pendingOrders }} đơn đang chờ</div>
          </div>
        </el-card>
      </div>

      <!-- Quick Stats -->
      <div class="quick-stats-grid">
        <div class="stat-item">
          <i class="el-icon-shopping-cart-2"></i>
          <div class="stat-value">{{ totalOrders }}</div>
          <div class="stat-label">Tổng Đơn Hàng</div>
        </div>
        <div class="stat-item">
          <i class="el-icon-check"></i>
          <div class="stat-value">{{ completedOrders }}</div>
          <div class="stat-label">Đã Hoàn Thành</div>
        </div>
        <div class="stat-item">
          <i class="el-icon-link"></i>
          <div class="stat-value">{{ totalLinks }}</div>
          <div class="stat-label">Links Đã Tạo</div>
        </div>
        <div class="stat-item">
          <i class="el-icon-trophy"></i>
          <div class="stat-value">{{ formatMoney(totalEarned) }}</div>
          <div class="stat-label">Tổng Đã Nhận</div>
        </div>
      </div>

      <!-- Orders Table -->
      <el-card style="margin-top: 24px;">
        <div slot="header" class="card-header">
          <span style="font-weight: bold; color: #ee4d2d;">
            <i class="el-icon-document"></i> Lịch Sử Đơn Hàng
          </span>
          <div class="header-actions-group">
            <!-- Date Range Picker -->
            <el-date-picker v-model="dateRange" type="daterange" range-separator="-" start-placeholder="Từ ngày"
              end-placeholder="Đến ngày" format="dd/MM/yyyy" value-format="yyyy-MM-dd" size="small"
              @change="filterByDate" style="width: 280px; margin-right: 12px;">
            </el-date-picker>

            <!-- Export Excel Button -->
            <el-button size="small" type="success" icon="el-icon-download" @click="exportToExcel" :loading="exporting">
              Xuất Excel
            </el-button>

            <!-- Create Link Button -->
            <el-button size="small" @click="$router.push('/links/create')">
              <i class="el-icon-plus"></i> Tạo Link Mới
            </el-button>
          </div>
        </div>

        <!-- Filter Summary -->
        <div v-if="dateRange && dateRange.length === 2" class="filter-summary">
          <el-tag closable @close="clearDateFilter">
            <i class="el-icon-date"></i>
            {{ formatDate(dateRange[0]) }} - {{ formatDate(dateRange[1]) }}
            ({{ filteredOrders.length }} đơn)
          </el-tag>
        </div>

        <el-table :data="filteredOrders" style="width: 100%" :default-sort="{ prop: 'date', order: 'descending' }">
          <el-table-column type="expand">
            <template slot-scope="props">
              <div class="order-detail">
                <p><strong>Link gốc:</strong> {{ props.row.originalUrl }}</p>
                <p><strong>Link hoàn tiền:</strong> {{ props.row.shortUrl }}</p>
                <p><strong>Ngày tạo:</strong> {{ props.row.createdAt }}</p>
                <p v-if="props.row.purchasedAt"><strong>Ngày mua:</strong> {{ props.row.purchasedAt }}</p>
                <p v-if="props.row.completedAt"><strong>Ngày hoàn:</strong> {{ props.row.completedAt }}</p>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="Mã Đơn" prop="orderCode" width="120">
            <template slot-scope="scope">
              <el-tag size="small">{{ scope.row.orderCode }}</el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Sản Phẩm" prop="productName" min-width="200">
            <template slot-scope="scope">
              <div class="product-info">
                <div class="product-name">{{ scope.row.productName }}</div>
                <div class="product-shop">{{ scope.row.shopName }}</div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="Giá Trị Đơn" prop="orderValue" width="120" align="right">
            <template slot-scope="scope">
              <span class="order-value">{{ formatMoney(scope.row.orderValue) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Hoàn Tiền" prop="cashback" width="120" align="right">
            <template slot-scope="scope">
              <span class="cashback-amount" :class="scope.row.status">
                {{ formatMoney(scope.row.cashback) }}
              </span>
            </template>
          </el-table-column>

          <el-table-column label="Trạng Thái" prop="status" width="140">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)" size="small">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Ngày" prop="date" width="110" sortable>
            <template slot-scope="scope">
              <span class="date-text">{{ scope.row.date }}</span>
            </template>
          </el-table-column>
        </el-table>

        <el-empty v-if="orders.length === 0" description="Chưa có đơn hàng nào">
          <el-button type="primary" @click="$router.push('/links/create')">
            Tạo Link Đầu Tiên
          </el-button>
        </el-empty>
      </el-card>

      <!-- Withdraw Info Card -->
      <el-card class="withdraw-info-card" style="margin-top: 24px;">
        <div slot="header">
          <span style="font-weight: bold; color: #26aa99;">
            <i class="el-icon-info"></i> Thông Tin Rút Tiền
          </span>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <i class="el-icon-wallet"></i>
              <div>
                <div class="info-label">Số dư khả dụng</div>
                <div class="info-value">{{ formatMoney(availableBalance) }}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <i class="el-icon-money"></i>
              <div>
                <div class="info-label">Tối thiểu rút</div>
                <div class="info-value">{{ formatMoney(minWithdraw) }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-divider></el-divider>
        <el-alert type="info" :closable="false">
          <strong>📅 Lịch rút tiền:</strong> Thứ 7, Chủ Nhật hàng tuần<br>
          <strong>⏰ Thời gian xử lý:</strong> 1-3 ngày làm việc<br>
          <strong>🏦 Phương thức:</strong> Chuyển khoản ngân hàng
        </el-alert>
      </el-card>
    </div>

    <!-- Withdraw Dialog -->
    <el-dialog title="Rút Tiền" :visible.sync="showWithdrawDialog" width="500px">
      <el-form :model="withdrawForm" label-position="top">
        <el-form-item label="Số tiền rút">
          <el-input-number v-model="withdrawForm.amount" :min="minWithdraw" :max="availableBalance" :step="10000"
            style="width: 100%;"></el-input-number>
          <div style="margin-top: 8px; font-size: 12px; color: #757575;">
            Khả dụng: {{ formatMoney(availableBalance) }}
          </div>
        </el-form-item>

        <el-form-item label="Ngân hàng">
          <el-select v-model="withdrawForm.bank" placeholder="Chọn ngân hàng" style="width: 100%;">
            <el-option label="Vietcombank" value="VCB"></el-option>
            <el-option label="Techcombank" value="TCB"></el-option>
            <el-option label="BIDV" value="BIDV"></el-option>
            <el-option label="VietinBank" value="CTG"></el-option>
            <el-option label="ACB" value="ACB"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="Số tài khoản">
          <el-input v-model="withdrawForm.accountNumber" placeholder="Nhập số tài khoản"></el-input>
        </el-form-item>

        <el-form-item label="Tên chủ tài khoản">
          <el-input v-model="withdrawForm.accountName" placeholder="NGUYEN VAN A"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="showWithdrawDialog = false">Hủy</el-button>
        <el-button type="primary" @click="handleWithdraw">Xác Nhận Rút</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      // Balance
      totalBalance: 0,
      availableBalance: 0,
      pendingBalance: 0,
      minWithdraw: 10000,

      // Stats
      totalOrders: 0,
      completedOrders: 0,
      pendingOrders: 0,
      totalLinks: 0,
      totalEarned: 0,

      // Orders
      orders: [],

      // Withdraw
      showWithdrawDialog: false,
      withdrawForm: {
        amount: 10000,
        bank: '',
        accountNumber: '',
        accountName: ''
      }
    }
  },
  computed: {
    canWithdraw() {
      return this.availableBalance >= this.minWithdraw
    },
    filteredOrders() {
      if (!this.dateRange || this.dateRange.length !== 2) {
        return this.orders
      }

      const [startDate, endDate] = this.dateRange
      return this.orders.filter(order => {
        return order.date >= startDate && order.date <= endDate
      })
    }
  },
  mounted() {
    this.loadDashboardData()
  },
  methods: {
    async loadDashboardData() {
      try {
        // Mock data - Replace with actual API calls
        this.availableBalance = 125000
        this.pendingBalance = 85000
        this.totalBalance = this.availableBalance + this.pendingBalance

        this.totalOrders = 15
        this.completedOrders = 8
        this.pendingOrders = 5
        this.totalLinks = 12
        this.totalEarned = 450000

        this.orders = [
          {
            orderCode: 'SP001',
            productName: 'Áo thun nam basic cotton',
            shopName: 'Shop Thời Trang ABC',
            orderValue: 250000,
            cashback: 25000,
            status: 'completed',
            date: '2026-01-28',
            originalUrl: 'https://shopee.vn/product/123',
            shortUrl: 'https://domain.com/r/abc123',
            createdAt: '2026-01-25 10:30',
            purchasedAt: '2026-01-25 14:20',
            completedAt: '2026-01-28 09:15'
          },
          {
            orderCode: 'SP002',
            productName: 'Quần jean nam slim fit',
            shopName: 'Jean Store VN',
            orderValue: 450000,
            cashback: 35000,
            status: 'pending',
            date: '2026-01-29',
            originalUrl: 'https://shopee.vn/product/456',
            shortUrl: 'https://domain.com/r/def456',
            createdAt: '2026-01-28 15:45',
            purchasedAt: '2026-01-29 11:30'
          },
          {
            orderCode: 'SP003',
            productName: 'Giày sneaker trắng',
            shopName: 'Sneaker World',
            orderValue: 380000,
            cashback: 30000,
            status: 'pending',
            date: '2026-01-30',
            originalUrl: 'https://shopee.vn/product/789',
            shortUrl: 'https://domain.com/r/ghi789',
            createdAt: '2026-01-29 09:20',
            purchasedAt: '2026-01-30 10:15'
          },
          {
            orderCode: 'SP004',
            productName: 'Balo laptop cao cấp',
            shopName: 'Balo Shop 24h',
            orderValue: 550000,
            cashback: 0,
            status: 'rejected',
            date: '2026-01-27',
            originalUrl: 'https://shopee.vn/product/101',
            shortUrl: 'https://domain.com/r/jkl101',
            createdAt: '2026-01-26 16:00',
            purchasedAt: '2026-01-27 08:45'
          },
          {
            orderCode: 'SP005',
            productName: 'Tai nghe bluetooth',
            shopName: 'Tech Store Pro',
            orderValue: 320000,
            cashback: 35000,
            status: 'completed',
            date: '2026-01-26',
            originalUrl: 'https://shopee.vn/product/202',
            shortUrl: 'https://domain.com/r/mno202',
            createdAt: '2026-01-24 13:30',
            purchasedAt: '2026-01-25 09:20',
            completedAt: '2026-01-26 14:00'
          }
        ]
      } catch (error) {
        console.error('Failed to load dashboard data:', error)
        this.$message.error('Không thể tải dữ liệu')
      }
    },
    formatMoney(amount) {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount)
    },
    getStatusType(status) {
      const types = {
        'completed': 'success',
        'pending': 'warning',
        'processing': 'info',
        'rejected': 'danger'
      }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = {
        'completed': 'Đã hoàn',
        'pending': 'Chờ hoàn',
        'processing': 'Đang xử lý',
        'rejected': 'Bị từ chối'
      }
      return texts[status] || status
    },
    async handleWithdraw() {
      if (!this.withdrawForm.bank || !this.withdrawForm.accountNumber || !this.withdrawForm.accountName) {
        this.$message.warning('Vui lòng điền đầy đủ thông tin')
        return
      }

      try {
        // Mock API call
        await new Promise(resolve => setTimeout(resolve, 1000))

        this.$message.success('Yêu cầu rút tiền đã được gửi! Chúng tôi sẽ xử lý trong 1-3 ngày làm việc.')
        this.showWithdrawDialog = false

        // Reset form
        this.withdrawForm = {
          amount: 10000,
          bank: '',
          accountNumber: '',
          accountName: ''
        }
      } catch (error) {
        this.$message.error('Có lỗi xảy ra, vui lòng thử lại')
      }
    }
  }
}
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  background: #f5f5f5;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h2 {
  margin: 0;
  color: white;
}

.header-content h2 i {
  margin-right: 8px;
}

/* Balance Cards */
.balance-cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.balance-card {
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.balance-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.balance-card::v-deep .el-card__body {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px;
}

.balance-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
}

.balance-total .balance-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.balance-available .balance-icon {
  background: linear-gradient(135deg, #26aa99 0%, #34d399 100%);
}

.balance-pending .balance-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.balance-info {
  flex: 1;
}

.balance-label {
  font-size: 13px;
  color: #757575;
  margin-bottom: 4px;
}

.balance-amount {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.balance-amount.success {
  color: #26aa99;
}

.balance-amount.pending {
  color: #f5576c;
}

.balance-note {
  font-size: 12px;
  color: #999;
}

/* Quick Stats */
.quick-stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.stat-item {
  background: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.stat-item i {
  font-size: 32px;
  color: #ee4d2d;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 13px;
  color: #757575;
}

/* Card Header */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* Table Styles */
.order-detail {
  padding: 16px;
  background: #f9f9f9;
  border-radius: 4px;
  font-size: 13px;
  line-height: 1.8;
}

.order-detail p {
  margin: 8px 0;
}

.product-info {
  line-height: 1.6;
}

.product-name {
  font-weight: 500;
  color: #333;
}

.product-shop {
  font-size: 12px;
  color: #757575;
  margin-top: 2px;
}

.order-value {
  font-weight: 500;
  color: #333;
}

.cashback-amount {
  font-weight: bold;
  font-size: 15px;
}

.cashback-amount.completed {
  color: #26aa99;
}

.cashback-amount.pending {
  color: #f5576c;
}

.cashback-amount.rejected {
  color: #999;
  text-decoration: line-through;
}

.date-text {
  font-size: 13px;
  color: #666;
}

/* Withdraw Info Card */
.withdraw-info-card {
  border: 2px solid #e8f5e9;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 8px;
}

.info-item i {
  font-size: 24px;
  color: #26aa99;
}

.info-label {
  font-size: 13px;
  color: #757575;
  margin-bottom: 4px;
}

.info-value {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

/* Responsive */
@media (max-width: 768px) {
  .balance-cards-grid {
    grid-template-columns: 1fr;
  }

  .quick-stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .balance-amount {
    font-size: 24px;
  }

  .stat-value {
    font-size: 20px;
  }
}
</style>
