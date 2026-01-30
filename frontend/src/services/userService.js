import api from './api'

export default {
    getUserProfile() {
        return api.get('/v1/users/profile')
    },

    updateUserProfile(userData) {
        return api.put('/v1/users/profile', userData)
    },

    updateShopeeSettings(settings) {
        return api.put('/v1/users/shopee-settings', settings)
    },

    getApiKeys() {
        return api.get('/v1/users/api-keys')
    },

    createApiKey(keyData) {
        return api.post('/v1/users/api-keys', keyData)
    },

    deleteApiKey(keyId) {
        return api.delete(`/v1/users/api-keys/${keyId}`)
    }
}
