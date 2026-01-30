package com.affiliate.shortlink.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Click Entity - Tracks individual clicks on affiliate links
 */
@Entity
@Table(name = "clicks", indexes = {
    @Index(name = "idx_link_id", columnList = "link_id"),
    @Index(name = "idx_clicked_at", columnList = "clicked_at"),
    @Index(name = "idx_ip_address", columnList = "ip_address")
})
public class Click {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id", nullable = false)
    private AffiliateLink link;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "referer", columnDefinition = "TEXT")
    private String referer;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(name = "browser", length = 100)
    private String browser;

    @Column(name = "os", length = 100)
    private String os;

    @Column(name = "clicked_at", nullable = false)
    private LocalDateTime clickedAt;

    @PrePersist
    protected void onCreate() {
        clickedAt = LocalDateTime.now();
    }

    // Constructors
    public Click() {
    }

    public Click(AffiliateLink link, String ipAddress) {
        this.link = link;
        this.ipAddress = ipAddress;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AffiliateLink getLink() {
        return link;
    }

    public void setLink(AffiliateLink link) {
        this.link = link;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public LocalDateTime getClickedAt() {
        return clickedAt;
    }

    public void setClickedAt(LocalDateTime clickedAt) {
        this.clickedAt = clickedAt;
    }
}
