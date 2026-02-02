<template>
  <span class="animated-number">{{ displayValue }}</span>
</template>

<script>
export default {
  name: 'AnimatedNumber',
  props: {
    value: {
      type: Number,
      required: true
    },
    duration: {
      type: Number,
      default: 1000
    },
    format: {
      type: Function,
      default: (val) => val.toLocaleString('vi-VN')
    }
  },
  data() {
    return {
      displayValue: this.format(0),
      currentValue: 0
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(newValue, oldValue) {
        this.animateValue(oldValue || 0, newValue)
      }
    }
  },
  methods: {
    animateValue(start, end) {
      const startTime = Date.now()
      const difference = end - start
      
      const step = () => {
        const now = Date.now()
        const elapsed = now - startTime
        const progress = Math.min(elapsed / this.duration, 1)
        
        // Easing function (ease-out cubic)
        const easeOut = 1 - Math.pow(1 - progress, 3)
        
        this.currentValue = start + (difference * easeOut)
        this.displayValue = this.format(Math.round(this.currentValue))
        
        if (progress < 1) {
          requestAnimationFrame(step)
        } else {
          this.currentValue = end
          this.displayValue = this.format(end)
        }
      }
      
      requestAnimationFrame(step)
    }
  }
}
</script>

<style scoped>
.animated-number {
  display: inline-block;
  font-variant-numeric: tabular-nums;
}
</style>
