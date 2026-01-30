package com.affiliate.shortlink.service;

import com.affiliate.shortlink.model.dto.request.LoginRequest;
import com.affiliate.shortlink.model.dto.request.RegisterRequest;
import com.affiliate.shortlink.model.dto.response.AuthResponse;
import com.affiliate.shortlink.model.entity.User;

/**
 * Service interface for authentication operations
 */
public interface AuthService {

    /**
     * Register a new user
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Login user
     */
    AuthResponse login(LoginRequest request);

    /**
     * Get current authenticated user
     */
    User getCurrentUser();
}
