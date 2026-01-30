import linkService from '@/services/linkService'

const state = {
    links: [],
    currentLink: null,
    loading: false,
    error: null,
    pagination: {
        page: 1,
        size: 10,
        total: 0
    }
}

const getters = {
    allLinks: state => state.links,
    currentLink: state => state.currentLink,
    isLoading: state => state.loading,
    error: state => state.error,
    pagination: state => state.pagination
}

const mutations = {
    SET_LINKS(state, links) {
        state.links = links
    },
    SET_CURRENT_LINK(state, link) {
        state.currentLink = link
    },
    ADD_LINK(state, link) {
        state.links.unshift(link)
    },
    UPDATE_LINK(state, updatedLink) {
        const index = state.links.findIndex(link => link.id === updatedLink.id)
        if (index !== -1) {
            state.links.splice(index, 1, updatedLink)
        }
    },
    DELETE_LINK(state, linkId) {
        state.links = state.links.filter(link => link.id !== linkId)
    },
    SET_LOADING(state, loading) {
        state.loading = loading
    },
    SET_ERROR(state, error) {
        state.error = error
    },
    SET_PAGINATION(state, pagination) {
        state.pagination = { ...state.pagination, ...pagination }
    }
}

const actions = {
    async fetchLinks({ commit }, params = {}) {
        try {
            commit('SET_LOADING', true)
            commit('SET_ERROR', null)

            const response = await linkService.getLinks(params)
            commit('SET_LINKS', response.data.content)
            commit('SET_PAGINATION', {
                page: response.data.page,
                size: response.data.size,
                total: response.data.total
            })

            return response
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Failed to fetch links')
            throw error
        } finally {
            commit('SET_LOADING', false)
        }
    },

    async createLink({ commit }, linkData) {
        try {
            commit('SET_LOADING', true)
            commit('SET_ERROR', null)

            const response = await linkService.createLink(linkData)
            commit('ADD_LINK', response.data)

            return response
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Failed to create link')
            throw error
        } finally {
            commit('SET_LOADING', false)
        }
    },

    async updateLink({ commit }, { id, data }) {
        try {
            commit('SET_LOADING', true)
            commit('SET_ERROR', null)

            const response = await linkService.updateLink(id, data)
            commit('UPDATE_LINK', response.data)

            return response
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Failed to update link')
            throw error
        } finally {
            commit('SET_LOADING', false)
        }
    },

    async deleteLink({ commit }, linkId) {
        try {
            commit('SET_LOADING', true)
            commit('SET_ERROR', null)

            await linkService.deleteLink(linkId)
            commit('DELETE_LINK', linkId)
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Failed to delete link')
            throw error
        } finally {
            commit('SET_LOADING', false)
        }
    },

    async fetchLinkById({ commit }, linkId) {
        try {
            commit('SET_LOADING', true)
            commit('SET_ERROR', null)

            const response = await linkService.getLinkById(linkId)
            commit('SET_CURRENT_LINK', response.data)

            return response
        } catch (error) {
            commit('SET_ERROR', error.response?.data?.message || 'Failed to fetch link')
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
