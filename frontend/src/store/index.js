import Vue from 'vue'
import Vuex from 'vuex'
import auth from './modules/auth'
import link from './modules/link'
import shopee from './modules/shopee'
import analytics from './modules/analytics'
import ui from './modules/ui'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        auth,
        link,
        shopee,
        analytics,
        ui
    },
    strict: process.env.NODE_ENV !== 'production'
})
