<template>
    <div class="navigation">
        <!-- Mobile Menu Button -->
        <el-button class="mobile-menu-btn" icon="el-icon-menu" @click="drawer = true" circle></el-button>

        <!-- Desktop Sidebar -->
        <div class="sidebar" :class="{ 'collapsed': collapsed }">
            <!-- Logo -->
            <div class="sidebar-logo">
                <i class="el-icon-shopping-cart-2" style="font-size: 28px; color: white;"></i>
                <h3 v-if="!collapsed">Shopee Cashback</h3>
            </div>

            <!-- Collapse Button -->
            <div class="collapse-btn" @click="collapsed = !collapsed">
                <i :class="collapsed ? 'el-icon-d-arrow-right' : 'el-icon-d-arrow-left'"></i>
            </div>

            <!-- Menu Items -->
            <el-menu :default-active="activeMenu" :collapse="collapsed" :router="true" background-color="#ffffff"
                text-color="#333" active-text-color="#ee4d2d">
                <el-menu-item index="/dashboard">
                    <i class="el-icon-s-home"></i>
                    <span slot="title">Dashboard</span>
                </el-menu-item>

                <el-menu-item index="/links/create">
                    <i class="el-icon-plus"></i>
                    <span slot="title">Tạo Link</span>
                </el-menu-item>

                <el-menu-item index="/links">
                    <i class="el-icon-link"></i>
                    <span slot="title">Quản Lý Links</span>
                </el-menu-item>

                <el-menu-item index="/analytics">
                    <i class="el-icon-data-analysis"></i>
                    <span slot="title">Thống Kê</span>
                </el-menu-item>

                <el-menu-item index="/blog">
                    <i class="el-icon-document"></i>
                    <span slot="title">Blog/SEO</span>
                </el-menu-item>

                <el-divider></el-divider>

                <el-menu-item index="/profile">
                    <i class="el-icon-user"></i>
                    <span slot="title">Tài Khoản</span>
                </el-menu-item>

                <el-menu-item index="/settings">
                    <i class="el-icon-setting"></i>
                    <span slot="title">Cài Đặt</span>
                </el-menu-item>

                <el-menu-item index="/" class="logout-item">
                    <i class="el-icon-switch-button"></i>
                    <span slot="title">Trang Chủ</span>
                </el-menu-item>
            </el-menu>
        </div>

        <!-- Mobile Drawer -->
        <el-drawer :visible.sync="drawer" direction="ltr" size="280px" :with-header="false">
            <div class="mobile-menu">
                <div class="mobile-logo">
                    <i class="el-icon-shopping-cart-2" style="font-size: 32px; color: #ee4d2d;"></i>
                    <h3>Shopee Cashback</h3>
                </div>

                <el-menu :default-active="activeMenu" :router="true" @select="drawer = false">
                    <el-menu-item index="/dashboard">
                        <i class="el-icon-s-home"></i>
                        <span>Dashboard</span>
                    </el-menu-item>

                    <el-menu-item index="/links/create">
                        <i class="el-icon-plus"></i>
                        <span>Tạo Link</span>
                    </el-menu-item>

                    <el-menu-item index="/links">
                        <i class="el-icon-link"></i>
                        <span>Quản Lý Links</span>
                    </el-menu-item>

                    <el-menu-item index="/analytics">
                        <i class="el-icon-data-analysis"></i>
                        <span>Thống Kê</span>
                    </el-menu-item>

                    <el-menu-item index="/blog">
                        <i class="el-icon-document"></i>
                        <span>Blog/SEO</span>
                    </el-menu-item>

                    <el-divider></el-divider>

                    <el-menu-item index="/profile">
                        <i class="el-icon-user"></i>
                        <span>Tài Khoản</span>
                    </el-menu-item>

                    <el-menu-item index="/settings">
                        <i class="el-icon-setting"></i>
                        <span>Cài Đặt</span>
                    </el-menu-item>

                    <el-menu-item index="/">
                        <i class="el-icon-switch-button"></i>
                        <span>Trang Chủ</span>
                    </el-menu-item>
                </el-menu>
            </div>
        </el-drawer>
    </div>
</template>

<script>
export default {
    name: 'Navigation',
    data() {
        return {
            collapsed: false,
            drawer: false
        }
    },
    computed: {
        activeMenu() {
            return this.$route.path
        }
    }
}
</script>

<style scoped>
/* Mobile Menu Button */
.mobile-menu-btn {
    position: fixed;
    top: 16px;
    left: 16px;
    z-index: 1001;
    display: none;
    background: #ee4d2d;
    color: white;
    border: none;
}

.mobile-menu-btn:hover {
    background: #d43f1f;
}

/* Desktop Sidebar */
.sidebar {
    position: fixed;
    left: 0;
    top: 0;
    bottom: 0;
    width: 240px;
    background: white;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
    transition: width 0.3s ease;
    z-index: 1000;
    overflow-x: hidden;
}

.sidebar.collapsed {
    width: 64px;
}

/* Logo */
.sidebar-logo {
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    padding: 0 16px;
    border-bottom: 1px solid #f0f0f0;
    background: linear-gradient(135deg, #ee4d2d 0%, #ff6b35 100%);
    color: white;
}

.sidebar-logo h3 {
    margin: 0;
    font-size: 18px;
    font-weight: bold;
    white-space: nowrap;
}

/* Collapse Button */
.collapse-btn {
    position: absolute;
    right: -12px;
    top: 72px;
    width: 24px;
    height: 24px;
    background: #ee4d2d;
    color: white;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    box-shadow: 0 2px 8px rgba(238, 77, 45, 0.3);
    transition: all 0.3s ease;
    z-index: 1001;
}

.collapse-btn:hover {
    background: #d43f1f;
    transform: scale(1.1);
}

/* Menu */
.sidebar ::v-deep .el-menu {
    border-right: none;
    height: calc(100vh - 64px);
    overflow-y: auto;
}

.sidebar ::v-deep .el-menu-item {
    height: 50px;
    line-height: 50px;
    transition: all 0.3s ease;
}

.sidebar ::v-deep .el-menu-item:hover {
    background: #fff6f4 !important;
}

.sidebar ::v-deep .el-menu-item.is-active {
    background: #fff6f4 !important;
    border-right: 3px solid #ee4d2d;
}

.sidebar ::v-deep .el-menu-item i {
    color: #ee4d2d;
    font-size: 18px;
}

.logout-item {
    margin-top: auto;
}

/* Mobile Menu */
.mobile-menu {
    padding: 20px 0;
}

.mobile-logo {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    padding: 20px;
    margin-bottom: 20px;
}

.mobile-logo h3 {
    margin: 0;
    font-size: 20px;
    font-weight: bold;
    color: #333;
}

/* Responsive */
@media (max-width: 768px) {
    .mobile-menu-btn {
        display: block;
    }

    .sidebar {
        display: none;
    }
}

@media (min-width: 769px) {
    .mobile-menu-btn {
        display: none;
    }
}

/* Scrollbar */
.sidebar ::v-deep .el-menu::-webkit-scrollbar {
    width: 6px;
}

.sidebar ::v-deep .el-menu::-webkit-scrollbar-thumb {
    background: #ddd;
    border-radius: 3px;
}

.sidebar ::v-deep .el-menu::-webkit-scrollbar-thumb:hover {
    background: #ccc;
}
</style>
