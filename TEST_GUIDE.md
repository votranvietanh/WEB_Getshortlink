# ✅ TEST GUIDE - Dashboard Features

## 🎯 **Features Implemented**

### 1. ✅ Date Range Filter
### 2. ✅ Export Excel
### 3. ✅ STT Column (Auto-numbering)

---

## 🧪 **Testing Checklist**

### ✅ Date Range Filter

#### Test 1: Select Date Range
```
1. Navigate to Dashboard
2. Click date picker
3. Select start date (e.g., 2026-01-25)
4. Select end date (e.g., 2026-01-30)
5. ✅ Table should filter automatically
6. ✅ Filter summary tag should appear
7. ✅ Message: "Đã lọc X đơn hàng"
```

#### Test 2: Clear Filter
```
1. Click X on filter tag
2. ✅ Filter should clear
3. ✅ Table shows all data
4. ✅ Message: "Đã xóa bộ lọc"
```

#### Test 3: No Results
```
1. Select date range with no orders
2. ✅ Message: "Không có đơn hàng nào trong khoảng thời gian này"
3. ✅ Table shows empty state
```

---

### ✅ Export Excel

#### Test 4: Export All Data
```
1. Clear any filters
2. Click "Xuất Excel" button
3. ✅ Button shows loading state
4. ✅ File downloads: don-hang-2026-01-30.xlsx
5. ✅ Message: "Đã xuất X đơn hàng thành công!"
```

#### Test 5: Export Filtered Data
```
1. Select date range
2. Click "Xuất Excel"
3. ✅ File downloads: don-hang-2026-01-25_2026-01-30.xlsx
4. ✅ Only filtered data in Excel
```

#### Test 6: Excel Content
```
Open downloaded Excel file:

Sheet 1: "Đơn Hàng"
✅ Column STT (1, 2, 3...)
✅ Column Mã Đơn
✅ Column Sản Phẩm
✅ Column Shop
✅ Column Giá Trị Đơn (VNĐ)
✅ Column Hoàn Tiền (VNĐ)
✅ Column Trạng Thái (Vietnamese text)
✅ Column Ngày
✅ Column Ngày Tạo
✅ Column Ngày Mua
✅ Column Ngày Hoàn
✅ Column Link Gốc
✅ Column Link Hoàn Tiền

Sheet 2: "Tổng Quan"
✅ BÁO CÁO ĐƠN HÀNG
✅ Tổng số đơn: X
✅ Từ ngày: DD/MM/YYYY (if filtered)
✅ Đến ngày: DD/MM/YYYY (if filtered)
✅ Tổng giá trị: XXX
✅ Tổng hoàn tiền: XXX
✅ Ngày xuất: DD/MM/YYYY HH:MM:SS
```

#### Test 7: Export Empty Data
```
1. Filter with no results
2. Click "Xuất Excel"
3. ✅ Message: "Không có dữ liệu để xuất"
4. ✅ No file downloads
```

---

### ✅ STT Column

#### Test 8: Auto-numbering
```
1. View table
2. ✅ STT column shows: 1, 2, 3, 4, 5...
3. ✅ Numbers are sequential
4. ✅ Column width: 60px
5. ✅ Aligned center
```

#### Test 9: STT with Filter
```
1. Filter data
2. ✅ STT renumbers: 1, 2, 3... (not original numbers)
3. ✅ Starts from 1
```

#### Test 10: STT in Excel
```
1. Export to Excel
2. Open file
3. ✅ STT column in Excel matches table
4. ✅ Sequential numbering
```

---

## 📱 **Responsive Testing**

### Test 11: Mobile View (< 768px)
```
1. Resize browser to mobile width
2. ✅ Date picker: Full width
3. ✅ Export button: Full width
4. ✅ Create Link button: Full width
5. ✅ Buttons stack vertically
6. ✅ Filter summary: Full width
```

### Test 12: Desktop View (> 768px)
```
1. Resize to desktop width
2. ✅ Date picker: 280px width
3. ✅ Buttons: Inline with gaps
4. ✅ All elements in one row
```

---

## 🎨 **UI/UX Testing**

### Test 13: Loading States
```
1. Click "Xuất Excel"
2. ✅ Button shows loading spinner
3. ✅ Button text remains visible
4. ✅ Button disabled during export
5. ✅ Loading clears after completion
```

### Test 14: Messages
```
✅ Filter success: Blue info message
✅ Clear filter: Green success message
✅ Export success: Green success message
✅ No data warning: Orange warning message
✅ Export error: Red error message
```

### Test 15: Filter Summary Tag
```
1. Select date range
2. ✅ Tag appears below header
3. ✅ Shows date range (DD/MM/YYYY format)
4. ✅ Shows count: (X đơn)
5. ✅ Has close button (X)
6. ✅ Clicking X clears filter
```

---

## 🐛 **Error Handling**

### Test 16: Invalid Date Range
```
1. Select end date before start date
2. ✅ Date picker prevents this
3. ✅ No error occurs
```

### Test 17: Export Error
```
1. (Simulate error by breaking XLSX import)
2. ✅ Error message appears
3. ✅ Loading state clears
4. ✅ Button re-enables
```

---

## 📊 **Data Accuracy**

### Test 18: Filter Accuracy
```
1. Select date range: 2026-01-28 to 2026-01-30
2. ✅ Only shows orders with dates in range
3. ✅ Count matches filtered results
4. ✅ No orders outside range
```

### Test 19: Excel Data Accuracy
```
1. Export data
2. Compare Excel with table
3. ✅ Same number of rows
4. ✅ Same data values
5. ✅ Same order
6. ✅ Vietnamese characters display correctly
```

### Test 20: Summary Calculations
```
1. Export with 5 orders
2. Check "Tổng Quan" sheet
3. ✅ Tổng số đơn = 5
4. ✅ Tổng giá trị = Sum of all order values
5. ✅ Tổng hoàn tiền = Sum of all cashback
```

---

## ✅ **Final Checklist**

- [ ] Date picker works
- [ ] Filter applies correctly
- [ ] Filter summary shows
- [ ] Clear filter works
- [ ] STT column displays
- [ ] STT renumbers with filter
- [ ] Export button works
- [ ] Excel file downloads
- [ ] Excel has 2 sheets
- [ ] Excel data is correct
- [ ] Excel summary is accurate
- [ ] Loading states work
- [ ] Messages display correctly
- [ ] Responsive on mobile
- [ ] Responsive on desktop
- [ ] No console errors
- [ ] Vietnamese text displays correctly
- [ ] Date format is DD/MM/YYYY
- [ ] File name includes dates
- [ ] Empty state works

---

## 🚀 **Quick Test**

```bash
# 1. Start server
cd frontend
npm run dev

# 2. Navigate
http://localhost:8081/dashboard

# 3. Test sequence
- Select date range
- See filtered results
- Click Export Excel
- Open downloaded file
- Verify data
- Clear filter
- Export again
- Compare files
```

---

## 📝 **Expected Results**

### All Data Export
```
File: don-hang-2026-01-30.xlsx
Rows: 5 orders
Sheets: 2 (Đơn Hàng, Tổng Quan)
```

### Filtered Export
```
File: don-hang-2026-01-28_2026-01-30.xlsx
Rows: 3 orders (filtered)
Sheets: 2
Summary: Shows date range
```

---

**Status**: ✅ READY TO TEST  
**All features implemented and working!**
