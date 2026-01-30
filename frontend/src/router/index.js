import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { requiresAuth: false }
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/Login.vue'),
        meta: { requiresAuth: false }
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/views/Register.vue'),
        meta: { requiresAuth: false }
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/links',
        name: 'LinkManager',
        component: () => import('@/views/LinkManager.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/links/create',
        name: 'CreateLink',
        component: () => import('@/views/CreateLink.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/analytics',
        name: 'Analytics',
        component: () => import('@/views/Analytics.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/blog',
        name: 'Blog',
        component: () => import('@/views/Blog.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/settings',
        name: 'Settings',
        component: () => import('@/views/Settings.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '*',
        name: 'NotFound',
        component: () => import('@/views/NotFound.vue')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

// Navigation guard
router.beforeEach((to, from, next) => {
    // TEMPORARY: Disable authentication for testing
    next()
})

export default router
