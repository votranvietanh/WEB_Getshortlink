package com.affiliate.shortlink.model.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO for creating affiliate link request
 */
public class CreateLinkRequest {

    @NotBlank(message = "Original URL is required")
    private String originalUrl;

    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private String shopeeProductId;
    
    private String shopeeProductName;
    
    private String shopeeShopId;

    // Custom short code (optional)
    @Size(min = 4, max = 20, message = "Custom short code must be between 4 and 20 characters")
    private String customShortCode;

    // Constructors
    public CreateLinkRequest() {
    }

    public CreateLinkRequest(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    // Getters and Setters
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

    public String getShopeeShopId() {
        return shopeeShopId;
    }

    public void setShopeeShopId(String shopeeShopId) {
        this.shopeeShopId = shopeeShopId;
    }

    public String getCustomShortCode() {
        return customShortCode;
    }

    public void setCustomShortCode(String customShortCode) {
        this.customShortCode = customShortCode;
    }
}
