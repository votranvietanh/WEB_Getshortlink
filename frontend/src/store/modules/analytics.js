import analyticsService from '@/services/analyticsService'

const state = {
    dashboard: null,
    linkAnalytics: null,
    loading: false,
    error: null
}

const getters = {
    dashboard: state => state.dashboard,
    linkAnalytics: state => state.linkAnalytics,
    isLoading: state => state.loading,
    error: state => state.error
}

const mutations = {
    SET_DASHBOARD(state, data) {
        state.dashboard = data
    },
    SET_LINK_ANALYTICS(state, data) {
        state.linkAnalytics = data
    },
    SET_LOADING(state, loading) {
        state.loading = loading
    },
    SET_ERROR(state, error) {
        state.error = error
    }
}

const actions = {
    async fetchDashboard({ commit }) {
        try {
            commit('SET_LOADING', true)
            commit('SET_ERROR', null)

            const response = await analyticsService.getDashboard()
            commit('SET_DASHBOARD', response.data)

            return response
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Failed to fetch dashboard')
            throw error
        } finally {
            commit('SET_LOADING', false)
        }
    },

    async fetchLinkAnalytics({ commit }, linkId) {
        try {
            commit('SET_LOADING', true)
            commit('SET_ERROR', null)

            const response = await analyticsService.getLinkAnalytics(linkId)
            commit('SET_LINK_ANALYTICS', response.data)

            return response
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Failed to fetch link analytics')
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
