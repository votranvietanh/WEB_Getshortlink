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

      <!-- Loading Skeletons -->
      <div v-if="loading" class="balance-cards-grid">
        <SkeletonLoader type="balance" />
        <SkeletonLoader type="balance" />
        <SkeletonLoader type="balance" />
      </div>

      <!-- Balance Cards -->
      <div v-else class="animate-fade-in-up">
      <div class="balance-cards-grid">
        <!-- Total Balance -->
        <el-card class="balance-card balance-total">
          <div class="balance-icon">
            <i class="el-icon-wallet"></i>
          </div>
          <div class="balance-info">
            <div class="balance-label">Tổng Số Dư</div>
            <div class="balance-amount">
              <AnimatedNumber :value="totalBalance" :format="formatMoney" />
            </div>
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
            <div class="balance-amount success">
              <AnimatedNumber :value="availableBalance" :format="formatMoney" />
            </div>
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
            <div class="balance-amount pending">
              <AnimatedNumber :value="pendingBalance" :format="formatMoney" />
            </div>
            <div class="balance-note">{{ pendingOrders }} đơn đang chờ</div>
          </div>
        </el-card>
      </div>

      </div>

      <!-- Quick Stats -->
      <div v-if="!loading" class="quick-stats-grid animate-fade-in-up" style="animation-delay: 0.1s;">
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

          <el-table-column label="STT" type="index" width="60" align="center">
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

        <el-empty v-if="filteredOrders.length === 0" description="Chưa có đơn hàng nào">
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
import * as XLSX from 'xlsx'
import { saveAs } from 'file-saver'
import AnimatedNumber from '@/components/AnimatedNumber.vue'
import SkeletonLoader from '@/components/SkeletonLoader.vue'

export default {
  name: 'Dashboard',
  components: {
    AnimatedNumber,
    SkeletonLoader
  },
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
      dateRange: null,
      exporting: false,
      
      // Loading state
      loading: true,

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
      } finally {
        this.loading = false
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
    },

    // Filter methods
    filterByDate() {
      if (this.filteredOrders.length > 0) {
        this.$message.info(`Đã lọc ${this.filteredOrders.length} đơn hàng`)
      } else {
        this.$message.warning('Không có đơn hàng nào trong khoảng thời gian này')
      }
    },

    clearDateFilter() {
      this.dateRange = null
      this.$message.success('Đã xóa bộ lọc')
    },

    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const day = String(d.getDate()).padStart(2, '0')
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const year = d.getFullYear()
      return `${day}/${month}/${year}`
    },

    getExportFileName() {
      if (this.dateRange && this.dateRange.length === 2) {
        return `${this.dateRange[0]}_${this.dateRange[1]}`
      }
      return new Date().toISOString().split('T')[0]
    },

    // Export Excel
    exportToExcel() {
      this.exporting = true

      try {
        if (this.filteredOrders.length === 0) {
          this.$message.warning('Không có dữ liệu để xuất')
          this.exporting = false
          return
        }

        // Prepare data with STT
        const data = this.filteredOrders.map((order, index) => ({
          'STT': index + 1,
          'Mã Đơn': order.orderCode,
          'Sản Phẩm': order.productName,
          'Shop': order.shopName,
          'Giá Trị Đơn (VNĐ)': order.orderValue,
          'Hoàn Tiền (VNĐ)': order.cashback,
          'Trạng Thái': this.getStatusText(order.status),
          'Ngày': order.date,
          'Ngày Tạo': order.createdAt,
          'Ngày Mua': order.purchasedAt || '',
          'Ngày Hoàn': order.completedAt || '',
          'Link Gốc': order.originalUrl,
          'Link Hoàn Tiền': order.shortUrl
        }))

        // Create workbook
        const ws = XLSX.utils.json_to_sheet(data)

        // Set column widths
        ws['!cols'] = [
          { wch: 5 },  // STT
          { wch: 10 }, // Mã Đơn
          { wch: 30 }, // Sản Phẩm
          { wch: 20 }, // Shop
          { wch: 15 }, // Giá Trị
          { wch: 15 }, // Hoàn Tiền
          { wch: 12 }, // Trạng Thái
          { wch: 12 }, // Ngày
          { wch: 18 }, // Ngày Tạo
          { wch: 18 }, // Ngày Mua
          { wch: 18 }, // Ngày Hoàn
          { wch: 40 }, // Link Gốc
          { wch: 40 }  // Link Hoàn Tiền
        ]

        const wb = XLSX.utils.book_new()
        XLSX.utils.book_append_sheet(wb, ws, 'Đơn Hàng')

        // Add summary sheet
        const summary = [
          ['BÁO CÁO ĐƠN HÀNG'],
          [''],
          ['Tổng số đơn:', data.length],
          ['Tổng giá trị:', data.reduce((sum, o) => sum + o['Giá Trị Đơn (VNĐ)'], 0)],
          ['Tổng hoàn tiền:', data.reduce((sum, o) => sum + o['Hoàn Tiền (VNĐ)'], 0)],
          [''],
          ['Ngày xuất:', new Date().toLocaleString('vi-VN')]
        ]

        if (this.dateRange && this.dateRange.length === 2) {
          summary.splice(3, 0, ['Từ ngày:', this.formatDate(this.dateRange[0])])
          summary.splice(4, 0, ['Đến ngày:', this.formatDate(this.dateRange[1])])
        }

        const wsSummary = XLSX.utils.aoa_to_sheet(summary)
        wsSummary['!cols'] = [{ wch: 20 }, { wch: 30 }]
        XLSX.utils.book_append_sheet(wb, wsSummary, 'Tổng Quan')

        // Generate file
        const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'array' })
        const blob = new Blob([wbout], { type: 'application/octet-stream' })

        // Download
        const fileName = `don-hang-${this.getExportFileName()}.xlsx`
        saveAs(blob, fileName)

        this.$message.success(`Đã xuất ${data.length} đơn hàng thành công!`)
      } catch (error) {
        console.error('Export error:', error)
        this.$message.error('Lỗi khi xuất file: ' + error.message)
      } finally {
        this.exporting = false
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

/* Header Actions Group */
.header-actions-group {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

/* Filter Summary */
.filter-summary {
  padding: 12px 0;
  margin-bottom: 12px;
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
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08), 0 1px 3px rgba(0, 0, 0, 0.04);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #ffffff 0%, #fafafa 100%);
}

.balance-card::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(238, 77, 45, 0.03) 0%, transparent 70%);
  animation: rotate 20s linear infinite;
  pointer-events: none;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.balance-card:hover {
  transform: translateY(-6px) scale(1.02);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12), 0 4px 8px rgba(0, 0, 0, 0.06);
}

.balance-card::v-deep .el-card__body {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px;
  position: relative;
  z-index: 1;
}

.balance-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: white;
  position: relative;
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.balance-icon::after {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 16px;
  background: inherit;
  opacity: 0.3;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.15);
    opacity: 0;
  }
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
  min-width: 0;
}

.balance-label {
  font-size: 13px;
  color: #757575;
  margin-bottom: 6px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.balance-amount {
  font-size: 32px;
  font-weight: 800;
  color: #333;
  margin-bottom: 6px;
  letter-spacing: -0.02em;
  line-height: 1;
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
  font-weight: 500;
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
  padding: 24px 20px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06), 0 1px 2px rgba(0, 0, 0, 0.04);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(0, 0, 0, 0.05);
  position: relative;
  overflow: hidden;
}

.stat-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #ee4d2d 0%, #ff6b35 100%);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-4px) scale(1.02);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1), 0 2px 4px rgba(0, 0, 0, 0.06);
  border-color: rgba(238, 77, 45, 0.2);
}

.stat-item:hover::before {
  transform: scaleX(1);
}

.stat-item i {
  font-size: 36px;
  color: #ee4d2d;
  margin-bottom: 12px;
  display: block;
  transition: transform 0.3s ease;
}

.stat-item:hover i {
  transform: scale(1.1);
}

.stat-value {
  font-size: 28px;
  font-weight: 800;
  color: #333;
  margin-bottom: 6px;
  letter-spacing: -0.02em;
}

.stat-label {
  font-size: 13px;
  color: #757575;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.05em;
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
  .header-actions-group {
    flex-direction: column;
    align-items: stretch;
    width: 100%;
  }

  .header-actions-group .el-date-picker,
  .header-actions-group .el-button {
    width: 100%;
  }

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
