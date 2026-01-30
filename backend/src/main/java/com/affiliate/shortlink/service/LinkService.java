package com.affiliate.shortlink.service;

import com.affiliate.shortlink.model.dto.request.CreateLinkRequest;
import com.affiliate.shortlink.model.dto.response.LinkResponse;
import com.affiliate.shortlink.model.entity.AffiliateLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for link operations
 */
public interface LinkService {

    /**
     * Create a new short link
     */
    LinkResponse createLink(CreateLinkRequest request);

    /**
     * Get all links for current user
     */
    Page<LinkResponse> getLinks(Pageable pageable);

    /**
     * Get link by ID
     */
    LinkResponse getLinkById(Long id);

    /**
     * Get link by short code
     */
    AffiliateLink getLinkByShortCode(String shortCode);

    /**
     * Delete link
     */
    void deleteLink(Long id);

    /**
     * Track click on link
     */
    void trackClick(String shortCode, String ipAddress, String userAgent, String referer);
}
