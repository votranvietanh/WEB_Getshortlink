import axios from 'axios'
import { getToken, removeToken } from '@/utils/storage'
import router from '@/router'
import { Message } from 'element-ui'

// Create axios instance
const api = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080/api',
    timeout: parseInt(process.env.VUE_APP_API_TIMEOUT) || 30000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// Request interceptor
api.interceptors.request.use(
    config => {
        const token = getToken()
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    error => {
        console.error('Request error:', error)
        return Promise.reject(error)
    }
)

// Response interceptor
api.interceptors.response.use(
    response => {
        return response
    },
    error => {
        if (error.response) {
            const { status, data } = error.response

            switch (status) {
                case 401:
                    // Unauthorized - clear token and redirect to login
                    removeToken()
                    router.push('/login')
                    Message.error('Session expired. Please login again.')
                    break
                case 403:
                    Message.error('You do not have permission to perform this action.')
                    break
                case 404:
                    Message.error('Resource not found.')
                    break
                case 500:
                    Message.error('Server error. Please try again later.')
                    break
                default:
                    Message.error(data.message || 'An error occurred.')
            }
        } else if (error.request) {
            Message.error('Network error. Please check your connection.')
        } else {
            Message.error('An unexpected error occurred.')
        }

        return Promise.reject(error)
    }
)

export default api
