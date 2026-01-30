import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/styles/main.css'
import './assets/styles/shopee-theme.css'

// Configure Element UI
Vue.use(ElementUI)

// Global error handler
Vue.config.errorHandler = (err, vm, info) => {
    console.error('Global error:', err, info)
    // You can send error to logging service here
}

Vue.config.productionTip = false

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
