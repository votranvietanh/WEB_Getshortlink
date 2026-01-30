<template>
  <div id="app">
    <!-- Navigation (only show on authenticated routes) -->
    <Navigation v-if="showNavigation" />

    <!-- Main Content -->
    <div :class="{ 'main-content': showNavigation }">
      <router-view />
    </div>
  </div>
</template>

<script>
import Navigation from '@/components/Navigation.vue'

export default {
  name: 'App',
  components: {
    Navigation
  },
  computed: {
    showNavigation() {
      // Hide navigation on home/landing page
      const hideOnRoutes = ['/', '/login', '/register']
      return !hideOnRoutes.includes(this.$route.path)
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

/* Main content with sidebar */
.main-content {
  margin-left: 240px;
  transition: margin-left 0.3s ease;
}

/* Responsive */
@media (max-width: 768px) {
  .main-content {
    margin-left: 0;
  }
}
</style>
