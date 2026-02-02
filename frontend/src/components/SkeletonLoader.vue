<template>
  <div class="skeleton-wrapper">
    <!-- Card Skeleton -->
    <div v-if="type === 'card'" class="skeleton-card">
      <div class="skeleton skeleton-title"></div>
      <div class="skeleton skeleton-text"></div>
      <div class="skeleton skeleton-text" style="width: 80%;"></div>
      <div class="skeleton skeleton-text" style="width: 60%;"></div>
    </div>

    <!-- Balance Card Skeleton -->
    <div v-else-if="type === 'balance'" class="skeleton-balance-card">
      <div class="skeleton-balance-icon"></div>
      <div class="skeleton-balance-content">
        <div class="skeleton skeleton-text" style="width: 100px; height: 12px;"></div>
        <div class="skeleton skeleton-text" style="width: 150px; height: 32px; margin-top: 8px;"></div>
        <div class="skeleton skeleton-text" style="width: 120px; height: 10px; margin-top: 4px;"></div>
      </div>
    </div>

    <!-- Table Skeleton -->
    <div v-else-if="type === 'table'" class="skeleton-table">
      <div class="skeleton-table-header">
        <div class="skeleton skeleton-text" style="width: 60px;"></div>
        <div class="skeleton skeleton-text" style="width: 100px;"></div>
        <div class="skeleton skeleton-text" style="width: 150px;"></div>
        <div class="skeleton skeleton-text" style="width: 80px;"></div>
      </div>
      <div v-for="i in rows" :key="i" class="skeleton-table-row">
        <div class="skeleton skeleton-text" style="width: 60px;"></div>
        <div class="skeleton skeleton-text" style="width: 100px;"></div>
        <div class="skeleton skeleton-text" style="width: 150px;"></div>
        <div class="skeleton skeleton-text" style="width: 80px;"></div>
      </div>
    </div>

    <!-- Text Skeleton -->
    <div v-else-if="type === 'text'" class="skeleton-text-wrapper">
      <div v-for="i in rows" :key="i" class="skeleton skeleton-text" :style="{ width: getRandomWidth() }"></div>
    </div>

    <!-- Custom Skeleton -->
    <div v-else class="skeleton" :style="customStyle"></div>
  </div>
</template>

<script>
export default {
  name: 'SkeletonLoader',
  props: {
    type: {
      type: String,
      default: 'card',
      validator: (value) => ['card', 'balance', 'table', 'text', 'custom'].includes(value)
    },
    rows: {
      type: Number,
      default: 3
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '120px'
    }
  },
  computed: {
    customStyle() {
      return {
        width: this.width,
        height: this.height
      }
    }
  },
  methods: {
    getRandomWidth() {
      const widths = ['100%', '90%', '80%', '70%', '95%']
      return widths[Math.floor(Math.random() * widths.length)]
    }
  }
}
</script>

<style scoped>
.skeleton-wrapper {
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* Base Skeleton */
.skeleton {
  background: linear-gradient(
    90deg,
    #f0f0f0 0%,
    #f8f8f8 50%,
    #f0f0f0 100%
  );
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
  border-radius: 4px;
}

@keyframes shimmer {
  0% {
    background-position: -200% 0;
  }
  100% {
    background-position: 200% 0;
  }
}

/* Card Skeleton */
.skeleton-card {
  background: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.skeleton-title {
  height: 24px;
  width: 60%;
  margin-bottom: 16px;
}

.skeleton-text {
  height: 16px;
  margin-bottom: 8px;
}

/* Balance Card Skeleton */
.skeleton-balance-card {
  background: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  gap: 16px;
}

.skeleton-balance-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  background: linear-gradient(
    90deg,
    #f0f0f0 0%,
    #f8f8f8 50%,
    #f0f0f0 100%
  );
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
  flex-shrink: 0;
}

.skeleton-balance-content {
  flex: 1;
}

/* Table Skeleton */
.skeleton-table {
  background: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.skeleton-table-header,
.skeleton-table-row {
  display: flex;
  gap: 16px;
  padding: 12px 0;
}

.skeleton-table-header {
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 12px;
}

.skeleton-table-row {
  border-bottom: 1px solid #f5f5f5;
}

.skeleton-table-row:last-child {
  border-bottom: none;
}

/* Text Skeleton */
.skeleton-text-wrapper {
  padding: 16px 0;
}

/* Responsive */
@media (max-width: 768px) {
  .skeleton-balance-card {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .skeleton-table-header,
  .skeleton-table-row {
    flex-direction: column;
    gap: 8px;
  }
}
</style>
