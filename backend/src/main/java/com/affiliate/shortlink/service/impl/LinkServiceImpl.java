package com.affiliate.shortlink.service.impl;

import com.affiliate.shortlink.model.dto.request.CreateLinkRequest;
import com.affiliate.shortlink.model.dto.response.LinkResponse;
import com.affiliate.shortlink.model.entity.AffiliateLink;
import com.affiliate.shortlink.model.entity.Click;
import com.affiliate.shortlink.model.entity.User;
import com.affiliate.shortlink.repository.ClickRepository;
import com.affiliate.shortlink.repository.LinkRepository;
import com.affiliate.shortlink.service.AuthService;
import com.affiliate.shortlink.service.LinkService;
import com.affiliate.shortlink.util.LinkGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of LinkService
 */
@Service
@Transactional
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ClickRepository clickRepository;

    @Autowired
    private AuthService authService;

    @Value("${shortlink.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public LinkResponse createLink(CreateLinkRequest request) {
        User currentUser = authService.getCurrentUser();

        // Generate short code
        String shortCode;
        if (request.getCustomShortCode() != null && !request.getCustomShortCode().isEmpty()) {
            // Use custom short code
            if (linkRepository.existsByShortCode(request.getCustomShortCode())) {
                throw new RuntimeException("Short code already exists");
            }
            shortCode = request.getCustomShortCode();
        } else {
            // Generate random short code
            do {
                shortCode = LinkGenerator.generateShortCode();
            } while (linkRepository.existsByShortCode(shortCode));
        }

        // Create link
        AffiliateLink link = new AffiliateLink();
        link.setUser(currentUser);
        link.setShortCode(shortCode);
        link.setOriginalUrl(request.getOriginalUrl());
        link.setTitle(request.getTitle());
        link.setDescription(request.getDescription());
        link.setShopeeProductId(request.getShopeeProductId());
        link.setShopeeProductName(request.getShopeeProductName());
        link.setShopeeShopId(request.getShopeeShopId());
        link.setIsActive(true);

        link = linkRepository.save(link);

        return mapToResponse(link);
    }

    @Override
    public Page<LinkResponse> getLinks(Pageable pageable) {
        User currentUser = authService.getCurrentUser();
        Page<AffiliateLink> links = linkRepository.findByUser(currentUser, pageable);
        return links.map(this::mapToResponse);
    }

    @Override
    public LinkResponse getLinkById(Long id) {
        User currentUser = authService.getCurrentUser();
        AffiliateLink link = linkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Link not found"));

        // Check ownership
        if (!link.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied");
        }

        return mapToResponse(link);
    }

    @Override
    public AffiliateLink getLinkByShortCode(String shortCode) {
        return linkRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Link not found"));
    }

    @Override
    public void deleteLink(Long id) {
        User currentUser = authService.getCurrentUser();
        AffiliateLink link = linkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Link not found"));

        // Check ownership
        if (!link.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied");
        }

        linkRepository.delete(link);
    }

    @Override
    public void trackClick(String shortCode, String ipAddress, String userAgent, String referer) {
        AffiliateLink link = getLinkByShortCode(shortCode);

        // Increment click count
        link.incrementClickCount();
        linkRepository.save(link);

        // Create click record
        Click click = new Click();
        click.setLink(link);
        click.setIpAddress(ipAddress);
        click.setUserAgent(userAgent);
        click.setReferer(referer);

        // Parse device type from user agent (simple implementation)
        if (userAgent != null) {
            if (userAgent.toLowerCase().contains("mobile")) {
                click.setDeviceType("Mobile");
            } else if (userAgent.toLowerCase().contains("tablet")) {
                click.setDeviceType("Tablet");
            } else {
                click.setDeviceType("Desktop");
            }
        }

        clickRepository.save(click);
    }

    private LinkResponse mapToResponse(AffiliateLink link) {
        LinkResponse response = new LinkResponse();
        response.setId(link.getId());
        response.setShortCode(link.getShortCode());
        response.setShortUrl(baseUrl + "/" + link.getShortCode());
        response.setOriginalUrl(link.getOriginalUrl());
        response.setTitle(link.getTitle());
        response.setDescription(link.getDescription());
        response.setShopeeProductId(link.getShopeeProductId());
        response.setShopeeProductName(link.getShopeeProductName());
        response.setClickCount(link.getClickCount());
        response.setConversionCount(link.getConversionCount());
        response.setIsActive(link.getIsActive());
        response.setCreatedAt(link.getCreatedAt());
        response.setUpdatedAt(link.getUpdatedAt());
        return response;
    }
}
