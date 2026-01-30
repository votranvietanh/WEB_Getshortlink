# 📅 Quick Implementation Guide - Date Filter & Export Excel

## ✅ **Đã Thêm Vào Dashboard**

### 1. Date Range Filter
### 2. Export Excel Button
### 3. Filter Summary Tag

---

## 🔧 **Implementation Steps**

### Step 1: Install Dependencies
```bash
cd frontend
npm install xlsx file-saver --save
```

### Step 2: Import Libraries (Dashboard.vue)
```javascript
import * as XLSX from 'xlsx'
import { saveAs } from 'file-saver'
```

### Step 3: Add Data Properties
```javascript
data() {
  return {
    // ... existing data
    dateRange: null,
    exporting: false
  }
}
```

### Step 4: Add Computed Property
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

### Step 5: Add Methods
```javascript
methods: {
  filterByDate() {
    this.$message.info(`Lọc ${this.filteredOrders.length} đơn hàng`)
  },
  
  clearDateFilter() {
    this.dateRange = null
  },
  
  formatDate(date) {
    if (!date) return ''
    const d = new Date(date)
    return `${d.getDate()}/${d.getMonth() + 1}/${d.getFullYear()}`
  },
  
  getExportFileName() {
    if (this.dateRange && this.dateRange.length === 2) {
      return `${this.dateRange[0]}_${this.dateRange[1]}`
    }
    return new Date().toISOString().split('T')[0]
  },
  
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
}
```

---

## 🎨 **UI Components**

### Date Range Picker
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

### Export Button
```vue
<el-button 
  size="small" 
  type="success" 
  icon="el-icon-download"
  @click="exportToExcel"
  :loading="exporting"
>
  Xuất Excel
</el-button>
```

### Filter Summary
```vue
<div v-if="dateRange && dateRange.length === 2" class="filter-summary">
  <el-tag closable @close="clearDateFilter">
    <i class="el-icon-date"></i> 
    {{ formatDate(dateRange[0]) }} - {{ formatDate(dateRange[1]) }}
    ({{ filteredOrders.length }} đơn)
  </el-tag>
</div>
```

### Update Table
```vue
<!-- Change from :data="orders" to :data="filteredOrders" -->
<el-table :data="filteredOrders" ...>
```

---

## 📊 **Excel Output Format**

### Columns
```
| Mã Đơn | Sản Phẩm | Shop | Giá Trị Đơn | Hoàn Tiền | Trạng Thái | Ngày | Ngày Tạo | Ngày Mua | Ngày Hoàn |
|--------|----------|------|-------------|-----------|------------|------|----------|----------|-----------|
| SP001  | Áo thun  | ABC  | 250000      | 25000     | Đã hoàn    | ...  | ...      | ...      | ...       |
```

### File Name Format
```
With date filter:
don-hang-2026-01-01_2026-01-31.xlsx

Without filter:
don-hang-2026-01-30.xlsx
```

---

## 🎯 **Features**

### ✅ Date Filter
- Select date range
- Auto filter table
- Show filter summary
- Clear filter easily

### ✅ Export Excel
- Export filtered data
- Professional format
- Vietnamese headers
- Auto file naming
- Loading state

### ✅ Responsive
- Mobile friendly
- Stack on small screens
- Full width buttons

---

## 🚀 **Usage**

### Filter by Date
```
1. Click date picker
2. Select start date
3. Select end date
4. Table auto-filters
5. See summary tag
```

### Export to Excel
```
1. (Optional) Filter by date
2. Click "Xuất Excel"
3. Wait for loading
4. File downloads automatically
5. Success message appears
```

### Clear Filter
```
1. Click X on filter tag
2. Table shows all data
```

---

## 📱 **Mobile Responsive CSS**

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
    margin-right: 0 !important;
    margin-bottom: 8px;
  }
}
```

---

## ✅ **Testing Checklist**

- [ ] Date picker opens correctly
- [ ] Can select date range
- [ ] Table filters on date change
- [ ] Filter summary shows correct count
- [ ] Clear filter works
- [ ] Export button shows loading
- [ ] Excel file downloads
- [ ] File name is correct
- [ ] Excel data is correct
- [ ] Mobile responsive works

---

**Status**: ✅ READY TO USE  
**Next**: Test on localhost
