import api from './api'

export default {
    searchProducts(query, params = {}) {
        return api.get('/v1/shopee/products/search', {
            params: { query, ...params }
        })
    },

    getProductDetails(productId) {
        return api.get(`/v1/shopee/products/${productId}`)
    },

    createAffiliateLink(productData) {
        return api.post('/v1/shopee/affiliate/create', productData)
    },

    getCategories() {
        return api.get('/v1/shopee/categories')
    }
}
