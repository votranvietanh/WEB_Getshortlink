<template>
    <div class="blog-page">
        <!-- Header -->
        <div class="shopee-header">
            <div class="shopee-container">
                <h2><i class="el-icon-document"></i> Quản Lý Blog & SEO</h2>
            </div>
        </div>

        <!-- Main Content -->
        <div class="shopee-container" style="padding-top: 24px;">

            <!-- Action Buttons -->
            <div class="action-bar">
                <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog = true">
                    Tạo Bài Viết Mới
                </el-button>
                <el-button icon="el-icon-refresh" @click="loadPosts">Làm Mới</el-button>
            </div>

            <!-- Posts List -->
            <el-card style="margin-top: 20px;">
                <div slot="header" class="card-header">
                    <span style="font-weight: bold; color: #ee4d2d;">
                        <i class="el-icon-reading"></i> Danh Sách Bài Viết
                    </span>
                    <el-tag>{{ posts.length }} bài viết</el-tag>
                </div>

                <el-table :data="posts" style="width: 100%">
                    <el-table-column label="Tiêu Đề" min-width="300">
                        <template slot-scope="scope">
                            <div class="post-title-cell">
                                <h4>{{ scope.row.title }}</h4>
                                <div class="post-meta">
                                    <el-tag size="mini" :type="scope.row.status === 'published' ? 'success' : 'info'">
                                        {{ scope.row.status === 'published' ? 'Đã xuất bản' : 'Nháp' }}
                                    </el-tag>
                                    <span class="meta-item">
                                        <i class="el-icon-view"></i> {{ scope.row.views }} lượt xem
                                    </span>
                                </div>
                            </div>
                        </template>
                    </el-table-column>

                    <el-table-column label="Danh Mục" width="150">
                        <template slot-scope="scope">
                            <el-tag size="small" type="warning">{{ scope.row.category }}</el-tag>
                        </template>
                    </el-table-column>

                    <el-table-column label="SEO Score" width="120" align="center">
                        <template slot-scope="scope">
                            <el-progress :percentage="scope.row.seoScore" :color="getSEOColor(scope.row.seoScore)"
                                :stroke-width="8"></el-progress>
                        </template>
                    </el-table-column>

                    <el-table-column label="Ngày Tạo" width="110">
                        <template slot-scope="scope">
                            <span class="date-text">{{ scope.row.createdAt }}</span>
                        </template>
                    </el-table-column>

                    <el-table-column label="Thao Tác" width="180" align="center">
                        <template slot-scope="scope">
                            <el-button-group>
                                <el-button size="mini" icon="el-icon-view" @click="viewPost(scope.row)"></el-button>
                                <el-button size="mini" icon="el-icon-edit" @click="editPost(scope.row)"></el-button>
                                <el-button size="mini" icon="el-icon-delete" type="danger"
                                    @click="deletePost(scope.row)"></el-button>
                            </el-button-group>
                        </template>
                    </el-table-column>
                </el-table>

                <el-empty v-if="posts.length === 0" description="Chưa có bài viết nào">
                    <el-button type="primary" @click="showCreateDialog = true">
                        Tạo Bài Viết Đầu Tiên
                    </el-button>
                </el-empty>
            </el-card>

            <!-- SEO Tips Card -->
            <el-card style="margin-top: 20px;">
                <div slot="header">
                    <span style="font-weight: bold; color: #26aa99;">
                        <i class="el-icon-info"></i> Tips SEO
                    </span>
                </div>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <div class="tip-item">
                            <i class="el-icon-edit"></i>
                            <h4>Tiêu Đề Hấp Dẫn</h4>
                            <p>Sử dụng từ khóa chính trong tiêu đề, giới hạn 50-60 ký tự</p>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="tip-item">
                            <i class="el-icon-document"></i>
                            <h4>Mô Tả Meta</h4>
                            <p>Viết mô tả ngắn gọn, hấp dẫn 150-160 ký tự</p>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="tip-item">
                            <i class="el-icon-picture"></i>
                            <h4>Hình Ảnh</h4>
                            <p>Thêm alt text cho hình ảnh, tối ưu kích thước</p>
                        </div>
                    </el-col>
                </el-row>
            </el-card>
        </div>

        <!-- Create/Edit Dialog -->
        <el-dialog :title="editingPost ? 'Chỉnh Sửa Bài Viết' : 'Tạo Bài Viết Mới'" :visible.sync="showCreateDialog"
            width="900px" top="5vh">
            <el-form :model="postForm" label-position="top">
                <el-form-item label="Tiêu Đề">
                    <el-input v-model="postForm.title" placeholder="Nhập tiêu đề bài viết..." @input="updateSEOScore">
                        <template slot="append">{{ postForm.title.length }}/60</template>
                    </el-input>
                    <div class="seo-hint"
                        :class="{ 'good': postForm.title.length >= 50 && postForm.title.length <= 60 }">
                        <i class="el-icon-info"></i> Tốt nhất: 50-60 ký tự
                    </div>
                </el-form-item>

                <el-form-item label="Slug URL">
                    <el-input v-model="postForm.slug" placeholder="bai-viet-mau">
                        <template slot="prepend">https://domain.com/blog/</template>
                    </el-input>
                </el-form-item>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="Danh Mục">
                            <el-select v-model="postForm.category" placeholder="Chọn danh mục" style="width: 100%;">
                                <el-option label="Hướng Dẫn" value="Hướng Dẫn"></el-option>
                                <el-option label="Mẹo Hay" value="Mẹo Hay"></el-option>
                                <el-option label="Tin Tức" value="Tin Tức"></el-option>
                                <el-option label="Review" value="Review"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="Trạng Thái">
                            <el-select v-model="postForm.status" placeholder="Chọn trạng thái" style="width: 100%;">
                                <el-option label="Nháp" value="draft"></el-option>
                                <el-option label="Xuất Bản" value="published"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="Mô Tả Meta (SEO)">
                    <el-input v-model="postForm.metaDescription" type="textarea" :rows="3"
                        placeholder="Mô tả ngắn gọn về bài viết..." @input="updateSEOScore"></el-input>
                    <div class="seo-hint"
                        :class="{ 'good': postForm.metaDescription.length >= 150 && postForm.metaDescription.length <= 160 }">
                        <i class="el-icon-info"></i> {{ postForm.metaDescription.length }}/160 ký tự (Tốt nhất: 150-160)
                    </div>
                </el-form-item>

                <el-form-item label="Từ Khóa (Keywords)">
                    <el-input v-model="postForm.keywords" placeholder="shopee, hoàn tiền, affiliate..."></el-input>
                    <div class="seo-hint">
                        <i class="el-icon-info"></i> Phân cách bằng dấu phẩy
                    </div>
                </el-form-item>

                <el-form-item label="Nội Dung">
                    <el-input v-model="postForm.content" type="textarea" :rows="12"
                        placeholder="Viết nội dung bài viết tại đây...&#10;&#10;Sử dụng markdown để format:&#10;# Tiêu đề 1&#10;## Tiêu đề 2&#10;**Chữ đậm**&#10;*Chữ nghiêng*&#10;[Link](url)"></el-input>
                </el-form-item>

                <el-form-item label="Hình Ảnh Đại Diện (URL)">
                    <el-input v-model="postForm.featuredImage" placeholder="https://..."></el-input>
                </el-form-item>

                <!-- SEO Score Preview -->
                <el-alert :type="currentSEOScore >= 80 ? 'success' : currentSEOScore >= 60 ? 'warning' : 'error'"
                    :closable="false" style="margin-top: 20px;">
                    <template slot="title">
                        <strong>SEO Score: {{ currentSEOScore }}/100</strong>
                    </template>
                    <div style="margin-top: 8px;">
                        <p v-if="postForm.title.length < 50">⚠️ Tiêu đề quá ngắn (nên 50-60 ký tự)</p>
                        <p v-if="postForm.title.length > 60">⚠️ Tiêu đề quá dài (nên 50-60 ký tự)</p>
                        <p v-if="postForm.metaDescription.length < 150">⚠️ Mô tả meta quá ngắn (nên 150-160 ký tự)</p>
                        <p v-if="postForm.metaDescription.length > 160">⚠️ Mô tả meta quá dài (nên 150-160 ký tự)</p>
                        <p v-if="!postForm.keywords">⚠️ Chưa có từ khóa</p>
                        <p v-if="currentSEOScore >= 80" style="color: #67c23a;">✅ Bài viết đã được tối ưu SEO tốt!</p>
                    </div>
                </el-alert>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="showCreateDialog = false">Hủy</el-button>
                <el-button type="primary" @click="savePost">
                    {{ editingPost ? 'Cập Nhật' : 'Tạo Bài Viết' }}
                </el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: 'Blog',
    data() {
        return {
            posts: [],
            showCreateDialog: false,
            editingPost: null,
            postForm: {
                title: '',
                slug: '',
                category: '',
                status: 'draft',
                metaDescription: '',
                keywords: '',
                content: '',
                featuredImage: ''
            },
            currentSEOScore: 0
        }
    },
    mounted() {
        this.loadPosts()
    },
    methods: {
        loadPosts() {
            // Mock data
            this.posts = [
                {
                    id: 1,
                    title: 'Hướng dẫn kiếm tiền với Shopee Affiliate 2026',
                    slug: 'huong-dan-kiem-tien-shopee-affiliate-2026',
                    category: 'Hướng Dẫn',
                    status: 'published',
                    metaDescription: 'Hướng dẫn chi tiết cách kiếm tiền với Shopee Affiliate, từ đăng ký đến tối ưu hoa hồng. Phù hợp cho người mới bắt đầu.',
                    keywords: 'shopee affiliate, kiếm tiền online, hoa hồng shopee',
                    views: 1250,
                    seoScore: 85,
                    createdAt: '2026-01-25'
                },
                {
                    id: 2,
                    title: '10 Mẹo tăng tỷ lệ chuyển đổi affiliate',
                    slug: '10-meo-tang-ty-le-chuyen-doi-affiliate',
                    category: 'Mẹo Hay',
                    status: 'published',
                    metaDescription: 'Khám phá 10 mẹo hiệu quả giúp tăng tỷ lệ chuyển đổi và doanh thu từ affiliate marketing.',
                    keywords: 'affiliate marketing, tỷ lệ chuyển đổi, mẹo affiliate',
                    views: 890,
                    seoScore: 78,
                    createdAt: '2026-01-28'
                },
                {
                    id: 3,
                    title: 'Cách tối ưu link affiliate cho SEO',
                    slug: 'cach-toi-uu-link-affiliate-cho-seo',
                    category: 'Hướng Dẫn',
                    status: 'draft',
                    metaDescription: 'Tìm hiểu cách tối ưu link affiliate để tăng thứ hạng trên Google và thu hút nhiều khách hàng hơn.',
                    keywords: 'seo, link affiliate, tối ưu seo',
                    views: 0,
                    seoScore: 65,
                    createdAt: '2026-01-30'
                }
            ]
        },
        getSEOColor(score) {
            if (score >= 80) return '#67c23a'
            if (score >= 60) return '#e6a23c'
            return '#f56c6c'
        },
        updateSEOScore() {
            let score = 0

            // Title (30 points)
            if (this.postForm.title.length >= 50 && this.postForm.title.length <= 60) {
                score += 30
            } else if (this.postForm.title.length > 0) {
                score += 15
            }

            // Meta Description (30 points)
            if (this.postForm.metaDescription.length >= 150 && this.postForm.metaDescription.length <= 160) {
                score += 30
            } else if (this.postForm.metaDescription.length > 0) {
                score += 15
            }

            // Keywords (20 points)
            if (this.postForm.keywords) {
                score += 20
            }

            // Content (10 points)
            if (this.postForm.content.length > 300) {
                score += 10
            }

            // Featured Image (10 points)
            if (this.postForm.featuredImage) {
                score += 10
            }

            this.currentSEOScore = score
        },
        viewPost(post) {
            this.$message.info(`Xem bài: ${post.title}`)
        },
        editPost(post) {
            this.editingPost = post
            this.postForm = { ...post }
            this.updateSEOScore()
            this.showCreateDialog = true
        },
        deletePost(post) {
            this.$confirm('Bạn có chắc muốn xóa bài viết này?', 'Xác nhận', {
                confirmButtonText: 'Xóa',
                cancelButtonText: 'Hủy',
                type: 'warning'
            }).then(() => {
                this.posts = this.posts.filter(p => p.id !== post.id)
                this.$message.success('Đã xóa bài viết')
            }).catch(() => { })
        },
        savePost() {
            if (!this.postForm.title || !this.postForm.category) {
                this.$message.warning('Vui lòng điền đầy đủ thông tin')
                return
            }

            if (this.editingPost) {
                // Update
                const index = this.posts.findIndex(p => p.id === this.editingPost.id)
                this.posts[index] = {
                    ...this.editingPost,
                    ...this.postForm,
                    seoScore: this.currentSEOScore
                }
                this.$message.success('Đã cập nhật bài viết')
            } else {
                // Create
                this.posts.unshift({
                    id: Date.now(),
                    ...this.postForm,
                    views: 0,
                    seoScore: this.currentSEOScore,
                    createdAt: new Date().toISOString().split('T')[0]
                })
                this.$message.success('Đã tạo bài viết mới')
            }

            this.showCreateDialog = false
            this.resetForm()
        },
        resetForm() {
            this.editingPost = null
            this.postForm = {
                title: '',
                slug: '',
                category: '',
                status: 'draft',
                metaDescription: '',
                keywords: '',
                content: '',
                featuredImage: ''
            }
            this.currentSEOScore = 0
        }
    }
}
</script>

<style scoped>
.blog-page {
    min-height: 100vh;
    background: #f5f5f5;
}

.action-bar {
    display: flex;
    gap: 12px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

/* Post Title Cell */
.post-title-cell h4 {
    margin: 0 0 8px 0;
    color: #333;
    font-size: 15px;
}

.post-meta {
    display: flex;
    align-items: center;
    gap: 12px;
}

.meta-item {
    font-size: 12px;
    color: #757575;
}

.meta-item i {
    margin-right: 4px;
}

.date-text {
    font-size: 13px;
    color: #666;
}

/* SEO Hints */
.seo-hint {
    margin-top: 4px;
    font-size: 12px;
    color: #f56c6c;
}

.seo-hint.good {
    color: #67c23a;
}

.seo-hint i {
    margin-right: 4px;
}

/* Tips */
.tip-item {
    text-align: center;
    padding: 20px;
    background: #f9f9f9;
    border-radius: 8px;
}

.tip-item i {
    font-size: 32px;
    color: #26aa99;
    margin-bottom: 12px;
}

.tip-item h4 {
    margin: 0 0 8px 0;
    color: #333;
}

.tip-item p {
    margin: 0;
    font-size: 13px;
    color: #757575;
    line-height: 1.6;
}

/* Responsive */
@media (max-width: 768px) {
    .action-bar {
        flex-direction: column;
    }

    .action-bar .el-button {
        width: 100%;
    }
}
</style>
