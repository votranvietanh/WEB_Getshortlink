package com.affiliate.shortlink.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * AffiliateLink Entity - Represents a shortened affiliate link
 */
@Entity
@Table(name = "affiliate_links", indexes = {
    @Index(name = "idx_short_code", columnList = "short_code", unique = true),
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_shopee_product_id", columnList = "shopee_product_id")
})
public class AffiliateLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "short_code", nullable = false, unique = true, length = 20)
    private String shortCode;

    @Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Column(name = "shopee_product_id", length = 100)
    private String shopeeProductId;

    @Column(name = "shopee_product_name", length = 500)
    private String shopeeProductName;

    @Column(name = "shopee_shop_id", length = 100)
    private String shopeeShopId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "click_count", nullable = false)
    private Long clickCount = 0L;

    @Column(name = "conversion_count")
    private Long conversionCount = 0L;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public AffiliateLink() {
    }

    public AffiliateLink(User user, String shortCode, String originalUrl) {
        this.user = user;
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
    }

    // Helper method to increment click count
    public void incrementClickCount() {
        this.clickCount++;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShopeeProductId() {
        return shopeeProductId;
    }

    public void setShopeeProductId(String shopeeProductId) {
        this.shopeeProductId = shopeeProductId;
    }

    public String getShopeeProductName() {
        return shopeeProductName;
    }

    public void setShopeeProductName(String shopeeProductName) {
        this.shopeeProductName = shopeeProductName;
    }

    public String getShopeeShopId() {
        return shopeeShopId;
    }

    public void setShopeeShopId(String shopeeShopId) {
        this.shopeeShopId = shopeeShopId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public Long getConversionCount() {
        return conversionCount;
    }

    public void setConversionCount(Long conversionCount) {
        this.conversionCount = conversionCount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
