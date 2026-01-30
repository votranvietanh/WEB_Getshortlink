<template>
  <div class="not-found">
    <h1>404</h1>
    <p>Trang không tồn tại</p>
    <el-button type="primary" @click="$router.push('/')">
      Về Trang Chủ
    </el-button>
  </div>
</template>

<script>
export default {
  name: 'NotFound'
}
</script>

<style scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}

h1 {
  font-size: 120px;
  margin: 0;
  color: #409eff;
}

p {
  font-size: 24px;
  margin: 20px 0 40px;
  color: #606266;
}
</style>
