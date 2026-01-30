package com.affiliate.shortlink.controller;

import com.affiliate.shortlink.model.dto.request.LoginRequest;
import com.affiliate.shortlink.model.dto.request.RegisterRequest;
import com.affiliate.shortlink.model.dto.response.ApiResponse;
import com.affiliate.shortlink.model.dto.response.AuthResponse;
import com.affiliate.shortlink.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Authentication Controller
 */
@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "Authentication")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ApiOperation("Register a new user")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    @ApiOperation("Login user")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/me")
    @ApiOperation("Get current user")
    public ResponseEntity<ApiResponse<Object>> getCurrentUser() {
        try {
            return ResponseEntity.ok(ApiResponse.success(authService.getCurrentUser()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
