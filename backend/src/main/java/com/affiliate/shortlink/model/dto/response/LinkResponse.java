package com.affiliate.shortlink.model.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for link response
 */
public class LinkResponse {

    private Long id;
    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private String title;
    private String description;
    private String shopeeProductId;
    private String shopeeProductName;
    private Long clickCount;
    private Long conversionCount;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public LinkResponse() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
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
