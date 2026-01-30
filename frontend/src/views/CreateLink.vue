<template>
  <div class="create-link-page">
    <!-- Header -->
    <div class="shopee-header">
      <div class="shopee-container">
        <h2><i class="el-icon-plus"></i> Tạo Link Hoàn Tiền</h2>
      </div>
    </div>

    <!-- Main Content -->
    <div class="shopee-container" style="padding-top: 24px; max-width: 900px;">

      <!-- Important Notice with Link to Terms -->
      <el-alert type="warning" :closable="false" show-icon style="margin-bottom: 24px;">
        <template slot="title">
          <strong>⚠️ LƯU Ý QUAN TRỌNG</strong>
        </template>
        <div style="line-height: 1.8;">
          <p style="margin: 8px 0; font-weight: 500;">
            • Số tiền hoàn có thể <strong>thấp hơn thực tế hoặc bằng 0đ</strong>
          </p>
          <p style="margin: 8px 0; font-weight: 500;">
            • Vui lòng
            <a href="#terms-section" @click.prevent="scrollToTerms"
              style="color: #ee4d2d; font-weight: bold; text-decoration: underline; cursor: pointer;">
              đọc kỹ Điều khoản & Điều kiện
            </a>
            trước khi mua hàng
          </p>
        </div>
      </el-alert>

      <!-- Create Link Form -->
      <el-card>
        <div slot="header">
          <span style="font-weight: bold; color: #ee4d2d;">
            <i class="el-icon-link"></i> Tạo Link Hoàn Tiền
          </span>
        </div>

        <el-form ref="linkForm" :model="form" :rules="rules" label-position="top">
          <!-- Dynamic Link Inputs -->
          <div class="link-inputs-container">
            <div v-for="(link, index) in form.links" :key="index" class="link-input-row">
              <div class="link-input-wrapper">
                <div class="link-number-circle">{{ index + 1 }}</div>
                <el-form-item :prop="`links.${index}.url`"
                  :rules="index === 0 ? [{ required: true, message: 'Link đầu tiên là bắt buộc', trigger: 'blur' }] : []"
                  style="flex: 1; margin-bottom: 0;">
                  <el-input v-model="link.url" :placeholder="`Link sản phẩm ${index + 1}`" prefix-icon="el-icon-link"
                    clearable size="large">
                  </el-input>
                </el-form-item>
                <el-button v-if="index > 0" type="danger" icon="el-icon-delete" circle size="small"
                  @click="removeLink(index)" class="remove-btn">
                </el-button>
              </div>

              <!-- Result for this link (if created) -->
              <div v-if="createdLinks[index]" class="inline-result">
                <div class="result-arrow">→</div>
                <div class="result-link-card">
                  <div class="result-link-header">
                    <i class="el-icon-success"></i> Link Hoàn Tiền
                  </div>
                  <el-input :value="createdLinks[index].shortUrl" readonly size="small">
                    <el-button slot="append" icon="el-icon-document-copy"
                      @click="copyUrl(createdLinks[index].shortUrl, index)" size="small">
                      Copy
                    </el-button>
                  </el-input>
                </div>
              </div>
            </div>

            <!-- Add Link Button -->
            <el-button v-if="form.links.length < 5" type="success" icon="el-icon-plus" @click="addLink"
              class="add-link-btn" plain>
              Thêm Link ({{ form.links.length }}/5)
            </el-button>
          </div>

          <!-- Agreement Checkbox -->
          <el-form-item style="margin-top: 24px;">
            <el-checkbox v-model="agreedToTerms">
              Tôi đã đọc và đồng ý với <strong>Điều khoản hoàn tiền</strong>
            </el-checkbox>
          </el-form-item>

          <!-- Submit Button -->
          <el-form-item>
            <el-button type="primary" size="large" :loading="loading" :disabled="!agreedToTerms || !hasAnyLink"
              @click="handleCreate" style="width: 100%;">
              <i class="el-icon-check"></i> Tạo {{ linkCount }} Link Hoàn Tiền
            </el-button>
            <p v-if="!agreedToTerms" style="margin-top: 8px; color: #f56c6c; font-size: 12px;">
              <i class="el-icon-warning"></i> Vui lòng đồng ý với điều khoản để tiếp tục
            </p>
            <p v-else-if="!hasAnyLink" style="margin-top: 8px; color: #f56c6c; font-size: 12px;">
              <i class="el-icon-warning"></i> Vui lòng nhập ít nhất 1 link sản phẩm
            </p>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- Success Summary (if any links created) -->
      <el-card v-if="createdLinks.length > 0" class="success-summary-card" style="margin-top: 24px;">
        <el-alert type="success" :closable="false">
          <template slot="title">
            <strong>🎉 Đã tạo {{ createdLinks.length }} link thành công!</strong>
          </template>
          <div style="margin-top: 12px;">
            <p style="margin: 4px 0;"><strong>Hướng dẫn:</strong></p>
            <ol style="margin: 8px 0; padding-left: 20px;">
              <li>Copy link hoàn tiền bên cạnh mỗi link gốc</li>
              <li>Mở link trên <strong>điện thoại</strong></li>
              <li>Mua hàng như bình thường</li>
              <li>Chờ nhận hoàn tiền</li>
            </ol>
            <el-alert type="warning" :closable="false" style="margin-top: 12px;">
              <strong>⚠️ Lưu ý:</strong> Chỉ mở trên điện thoại • Mỗi lần 1 đơn • Số tiền hoàn có thể thấp hơn
            </el-alert>
          </div>
        </el-alert>

        <div class="result-actions" style="margin-top: 16px;">
          <el-button type="primary" @click="$router.push('/links')">
            <i class="el-icon-document"></i> Xem Tất Cả Links
          </el-button>
          <el-button @click="resetForm">
            <i class="el-icon-plus"></i> Tạo Link Khác
          </el-button>
        </div>
      </el-card>

      <!-- Terms & Conditions Section (Bottom) -->
      <div id="terms-section" style="margin-top: 40px;">
        <TermsAndConditions :agreed-to-terms="agreedToTerms" />
      </div>
    </div>
  </div>
</template>

<script>
import TermsAndConditions from '@/components/TermsAndConditions.vue'

export default {
  name: 'CreateLink',
  components: {
    TermsAndConditions
  },
  data() {
    return {
      form: {
        links: [
          { url: '' }  // Start with 1 input
        ]
      },
      rules: {},
      loading: false,
      createdLinks: [],
      baseUrl: window.location.origin,
      agreedToTerms: false
    }
  },
  computed: {
    hasAnyLink() {
      return this.form.links.some(link => link.url && link.url.trim() !== '')
    },
    linkCount() {
      return this.form.links.filter(link => link.url && link.url.trim() !== '').length
    }
  },
  methods: {
    addLink() {
      if (this.form.links.length < 5) {
        this.form.links.push({ url: '' })
      }
    },
    removeLink(index) {
      if (index > 0) {
        this.form.links.splice(index, 1)
        // Also remove corresponding created link if exists
        if (this.createdLinks[index]) {
          this.createdLinks.splice(index, 1)
        }
      }
    },
    async handleCreate() {
      if (!this.agreedToTerms) {
        this.$message.warning('Vui lòng đồng ý với điều khoản hoàn tiền')
        return
      }

      if (!this.hasAnyLink) {
        this.$message.warning('Vui lòng nhập ít nhất 1 link sản phẩm')
        return
      }

      this.loading = true
      try {
        // Mock API call
        await new Promise(resolve => setTimeout(resolve, 1000))

        // Create links maintaining the same order as inputs
        // Use sparse array to keep indices aligned
        this.createdLinks = this.form.links.map((link, index) => {
          if (link.url && link.url.trim() !== '') {
            const shortCode = this.generateRandomCode()
            return {
              shortCode: shortCode,
              shortUrl: `${this.baseUrl}/r/${shortCode}`,
              originalUrl: link.url
            }
          }
          return null
        })

        const count = this.createdLinks.filter(l => l !== null).length
        this.$message.success(`Đã tạo ${count} link thành công!`)

        // No scroll needed - results appear inline
      } catch (error) {
        this.$message.error('Tạo link thất bại: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    generateRandomCode() {
      return Math.random().toString(36).substring(2, 10)
    },
    copyUrl(url, index) {
      navigator.clipboard.writeText(url)
      this.$message.success(`Đã copy Link ${index + 1}! Hãy mở trên điện thoại nhé!`)
    },
    resetForm() {
      this.form = {
        links: [{ url: '' }]
      }
      this.createdLinks = []
      this.agreedToTerms = false
      window.scrollTo({ top: 0, behavior: 'smooth' })
    },
    scrollToTerms() {
      this.$nextTick(() => {
        const termsSection = document.getElementById('terms-section')
        if (termsSection) {
          const yOffset = -20 // Offset 20px from top
          const y = termsSection.getBoundingClientRect().top + window.pageYOffset + yOffset

          window.scrollTo({
            top: y,
            behavior: 'smooth'
          })

          // Alternative method if first doesn't work
          // termsSection.scrollIntoView({ 
          //   behavior: 'smooth', 
          //   block: 'start' 
          // })
        } else {
          console.error('Terms section not found!')
        }
      })
    }
  }
}
</script>

<style scoped>
.create-link-page {
  min-height: 100vh;
  background: #f5f5f5;
}

/* Link Inputs Container */
.link-inputs-container {
  margin-bottom: 20px;
}

.link-input-row {
  margin-bottom: 20px;
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Link Input Wrapper */
.link-input-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.link-number-circle {
  flex-shrink: 0;
  width: 36px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  background: linear-gradient(135deg, #ee4d2d 0%, #ff6b35 100%);
  color: white;
  border-radius: 50%;
  font-weight: bold;
  font-size: 16px;
  box-shadow: 0 2px 8px rgba(238, 77, 45, 0.3);
}

.remove-btn {
  flex-shrink: 0;
}

/* Inline Result */
.inline-result {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-left: 48px;
  padding: 12px;
  background: linear-gradient(135deg, #f0f9ff 0%, #e8f5e9 100%);
  border-radius: 8px;
  border: 2px solid #26aa99;
  animation: slideInRight 0.4s ease-out;
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }

  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.result-arrow {
  font-size: 24px;
  color: #26aa99;
  font-weight: bold;
  flex-shrink: 0;
}

.result-link-card {
  flex: 1;
}

.result-link-header {
  font-size: 13px;
  font-weight: 600;
  color: #26aa99;
  margin-bottom: 8px;
}

/* Add Link Button */
.add-link-btn {
  width: 100%;
  margin-top: 12px;
  border: 2px dashed #26aa99;
  color: #26aa99;
  font-weight: 600;
  transition: all 0.3s ease;
}

.add-link-btn:hover {
  background: #f0f9ff;
  border-color: #1e8e7e;
  color: #1e8e7e;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(38, 170, 153, 0.2);
}

/* Success Summary Card */
.success-summary-card {
  border: 2px solid #e8f5e9;
  box-shadow: 0 4px 16px rgba(38, 170, 153, 0.1);
}

.terms-card {
  border: 2px solid #fff6f4;
}

.terms-content {
  line-height: 1.8;
  color: #333;
}

.terms-content p {
  margin: 12px 0;
}

.terms-content ul,
.terms-content ol {
  margin: 8px 0;
  padding-left: 24px;
}

.terms-content li {
  margin: 8px 0;
}

.terms-content strong {
  color: #ee4d2d;
}

.terms-content i {
  margin-right: 4px;
  color: #ee4d2d;
}

.result-content {
  padding: 16px 0;
}

.result-item {
  margin-bottom: 20px;
}

.result-item label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.url-display {
  margin-bottom: 16px;
}

.result-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
  margin-top: 24px;
}

/* Collapse customization */
::v-deep .el-collapse-item__header {
  font-size: 15px;
  padding: 12px 16px;
  background: #fff6f4;
  border-radius: 4px;
  margin-bottom: 8px;
}

::v-deep .el-collapse-item__header:hover {
  background: #ffe8e0;
}

::v-deep .el-collapse-item__wrap {
  border: none;
}

::v-deep .el-collapse-item__content {
  padding: 16px;
  background: white;
  border-radius: 4px;
  border: 1px solid #f0f0f0;
}

/* Form item spacing */
::v-deep .link-input-wrapper .el-form-item {
  margin-bottom: 0;
}

::v-deep .el-form-item__error {
  position: relative;
  top: 2px;
}

/* Responsive */
@media (max-width: 768px) {
  .link-input-wrapper {
    flex-wrap: wrap;
  }

  .link-number-circle {
    width: 32px;
    height: 32px;
    line-height: 32px;
    font-size: 14px;
  }

  .inline-result {
    flex-direction: column;
    margin-left: 0;
    gap: 8px;
  }

  .result-arrow {
    transform: rotate(90deg);
    font-size: 20px;
  }

  .result-actions {
    flex-direction: column;
  }

  .result-actions .el-button {
    width: 100%;
  }

  .terms-content {
    font-size: 14px;
  }
}

@media (min-width: 769px) {
  .inline-result {
    max-width: 600px;
  }
}
</style>
