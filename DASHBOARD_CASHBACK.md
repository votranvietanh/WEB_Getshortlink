# 🎯 Dashboard Hoàn Tiền Affiliate

## ✨ **Tổng Quan**

Dashboard được thiết kế để hiển thị đầy đủ thông tin về:
- **Số dư** - Đã hoàn, Chờ hoàn, Tổng số dư
- **Đơn hàng** - Lịch sử chi tiết từng đơn
- **Rút tiền** - Chức năng rút tiền về ngân hàng

---

## 💰 **Balance Cards (Thẻ Số Dư)**

### 1. **Tổng Số Dư** (Tím)
```
┌─────────────────────────────┐
│ 💼 Tổng Số Dư              │
│ 210.000đ                    │
│ Đã hoàn + Chờ hoàn          │
└─────────────────────────────┘
```
- Icon: Wallet (tím gradient)
- Hiển thị: Tổng tiền (đã hoàn + chờ hoàn)

### 2. **Đã Hoàn** (Xanh)
```
┌─────────────────────────────┐
│ ✅ Đã Hoàn                  │
│ 125.000đ                    │
│ Có thể rút từ 10.000đ       │
└─────────────────────────────┘
```
- Icon: Success (xanh gradient)
- Hiển thị: Số tiền có thể rút
- Note: Điều kiện rút tối thiểu

### 3. **Chờ Hoàn** (Hồng)
```
┌─────────────────────────────┐
│ ⏰ Chờ Hoàn                 │
│ 85.000đ                     │
│ 5 đơn đang chờ              │
└─────────────────────────────┘
```
- Icon: Time (hồng gradient)
- Hiển thị: Số tiền đang chờ
- Note: Số đơn đang pending

---

## 📊 **Quick Stats (Thống Kê Nhanh)**

```
┌──────────┬──────────┬──────────┬──────────┐
│ 🛒 15    │ ✅ 8     │ 🔗 12    │ 🏆 450K  │
│ Tổng Đơn │ Hoàn TC  │ Links    │ Tổng Nhận│
└──────────┴──────────┴──────────┴──────────┘
```

- **Tổng Đơn Hàng**: Tất cả đơn đã tạo
- **Đã Hoàn Thành**: Đơn đã nhận tiền
- **Links Đã Tạo**: Số link affiliate
- **Tổng Đã Nhận**: Tổng tiền đã rút

---

## 📋 **Bảng Lịch Sử Đơn Hàng**

### Columns

| Cột | Mô Tả | Width |
|-----|-------|-------|
| **Expand** | Xem chi tiết | Auto |
| **Mã Đơn** | SP001, SP002... | 120px |
| **Sản Phẩm** | Tên + Shop | 200px+ |
| **Giá Trị Đơn** | Tổng tiền đơn | 120px |
| **Hoàn Tiền** | Số tiền hoàn | 120px |
| **Trạng Thái** | Tag màu | 140px |
| **Ngày** | Ngày tạo | 110px |

### Expand Detail
```
Link gốc: https://shopee.vn/product/123
Link hoàn tiền: https://domain.com/r/abc123
Ngày tạo: 2026-01-25 10:30
Ngày mua: 2026-01-25 14:20
Ngày hoàn: 2026-01-28 09:15
```

### Status Types

| Status | Tag Color | Text | Meaning |
|--------|-----------|------|---------|
| `completed` | Green | Đã hoàn | Đã nhận tiền |
| `pending` | Orange | Chờ hoàn | Đang chờ duyệt |
| `processing` | Blue | Đang xử lý | Đang kiểm tra |
| `rejected` | Red | Bị từ chối | Không đủ điều kiện |

### Example Data
```javascript
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
}
```

---

## 💸 **Rút Tiền**

### Button "Rút Tiền"
- **Vị trí**: Header (góc phải)
- **Màu**: Xanh lá (success)
- **Disabled**: Khi số dư < 10.000đ
- **Icon**: Money

### Withdraw Dialog

```
┌─────────────────────────────────┐
│ Rút Tiền                    [X] │
├─────────────────────────────────┤
│ Số tiền rút:                    │
│ [125,000đ] ▲▼                   │
│ Khả dụng: 125.000đ              │
│                                 │
│ Ngân hàng:                      │
│ [Chọn ngân hàng ▼]              │
│                                 │
│ Số tài khoản:                   │
│ [________________]              │
│                                 │
│ Tên chủ tài khoản:              │
│ [NGUYEN VAN A___]               │
│                                 │
│         [Hủy] [Xác Nhận Rút]    │
└─────────────────────────────────┘
```

### Form Fields

1. **Số tiền rút**
   - Type: Number input
   - Min: 10.000đ
   - Max: Available balance
   - Step: 10.000đ

2. **Ngân hàng**
   - Type: Select
   - Options: VCB, TCB, BIDV, CTG, ACB

3. **Số tài khoản**
   - Type: Text input
   - Validation: Required

4. **Tên chủ tài khoản**
   - Type: Text input
   - Validation: Required
   - Format: UPPERCASE

### Validation
```javascript
if (!bank || !accountNumber || !accountName) {
  $message.warning('Vui lòng điền đầy đủ thông tin')
  return
}
```

### Success Message
```
✅ Yêu cầu rút tiền đã được gửi! 
   Chúng tôi sẽ xử lý trong 1-3 ngày làm việc.
```

---

## ℹ️ **Thông Tin Rút Tiền Card**

```
┌─────────────────────────────────────┐
│ ℹ️ Thông Tin Rút Tiền              │
├─────────────────────────────────────┤
│ 💼 Số dư khả dụng    💰 Tối thiểu  │
│    125.000đ             10.000đ     │
├─────────────────────────────────────┤
│ 📅 Lịch rút tiền: Thứ 7, CN        │
│ ⏰ Thời gian xử lý: 1-3 ngày       │
│ 🏦 Phương thức: Chuyển khoản       │
└─────────────────────────────────────┘
```

---

## 🎨 **Color Scheme**

### Balance Cards
- **Tổng Số Dư**: Purple gradient (#667eea → #764ba2)
- **Đã Hoàn**: Green gradient (#26aa99 → #34d399)
- **Chờ Hoàn**: Pink gradient (#f093fb → #f5576c)

### Status Tags
- **Completed**: Green (#26aa99)
- **Pending**: Orange (#f5576c)
- **Processing**: Blue (#409EFF)
- **Rejected**: Red (#F56C6C)

### Hover Effects
- **Cards**: translateY(-4px) + shadow
- **Stats**: translateY(-2px) + shadow

---

## 📱 **Responsive Design**

### Desktop (>768px)
```
Balance Cards: 3 columns
Quick Stats: 4 columns
Table: Full width with all columns
```

### Mobile (<768px)
```
Balance Cards: 1 column (stacked)
Quick Stats: 2 columns (2x2 grid)
Table: Scrollable horizontal
```

---

## 🔄 **Data Flow**

### Load Dashboard
```
mounted() → loadDashboardData()
  ↓
Fetch from API:
  - Balance (available, pending, total)
  - Stats (orders, links, earned)
  - Orders list
  ↓
Update UI
```

### Withdraw Flow
```
Click "Rút Tiền" button
  ↓
Check: availableBalance >= minWithdraw
  ↓
Show dialog
  ↓
Fill form (amount, bank, account)
  ↓
Validate fields
  ↓
Submit API request
  ↓
Success message
  ↓
Close dialog & reset form
```

---

## 🎯 **Key Features**

### 1. **Real-time Balance**
- Tự động cập nhật số dư
- Phân biệt rõ: Đã hoàn vs Chờ hoàn

### 2. **Order Tracking**
- Xem chi tiết từng đơn
- Filter theo status
- Sort theo ngày

### 3. **Easy Withdraw**
- Button nổi bật
- Form đơn giản
- Validation rõ ràng

### 4. **Visual Feedback**
- Color-coded status
- Hover effects
- Loading states

---

## 📊 **Mock Data**

### Balance
```javascript
availableBalance: 125000,  // Đã hoàn
pendingBalance: 85000,     // Chờ hoàn
totalBalance: 210000,      // Tổng
minWithdraw: 10000         // Tối thiểu rút
```

### Stats
```javascript
totalOrders: 15,       // Tổng đơn
completedOrders: 8,    // Đã hoàn thành
pendingOrders: 5,      // Đang chờ
totalLinks: 12,        // Links đã tạo
totalEarned: 450000    // Tổng đã nhận
```

### Orders
```javascript
[
  {
    orderCode: 'SP001',
    productName: 'Áo thun nam',
    shopName: 'Shop ABC',
    orderValue: 250000,
    cashback: 25000,
    status: 'completed',
    date: '2026-01-28'
  },
  // ... more orders
]
```

---

## 🚀 **Usage**

### Navigate to Dashboard
```javascript
this.$router.push('/dashboard')
```

### Check Withdraw Eligibility
```javascript
computed: {
  canWithdraw() {
    return this.availableBalance >= this.minWithdraw
  }
}
```

### Format Money
```javascript
formatMoney(amount) {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(amount)
}
```

---

## ✅ **Summary**

**Dashboard Features:**
- ✅ 3 Balance cards (Tổng, Đã hoàn, Chờ hoàn)
- ✅ 4 Quick stats
- ✅ Order history table with expand
- ✅ Withdraw dialog with form
- ✅ Withdraw info card
- ✅ Responsive design
- ✅ Beautiful UI with gradients
- ✅ Hover effects & animations
- ✅ Status color-coding
- ✅ Mock data ready

**User Can:**
- 👀 Xem số dư chi tiết
- 📊 Theo dõi đơn hàng
- 💸 Rút tiền dễ dàng
- 📱 Sử dụng trên mobile

---

**Created**: 2026-01-30  
**Version**: 1.0  
**Status**: ✅ COMPLETE
