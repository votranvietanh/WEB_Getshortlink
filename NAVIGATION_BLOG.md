# 🧭 Navigation Menu & Blog/SEO System

## ✅ **Đã Hoàn Thành**

### 1. **Navigation Menu** - Sidebar + Mobile Drawer
### 2. **Blog/SEO Page** - Quản lý content & tối ưu SEO

---

## 🧭 **Navigation Menu**

### Desktop Sidebar

```
┌─────────────────────┐
│ 🛒 Shopee Cashback │ ← Logo/Header
├─────────────────────┤
│ 🏠 Dashboard        │
│ ➕ Tạo Link         │
│ 🔗 Quản Lý Links    │
│ 📊 Thống Kê         │
│ 📝 Blog/SEO         │
├─────────────────────┤
│ 👤 Tài Khoản        │
│ ⚙️ Cài Đặt          │
│ 🏠 Trang Chủ        │
└─────────────────────┘
```

### Features

#### 1. **Collapsible Sidebar**
- **Default**: 240px width
- **Collapsed**: 64px width (chỉ icon)
- **Button**: Nút collapse ở góc phải
- **Animation**: Smooth transition 0.3s

#### 2. **Active State**
- Border right màu đỏ (#ee4d2d)
- Background màu hồng nhạt (#fff6f4)
- Auto highlight theo route

#### 3. **Hover Effect**
- Background: #fff6f4
- Smooth transition

#### 4. **Mobile Drawer**
- **Trigger**: Hamburger button (fixed top-left)
- **Direction**: Slide from left
- **Width**: 280px
- **Auto close**: Khi chọn menu item

---

## 📱 **Responsive Behavior**

### Desktop (>768px)
```
┌─────────┬──────────────────┐
│ Sidebar │  Main Content    │
│ 240px   │  Remaining width │
└─────────┴──────────────────┘
```

### Mobile (<768px)
```
┌──────────────────────────┐
│ [☰]  Main Content        │
│      (Full width)        │
└──────────────────────────┘

Click [☰] → Drawer slides in
```

---

## 📝 **Blog/SEO Page**

### Overview
```
┌─────────────────────────────────┐
│ 📝 Quản Lý Blog & SEO          │
├─────────────────────────────────┤
│ [+ Tạo Bài Viết] [🔄 Làm Mới] │
├─────────────────────────────────┤
│ Danh Sách Bài Viết (3 bài)     │
│                                 │
│ ┌─────────────────────────────┐ │
│ │ Tiêu đề | Danh mục | SEO    │ │
│ │ Status  | Views    | Actions│ │
│ └─────────────────────────────┘ │
├─────────────────────────────────┤
│ 💡 Tips SEO                    │
└─────────────────────────────────┘
```

---

## 📊 **Posts Table**

### Columns

| Column | Description | Width |
|--------|-------------|-------|
| **Tiêu Đề** | Title + Status + Views | 300px+ |
| **Danh Mục** | Category tag | 150px |
| **SEO Score** | Progress bar | 120px |
| **Ngày Tạo** | Created date | 110px |
| **Thao Tác** | View/Edit/Delete | 180px |

### Example Row
```
┌──────────────────────────────────────────────────┐
│ Hướng dẫn kiếm tiền với Shopee Affiliate 2026  │
│ ✅ Đã xuất bản  👁️ 1250 lượt xem               │
│ [Hướng Dẫn] [████████░░ 85%] 2026-01-25        │
│ [👁️] [✏️] [🗑️]                                  │
└──────────────────────────────────────────────────┘
```

---

## ✍️ **Create/Edit Post Dialog**

### Form Fields

```
┌─────────────────────────────────────┐
│ Tạo Bài Viết Mới               [X] │
├─────────────────────────────────────┤
│ Tiêu Đề:                            │
│ [_____________________________] 0/60│
│ ℹ️ Tốt nhất: 50-60 ký tự            │
│                                     │
│ Slug URL:                           │
│ https://domain.com/blog/[_______]   │
│                                     │
│ Danh Mục:        Trạng Thái:       │
│ [Hướng Dẫn ▼]   [Nháp ▼]           │
│                                     │
│ Mô Tả Meta (SEO):                   │
│ [_____________________________]     │
│ [_____________________________]     │
│ ℹ️ 0/160 ký tự (Tốt nhất: 150-160) │
│                                     │
│ Từ Khóa (Keywords):                 │
│ [shopee, hoàn tiền, affiliate]      │
│                                     │
│ Nội Dung:                           │
│ [_____________________________]     │
│ [_____________________________]     │
│ [_____________________________]     │
│                                     │
│ Hình Ảnh Đại Diện (URL):            │
│ [https://...]                       │
│                                     │
│ ┌─────────────────────────────┐     │
│ │ ✅ SEO Score: 85/100        │     │
│ │ ✅ Bài viết đã tối ưu tốt!  │     │
│ └─────────────────────────────┘     │
│                                     │
│         [Hủy] [Tạo Bài Viết]       │
└─────────────────────────────────────┘
```

---

## 🎯 **SEO Scoring System**

### Score Calculation

| Criteria | Points | Condition |
|----------|--------|-----------|
| **Title** | 30 | 50-60 chars (15 if has content) |
| **Meta Description** | 30 | 150-160 chars (15 if has content) |
| **Keywords** | 20 | Has keywords |
| **Content** | 10 | >300 chars |
| **Featured Image** | 10 | Has URL |
| **Total** | **100** | |

### Score Levels

```javascript
score >= 80  → ✅ Green  "Tốt"
score >= 60  → ⚠️ Orange "Trung bình"
score < 60   → ❌ Red    "Cần cải thiện"
```

### Real-time Validation

```
⚠️ Tiêu đề quá ngắn (nên 50-60 ký tự)
⚠️ Mô tả meta quá dài (nên 150-160 ký tự)
⚠️ Chưa có từ khóa
✅ Bài viết đã được tối ưu SEO tốt!
```

---

## 💡 **SEO Tips Card**

```
┌─────────────────────────────────────────┐
│ 💡 Tips SEO                            │
├─────────────────────────────────────────┤
│ ✏️ Tiêu Đề    📄 Mô Tả    🖼️ Hình Ảnh │
│ Hấp Dẫn       Meta        Alt Text     │
│                                         │
│ 50-60 ký tự   150-160 ký  Tối ưu      │
│ Từ khóa       tự          kích thước   │
└─────────────────────────────────────────┘
```

---

## 🎨 **UI Components**

### Navigation
```css
/* Sidebar */
width: 240px (normal) / 64px (collapsed)
background: white
shadow: 2px 0 8px rgba(0,0,0,0.1)

/* Logo Header */
background: linear-gradient(135deg, #ee4d2d, #ff6b35)
color: white

/* Active Menu */
background: #fff6f4
border-right: 3px solid #ee4d2d
```

### Blog Page
```css
/* SEO Progress Bar */
Green: #67c23a (score >= 80)
Orange: #e6a23c (score >= 60)
Red: #f56c6c (score < 60)

/* Category Tags */
type: warning (orange)

/* Status Tags */
published: success (green)
draft: info (blue)
```

---

## 🔄 **Data Flow**

### Navigation
```
App.vue
  ↓
Check route path
  ↓
Show/Hide Navigation
  ↓
Navigation component
  ↓
Active menu based on $route.path
```

### Blog CRUD
```
Load Posts
  ↓
Display in table
  ↓
Click "Tạo Bài Viết"
  ↓
Open dialog
  ↓
Fill form (auto SEO score)
  ↓
Save post
  ↓
Update table
```

---

## 📊 **Mock Data**

### Posts
```javascript
{
  id: 1,
  title: 'Hướng dẫn kiếm tiền với Shopee Affiliate 2026',
  slug: 'huong-dan-kiem-tien-shopee-affiliate-2026',
  category: 'Hướng Dẫn',
  status: 'published',
  metaDescription: 'Hướng dẫn chi tiết...',
  keywords: 'shopee affiliate, kiếm tiền online',
  views: 1250,
  seoScore: 85,
  createdAt: '2026-01-25'
}
```

### Categories
- Hướng Dẫn
- Mẹo Hay
- Tin Tức
- Review

---

## 🚀 **Usage**

### Navigate to Blog
```javascript
this.$router.push('/blog')
```

### Create Post
```javascript
// Click "Tạo Bài Viết Mới"
// Fill form
// SEO score auto-updates
// Click "Tạo Bài Viết"
```

### Edit Post
```javascript
// Click edit icon
// Form pre-filled
// Update & save
```

---

## ✅ **Features Summary**

### Navigation
- ✅ Desktop sidebar (collapsible)
- ✅ Mobile drawer
- ✅ Active state highlighting
- ✅ Smooth animations
- ✅ Logo header with gradient
- ✅ Conditional rendering (hide on home/login)

### Blog/SEO
- ✅ Posts table with expand
- ✅ Create/Edit dialog
- ✅ Real-time SEO scoring (0-100)
- ✅ SEO validation hints
- ✅ Category management
- ✅ Status (draft/published)
- ✅ View counter
- ✅ SEO tips card
- ✅ CRUD operations
- ✅ Mock data ready

---

## 🎯 **Menu Items**

| Icon | Label | Route | Auth |
|------|-------|-------|------|
| 🏠 | Dashboard | /dashboard | ✅ |
| ➕ | Tạo Link | /links/create | ✅ |
| 🔗 | Quản Lý Links | /links | ✅ |
| 📊 | Thống Kê | /analytics | ✅ |
| 📝 | Blog/SEO | /blog | ✅ |
| 👤 | Tài Khoản | /profile | ✅ |
| ⚙️ | Cài Đặt | /settings | ✅ |
| 🏠 | Trang Chủ | / | ❌ |

---

## 📱 **Responsive**

### Desktop
- Sidebar: Always visible
- Width: 240px (normal) / 64px (collapsed)
- Main content: margin-left auto-adjust

### Mobile
- Sidebar: Hidden
- Hamburger button: Fixed top-left
- Drawer: Slide from left
- Main content: Full width

---

**Created**: 2026-01-30  
**Version**: 1.0  
**Status**: ✅ COMPLETE
