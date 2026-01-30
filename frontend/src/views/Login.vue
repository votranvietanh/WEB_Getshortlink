<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2>Đăng Nhập</h2>
      <el-form ref="loginForm" :model="form" :rules="rules" label-position="top">
        <el-form-item label="Tên đăng nhập" prop="username">
          <el-input v-model="form.username" placeholder="Nhập tên đăng nhập"></el-input>
        </el-form-item>
        <el-form-item label="Mật khẩu" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="Nhập mật khẩu"
            @keyup.enter.native="handleLogin"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            style="width: 100%"
            @click="handleLogin"
          >
            Đăng Nhập
          </el-button>
        </el-form-item>
      </el-form>
      <div class="register-link">
        Chưa có tài khoản?
        <router-link to="/register">Đăng ký ngay</router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: 'Vui lòng nhập tên đăng nhập', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Vui lòng nhập mật khẩu', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(async(valid) => {
        if (valid) {
          this.loading = true
          try {
            await this.$store.dispatch('auth/login', this.form)
            this.$message.success('Đăng nhập thành công!')
            this.$router.push('/dashboard')
          } catch (error) {
            this.$message.error(error.response?.data?.message || 'Đăng nhập thất bại')
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 20px;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}

.register-link {
  text-align: center;
  margin-top: 20px;
  color: #606266;
}

.register-link a {
  color: #409eff;
  text-decoration: none;
}
</style>
