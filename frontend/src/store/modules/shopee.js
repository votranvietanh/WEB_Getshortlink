import shopeeService from '@/services/shopeeService'

const state = {
    products: [],
    currentProduct: null,
    loading: false,
    error: null,
    searchQuery: ''
}

const getters = {
    products: state => state.products,
    currentProduct: state => state.currentProduct,
    isLoading: state => state.loading,
    error: state => state.error,
    searchQuery: state => state.searchQuery
}

const mutations = {
    SET_PRODUCTS(state, products) {
        state.products = products
    },
    SET_CURRENT_PRODUCT(state, product) {
        state.currentProduct = product
    },
    SET_LOADING(state, loading) {
        state.loading = loading
    },
    SET_ERROR(state, error) {
        state.error = error
    },
    SET_SEARCH_QUERY(state, query) {
        state.searchQuery = query
    }
}

const actions = {
    async searchProducts({ commit }, query) {
        try {
            commit('SET_LOADING', true)
            commit('SET_ERROR', null)
            commit('SET_SEARCH_QUERY', query)

            const response = await shopeeService.searchProducts(query)
            commit('SET_PRODUCTS', response.data)

            return response
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Failed to search products')
            throw error
        } finally {
            commit('SET_LOADING', false)
        }
    },

    async createAffiliateLink({ commit }, productId) {
        try {
            commit('SET_LOADING', true)
            commit('SET_ERROR', null)

            const response = await shopeeService.createAffiliateLink(productId)

            return response
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Failed to create affiliate link')
            throw error
        } finally {
            commit('SET_LOADING', false)
        }
    }
}

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
}
