module.exports = {
    publicPath: process.env.NODE_ENV === 'production' ? '/' : '/',
    outputDir: 'dist',
    assetsDir: 'static',
    lintOnSave: process.env.NODE_ENV !== 'production',
    productionSourceMap: false,

    devServer: {
        port: 8081,
        open: true,
        overlay: {
            warnings: false,
            errors: true
        },
        proxy: {
            '/api': {
                target: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080',
                changeOrigin: true,
                pathRewrite: {
                    '^/api': '/api'
                }
            }
        }
    },

    css: {
        loaderOptions: {
            sass: {
                additionalData: `@import "@/assets/styles/variables.scss";`
            }
        }
    },

    chainWebpack: config => {
        config.plugin('html').tap(args => {
            args[0].title = 'Shopee Affiliate Link Shortener'
            return args
        })
    }
}
