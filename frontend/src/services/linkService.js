import api from './api'

export default {
    getLinks(params = {}) {
        return api.get('/v1/links', { params })
    },

    getLinkById(id) {
        return api.get(`/v1/links/${id}`)
    },

    createLink(linkData) {
        return api.post('/v1/links', linkData)
    },

    updateLink(id, linkData) {
        return api.put(`/v1/links/${id}`, linkData)
    },

    deleteLink(id) {
        return api.delete(`/v1/links/${id}`)
    },

    getLinkStats(id) {
        return api.get(`/v1/links/${id}/stats`)
    },

    generateQRCode(id) {
        return api.get(`/v1/links/${id}/qrcode`, {
            responseType: 'blob'
        })
    }
}
