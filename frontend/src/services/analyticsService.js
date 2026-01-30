import api from './api'

export default {
    getDashboard(params = {}) {
        return api.get('/v1/analytics/dashboard', { params })
    },

    getLinkAnalytics(linkId, params = {}) {
        return api.get(`/v1/analytics/links/${linkId}`, { params })
    },

    getClicksByDate(params = {}) {
        return api.get('/v1/analytics/clicks/by-date', { params })
    },

    getClicksByDevice(params = {}) {
        return api.get('/v1/analytics/clicks/by-device', { params })
    },

    getClicksByLocation(params = {}) {
        return api.get('/v1/analytics/clicks/by-location', { params })
    },

    getConversions(params = {}) {
        return api.get('/v1/analytics/conversions', { params })
    },

    exportReport(params = {}) {
        return api.get('/v1/analytics/export', {
            params,
            responseType: 'blob'
        })
    }
}
