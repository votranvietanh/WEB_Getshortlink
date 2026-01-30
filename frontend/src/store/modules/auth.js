import authService from '@/services/authService'
import { getToken, setToken, removeToken } from '@/utils/storage'

const state = {
    token: getToken(),
    user: null,
    loading: false,
    error: null
}

const getters = {
    isAuthenticated: state => !!state.token,
    currentUser: state => state.user,
    isLoading: state => state.loading,
    error: state => state.error
}

const mutations = {
    SET_TOKEN(state, token) {
        state.token = token
    },
    SET_USER(state, user) {
        state.user = user
    },
    SET_LOADING(state, loading) {
        state.loading = loading
    },
    SET_ERROR(state, error) {
        state.error = error
    },
    CLEAR_AUTH(state) {
        state.token = null
        state.user = null
        state.error = null
    }
}

const actions = {
    async login({ commit }, credentials) {
        try {
            commit('SET_LOADING', true)
            commit('SET_ERROR', null)

            const response = await authService.login(credentials)
            const { token, user } = response.data

            commit('SET_TOKEN', token)
            commit('SET_USER', user)
            setToken(token)

            return response
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Login failed')
            throw error
        } finally {
            commit('SET_LOADING', false)
        }
    },

    async register({ commit }, userData) {
        try {
            commit('SET_LOADING', true)
            commit('SET_ERROR', null)

            const response = await authService.register(userData)
            const { token, user } = response.data

            commit('SET_TOKEN', token)
            commit('SET_USER', user)
            setToken(token)

            return response
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Registration failed')
            throw error
        } finally {
            commit('SET_LOADING', false)
        }
    },

    async logout({ commit }) {
        try {
            await authService.logout()
        } catch (error) {
            console.error('Logout error:', error)
        } finally {
            commit('CLEAR_AUTH')
            removeToken()
        }
    },

    async fetchUser({ commit }) {
        try {
            const response = await authService.getCurrentUser()
            commit('SET_USER', response.data)
            return response
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Failed to fetch user')
            throw error
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
