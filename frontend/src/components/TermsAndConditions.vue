<template>
  <div class="terms-component">
    <!-- Important Notice -->
    <el-alert
      type="warning"
      :closable="false"
      show-icon
      style="margin-bottom: 24px;"
    >
      <template slot="title">
        <strong>⚠️ LƯU Ý QUAN TRỌNG</strong>
      </template>
      <div style="line-height: 1.8;">
        <p style="margin: 8px 0; font-weight: 500;">
          • Số tiền hoàn có thể <strong>thấp hơn thực tế hoặc bằng 0đ</strong>
        </p>
        <p style="margin: 8px 0; font-weight: 500;">
          • Vui lòng đọc kỹ <strong>Điều kiện & Điều khoản</strong> bên dưới trước khi mua hàng
        </p>
      </div>
    </el-alert>

    <!-- Terms Card -->
    <el-card class="terms-card">
      <div slot="header">
        <span style="font-weight: bold; color: #ee4d2d; font-size: 16px;">
          <i class="el-icon-document"></i> ĐIỀU KHOẢN HOÀN TIỀN
        </span>
      </div>

      <el-collapse v-model="activeNames" accordion>
        <!-- Thời gian hoàn tiền -->
        <el-collapse-item name="1">
          <template slot="title">
            <strong><i class="el-icon-time"></i> THỜI GIAN HOÀN TIỀN</strong>
          </template>
          <div class="terms-content">
            <p>
              <i class="el-icon-check"></i>
              Thời gian cập nhật đơn hàng là <strong>24 - 48H</strong> kể từ khi đặt hàng và thanh toán thành công
            </p>
            <p>
              <i class="el-icon-warning"></i>
              Nếu quá 48H đơn không hiển thị tức là đơn không được ghi nhận
            </p>
            <p>
              <i class="el-icon-time"></i>
              Thời gian duyệt đơn hàng:
            </p>
            <ul>
              <li><strong>Shopee:</strong> 30 ngày</li>
              <li><strong>Lazada:</strong> 45 ngày</li>
              <li><strong>Tiktok:</strong> 7 ngày</li>
            </ul>
            <p style="font-size: 12px; color: #757575;">
              (Kể từ khi khách hàng bấm "Đã nhận hàng" trên app)
            </p>
            <p>
              <i class="el-icon-wallet"></i>
              Số dư hoàn tiền có thể rút tối thiểu là <strong>10.000đ</strong>
            </p>
            <p>
              <i class="el-icon-date"></i>
              Khả dụng rút số dư vào <strong>Thứ 7, CN</strong> hàng tuần
            </p>
          </div>
        </el-collapse-item>

        <!-- Điều kiện chấp nhận -->
        <el-collapse-item name="2">
          <template slot="title">
            <strong><i class="el-icon-success"></i> ĐIỀU KIỆN CHẤP NHẬN HOÀN TIỀN</strong>
          </template>
          <div class="terms-content">
            <el-alert
              type="info"
              :closable="false"
              style="margin-bottom: 16px;"
            >
              <strong>Số tiền hoàn được tính dựa trên số tiền cuối cùng khách thanh toán</strong>
              (sau khi đã áp mã giảm giá, miễn phí vận chuyển...) 
              nên số tiền có thể <strong>bằng 0đ hoặc bé hơn</strong> so với dự kiến trên web
            </el-alert>

            <p><strong>📦 Mua nhiều sản phẩm:</strong></p>
            <ul>
              <li>
                <strong>Cùng 1 Shop:</strong> Chỉ cần dán link 1 món tại Shop đó
              </li>
              <li>
                <strong>Nhiều Shop:</strong> Cần dán link mỗi Shop ít nhất 1 món
              </li>
              <li>
                <strong>Để tối đa hoàn tiền:</strong> Nên tách lẻ mỗi đơn hàng chỉ mua 1 Shop nhất định
              </li>
            </ul>

            <p><strong>📱 Điều kiện đơn hàng:</strong></p>
            <ul>
              <li>Chỉ chấp nhận các đơn hàng phát sinh trên <strong>điện thoại</strong></li>
              <li>Mỗi lần mua hàng chỉ mua <strong>1 đơn duy nhất</strong></li>
              <li>Mua đơn thứ 2 trở đi cần quay trở lại website để dán link hoàn tiền như ban đầu</li>
            </ul>

            <p><strong>✅ Được chấp nhận:</strong></p>
            <ul>
              <li>Bấm "Mua ngay" sau đó thêm sản phẩm vào giỏ hàng</li>
              <li>Áp các mã giảm giá vẫn được tính</li>
            </ul>

            <p><strong>❌ Không áp dụng:</strong></p>
            <ul>
              <li>Thêm giỏ từ video, live</li>
              <li>Mua hàng áp các mã video, live</li>
              <li style="font-size: 12px; color: #757575;">(Trừ một số trường hợp nhất định)</li>
            </ul>
          </div>
        </el-collapse-item>

        <!-- Hướng dẫn sử dụng -->
        <el-collapse-item name="3">
          <template slot="title">
            <strong><i class="el-icon-info"></i> HƯỚNG DẪN SỬ DỤNG</strong>
          </template>
          <div class="terms-content">
            <ol>
              <li>Dán link sản phẩm Shopee vào form</li>
              <li>Click "Tạo Link" để nhận link hoàn tiền</li>
              <li>Copy link và mở trên <strong>điện thoại</strong></li>
              <li>Mua hàng như bình thường</li>
              <li>Chờ 24-48H để đơn hàng được cập nhật</li>
              <li>Nhận hoàn tiền sau khi đơn được duyệt</li>
            </ol>
          </div>
        </el-collapse-item>

        <!-- Điều kiện từ chối -->
        <el-collapse-item name="4">
          <template slot="title">
            <strong style="color: #f56c6c;"><i class="el-icon-close"></i> ĐIỀU KIỆN TỪ CHỐI HOÀN TIỀN</strong>
          </template>
          <div class="terms-content">
            <el-alert
              type="error"
              :closable="false"
              style="margin-bottom: 16px;"
            >
              <strong>⚠️ QUAN TRỌNG: Đọc kỹ để tránh mất đơn!</strong>
            </el-alert>

            <p><strong style="color: #f56c6c;">🚫 Không ghi nhận hoàn tiền cho:</strong></p>
            
            <div style="background: #fff1f0; padding: 12px; border-radius: 4px; margin: 12px 0; border-left: 4px solid #f56c6c;">
              <p><strong>❌ Đơn hàng từ LiveStream và Video</strong></p>
              <ul>
                <li>Không áp dụng cho đơn hàng mua từ live stream</li>
                <li>Không áp dụng cho đơn hàng mua từ video</li>
                <li>Không áp dụng khi thêm giỏ từ video/live</li>
                <li>Không áp dụng khi áp mã giảm giá từ video/live</li>
                <li style="font-size: 12px; color: #757575;">
                  (Trừ một số trường hợp đặc biệt - liên hệ để biết thêm)
                </li>
              </ul>
            </div>

            <p><strong style="color: #f56c6c;">⚠️ Hoàn tiền có thể bị hủy hoặc bằng 0đ do:</strong></p>
            <ul>
              <li>Chính sách và quy định riêng của từng sàn (Shopee, Lazada, Tiktok)</li>
              <li>Longhousee chỉ đảm nhiệm vai trò thu thập dữ liệu từ sàn</li>
              <li>Không có quyền biết hoặc cung cấp lý do cụ thể hủy đơn</li>
            </ul>

            <p><strong style="color: #f56c6c;">🚨 Các đơn hàng có dấu hiệu GIAN LẬN sẽ KHÔNG được ghi nhận:</strong></p>
            
            <div style="background: #fff1f0; padding: 12px; border-radius: 4px; margin: 12px 0;">
              <p><strong>1. Hủy đơn, boom hàng:</strong></p>
              <ul>
                <li>Hủy đơn nhiều lần</li>
                <li>Boom hàng (đặt nhưng không nhận)</li>
                <li>Treo đơn không thanh toán bị hủy nhiều lần</li>
              </ul>

              <p><strong>2. Mua hàng bất thường:</strong></p>
              <ul>
                <li>Mua nhiều đơn hàng, mặt hàng giống nhau</li>
                <li>Mua cùng sản phẩm nhiều lần trong thời gian ngắn</li>
              </ul>

              <p><strong>3. Sử dụng nhiều tài khoản:</strong></p>
              <ul>
                <li>Sử dụng nhiều tài khoản trên cùng 1 thiết bị</li>
                <li>Sử dụng nhiều thiết bị chung thông tin nhận hàng:</li>
                <ul>
                  <li>Cùng WiFi</li>
                  <li>Cùng email</li>
                  <li>Cùng số điện thoại</li>
                  <li>Cùng khu vực nhận hàng</li>
                </ul>
              </ul>

              <p><strong>4. Lạm dụng mã giảm giá:</strong></p>
              <ul>
                <li>Sử dụng nhiều mã giảm giá giống nhau</li>
                <li>Sử dụng mã trùng lặp trong khoảng thời gian nhất định</li>
              </ul>

              <p><strong>5. Click nhiều link:</strong></p>
              <ul>
                <li>Click nhiều link từ nhiều KOL khác nhau để mua hàng</li>
                <li>Click nhiều link từ nhiều group khác nhau</li>
              </ul>
            </div>

            <el-alert
              type="warning"
              :closable="false"
              style="margin-top: 16px;"
            >
              <p style="margin: 0;">
                <strong>📌 Lưu ý quan trọng:</strong>
              </p>
              <ul style="margin: 8px 0 0 0; padding-left: 20px;">
                <li>Còn rất nhiều lý do khác có thể bị từ chối</li>
                <li>Các thông tin trên là tổng hợp từ kinh nghiệm thực tế</li>
                <li>Shopee không cung cấp lý do cụ thể hủy đơn</li>
                <li>Hãy mua hàng một cách tự nhiên, đúng nhu cầu</li>
              </ul>
            </el-alert>
          </div>
        </el-collapse-item>

        <!-- FAQ -->
        <el-collapse-item name="5">
          <template slot="title">
            <strong><i class="el-icon-question"></i> CÂU HỎI THƯỜNG GẶP</strong>
          </template>
          <div class="terms-content">
            <p><strong>Q: Tại sao số tiền hoàn bằng 0đ?</strong></p>
            <p>A: Vì bạn đã áp quá nhiều mã giảm giá, số tiền thanh toán cuối cùng quá thấp hoặc bằng 0.</p>

            <p><strong>Q: Tôi có thể mua trên máy tính không?</strong></p>
            <p>A: Không. Chỉ chấp nhận đơn hàng từ <strong>điện thoại</strong>.</p>

            <p><strong>Q: Mua nhiều đơn cùng lúc có được không?</strong></p>
            <p>A: Không. Mỗi lần chỉ mua <strong>1 đơn duy nhất</strong>.</p>

            <p><strong>Q: Đơn hàng không hiển thị sau 48H?</strong></p>
            <p>A: Đơn không được ghi nhận. Có thể do không mở link từ website, mua trên máy tính, hoặc mua nhiều đơn cùng lúc.</p>

            <p><strong>Q: Khi nào tôi có thể rút tiền?</strong></p>
            <p>A: Vào <strong>Thứ 7, Chủ Nhật</strong> hàng tuần, với số dư tối thiểu <strong>10.000đ</strong>.</p>
          </div>
        </el-collapse-item>
      </el-collapse>

        <div 
          :style="{
            marginTop: '16px',
            padding: '12px',
            background: agreedToTerms ? '#f0f9ff' : '#fff1f0',
            borderRadius: '4px',
            borderLeft: agreedToTerms ? '4px solid #26aa99' : '4px solid #f56c6c',
            transition: 'all 0.3s ease'
          }"
        >
          <p 
            :style="{
              margin: 0,
              color: agreedToTerms ? '#26aa99' : '#f56c6c',
              fontWeight: 500,
              transition: 'color 0.3s ease'
            }"
          >
            <i :class="agreedToTerms ? 'el-icon-success' : 'el-icon-warning'"></i>
            {{ agreedToTerms 
              ? '✅ Bạn đã đồng ý với các điều khoản trên' 
              : '⚠️ Bằng việc tạo link, bạn đã đồng ý với các điều khoản trên'
            }}
          </p>
        </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'TermsAndConditions',
  props: {
    agreedToTerms: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      activeNames: '1' // Default open first panel
    }
  }
}
</script>

<style scoped>
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

@media (max-width: 768px) {
  .terms-content {
    font-size: 14px;
  }
}
</style>
