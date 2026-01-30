package com.affiliate.shortlink.controller;

import com.affiliate.shortlink.model.entity.AffiliateLink;
import com.affiliate.shortlink.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Redirect Controller for short links
 */
@Controller
public class RedirectController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/r/{shortCode}")
    public void redirect(@PathVariable String shortCode,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            // Get link
            AffiliateLink link = linkService.getLinkByShortCode(shortCode);

            // Track click
            String ipAddress = getClientIp(request);
            String userAgent = request.getHeader("User-Agent");
            String referer = request.getHeader("Referer");

            linkService.trackClick(shortCode, ipAddress, userAgent, referer);

            // Redirect
            response.sendRedirect(link.getOriginalUrl());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Link not found");
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
