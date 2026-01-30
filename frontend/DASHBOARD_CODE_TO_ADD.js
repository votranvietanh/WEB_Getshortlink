// ==========================================
// ADD THESE TO Dashboard.vue
// ==========================================

// 1. ADD IMPORTS (after <script>)
import * as XLSX from 'xlsx'
import { saveAs } from 'file-saver'

// 2. ADD DATA PROPERTIES (in data() return)
dateRange: null,
    exporting: false,

        // 3. ADD COMPUTED PROPERTY (in computed section)
        filteredOrders() {
    if (!this.dateRange || this.dateRange.length !== 2) {
        return this.orders
    }

    const [startDate, endDate] = this.dateRange
    return this.orders.filter(order => {
        return order.date >= startDate && order.date <= endDate
    })
},

// 4. ADD METHODS (in methods section)
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
},

// 5. UPDATE TABLE (change :data="orders" to :data="filteredOrders")
// <el-table :data="filteredOrders" ...>

// 6. ADD STT COLUMN (after expand column)
/*
<el-table-column label="STT" type="index" width="60" align="center">
</el-table-column>
*/

// 7. UPDATE EMPTY CHECK (change orders.length to filteredOrders.length)
// <el-empty v-if="filteredOrders.length === 0" ...>
