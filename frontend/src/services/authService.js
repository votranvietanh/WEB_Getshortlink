import api from './api'

export default {
    login(credentials) {
        return api.post('/v1/auth/login', credentials)
    },

    register(userData) {
        return api.post('/v1/auth/register', userData)
    },

    logout() {
        return api.post('/v1/auth/logout')
    },

    refreshToken(refreshToken) {
        return api.post('/v1/auth/refresh', { refreshToken })
    },

    getCurrentUser() {
        return api.get('/v1/auth/me')
    },

    updateProfile(userData) {
        return api.put('/v1/auth/profile', userData)
    },

    changePassword(passwordData) {
        return api.put('/v1/auth/password', passwordData)
    }
}
