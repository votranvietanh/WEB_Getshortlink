package com.affiliate.shortlink.controller;

import com.affiliate.shortlink.model.dto.request.CreateLinkRequest;
import com.affiliate.shortlink.model.dto.response.ApiResponse;
import com.affiliate.shortlink.model.dto.response.LinkResponse;
import com.affiliate.shortlink.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Link Management Controller
 */
@RestController
@RequestMapping("/api/v1/links")
@Api(tags = "Link Management")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @PostMapping
    @ApiOperation("Create a new short link")
    public ResponseEntity<ApiResponse<LinkResponse>> createLink(@Valid @RequestBody CreateLinkRequest request) {
        try {
            LinkResponse response = linkService.createLink(request);
            return ResponseEntity.ok(ApiResponse.success("Link created successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @ApiOperation("Get all links for current user")
    public ResponseEntity<ApiResponse<Page<LinkResponse>>> getLinks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("ASC")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<LinkResponse> links = linkService.getLinks(pageable);
            return ResponseEntity.ok(ApiResponse.success(links));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("Get link by ID")
    public ResponseEntity<ApiResponse<LinkResponse>> getLinkById(@PathVariable Long id) {
        try {
            LinkResponse response = linkService.getLinkById(id);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete link")
    public ResponseEntity<ApiResponse<Void>> deleteLink(@PathVariable Long id) {
        try {
            linkService.deleteLink(id);
            return ResponseEntity.ok(ApiResponse.success("Link deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
