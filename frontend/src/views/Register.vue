<template>
  <div class="register-page">
    <el-card class="register-card">
      <h2>Đăng Ký</h2>
      <el-form ref="registerForm" :model="form" :rules="rules" label-position="top">
        <el-form-item label="Tên đăng nhập" prop="username">
          <el-input v-model="form.username" placeholder="Nhập tên đăng nhập"></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="form.email" type="email" placeholder="Nhập email"></el-input>
        </el-form-item>
        <el-form-item label="Họ tên" prop="fullName">
          <el-input v-model="form.fullName" placeholder="Nhập họ tên"></el-input>
        </el-form-item>
        <el-form-item label="Mật khẩu" prop="password">
          <el-input v-model="form.password" type="password" placeholder="Nhập mật khẩu"></el-input>
        </el-form-item>
        <el-form-item label="Xác nhận mật khẩu" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="Nhập lại mật khẩu"
            @keyup.enter.native="handleRegister"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            style="width: 100%"
            @click="handleRegister"
          >
            Đăng Ký
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-link">
        Đã có tài khoản?
        <router-link to="/login">Đăng nhập</router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error('Mật khẩu không khớp'))
      } else {
        callback()
      }
    }
    return {
      form: {
        username: '',
        email: '',
        fullName: '',
        password: '',
        confirmPassword: ''
      },
      rules: {
        username: [
          { required: true, message: 'Vui lòng nhập tên đăng nhập', trigger: 'blur' },
          { min: 3, max: 50, message: 'Tên đăng nhập từ 3-50 ký tự', trigger: 'blur' }
        ],
        email: [
          { required: true, message: 'Vui lòng nhập email', trigger: 'blur' },
          { type: 'email', message: 'Email không hợp lệ', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Vui lòng nhập mật khẩu', trigger: 'blur' },
          { min: 6, message: 'Mật khẩu tối thiểu 6 ký tự', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: 'Vui lòng xác nhận mật khẩu', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleRegister() {
      this.$refs.registerForm.validate(async(valid) => {
        if (valid) {
          this.loading = true
          try {
            const { confirmPassword, ...registerData } = this.form
            await this.$store.dispatch('auth/register', registerData)
            this.$message.success('Đăng ký thành công!')
            this.$router.push('/dashboard')
          } catch (error) {
            this.$message.error(error.response?.data?.message || 'Đăng ký thất bại')
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
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-card {
  width: 450px;
  padding: 20px;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}

.login-link {
  text-align: center;
  margin-top: 20px;
  color: #606266;
}

.login-link a {
  color: #409eff;
  text-decoration: none;
}
</style>
