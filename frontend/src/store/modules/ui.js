const state = {
    loading: false,
    sidebarCollapsed: false,
    notifications: []
}

const getters = {
    isLoading: state => state.loading,
    isSidebarCollapsed: state => state.sidebarCollapsed,
    notifications: state => state.notifications
}

const mutations = {
    SET_LOADING(state, loading) {
        state.loading = loading
    },
    TOGGLE_SIDEBAR(state) {
        state.sidebarCollapsed = !state.sidebarCollapsed
    },
    SET_SIDEBAR(state, collapsed) {
        state.sidebarCollapsed = collapsed
    },
    ADD_NOTIFICATION(state, notification) {
        state.notifications.push({
            id: Date.now(),
            ...notification
        })
    },
    REMOVE_NOTIFICATION(state, id) {
        state.notifications = state.notifications.filter(n => n.id !== id)
    },
    CLEAR_NOTIFICATIONS(state) {
        state.notifications = []
    }
}

const actions = {
    showNotification({ commit }, notification) {
        commit('ADD_NOTIFICATION', notification)

        // Auto remove after 5 seconds
        if (notification.duration !== 0) {
            setTimeout(() => {
                commit('REMOVE_NOTIFICATION', notification.id)
            }, notification.duration || 5000)
        }
    },

    removeNotification({ commit }, id) {
        commit('REMOVE_NOTIFICATION', id)
    }
}

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
}
