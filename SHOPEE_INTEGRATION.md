# Shopee GraphAPI Integration Guide

## Tổng Quan

Tài liệu này hướng dẫn cách tích hợp Shopee GraphAPI vào hệ thống Affiliate Link Shortener.

---

## 1. Đăng Ký Shopee Open Platform

### Bước 1: Tạo Tài Khoản Developer
1. Truy cập: https://open.shopee.com/
2. Đăng ký tài khoản developer
3. Xác thực email và thông tin

### Bước 2: Tạo Application
1. Vào Dashboard → My Apps
2. Click "Create New App"
3. Điền thông tin:
   - App Name: Shopee Link Shortener
   - App Type: Affiliate
   - Callback URL: https://yourdomain.com/callback
4. Submit và chờ phê duyệt

### Bước 3: Lấy API Credentials
Sau khi được phê duyệt, bạn sẽ nhận được:
- **Partner ID**: Mã định danh ứng dụng
- **Partner Key**: API key
- **Partner Secret**: Secret key

---

## 2. Cấu Hình Backend

### File: `application.yml`
```yaml
shopee:
  api:
    url: https://open-api.shopee.com
    partner-id: ${SHOPEE_PARTNER_ID}
    partner-key: ${SHOPEE_PARTNER_KEY}
    partner-secret: ${SHOPEE_PARTNER_SECRET}
    timeout: 30000
    retry-attempts: 3
  affiliate:
    id: ${SHOPEE_AFFILIATE_ID}
    commission-rate: 3.5
```

### File: `ShopeeApiConfig.java`
```java
@Configuration
@ConfigurationProperties(prefix = "shopee")
public class ShopeeApiConfig {
    
    private Api api;
    private Affiliate affiliate;
    
    @Data
    public static class Api {
        private String url;
        private String partnerId;
        private String partnerKey;
        private String partnerSecret;
        private Integer timeout;
        private Integer retryAttempts;
    }
    
    @Data
    public static class Affiliate {
        private String id;
        private Double commissionRate;
    }
    
    @Bean
    public RestTemplate shopeeRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(
            new HttpComponentsClientHttpRequestFactory()
        );
        return restTemplate;
    }
}
```

---

## 3. GraphQL Queries

### 3.1. Tìm Kiếm Sản Phẩm

```graphql
query SearchProducts($keyword: String!, $limit: Int!, $offset: Int!) {
  searchProducts(
    keyword: $keyword
    limit: $limit
    offset: $offset
    sort: SALES
  ) {
    items {
      itemId
      shopId
      name
      price
      priceMin
      priceMax
      discount
      images
      sold
      rating
      ratingCount
      shopName
      shopRating
    }
    total
    hasMore
  }
}
```

### 3.2. Chi Tiết Sản Phẩm

```graphql
query GetProduct($itemId: String!, $shopId: String!) {
  product(itemId: $itemId, shopId: $shopId) {
    itemId
    shopId
    name
    description
    price
    priceMin
    priceMax
    discount
    images
    sold
    rating
    ratingCount
    stock
    categories {
      id
      name
    }
    attributes {
      name
      value
    }
    shop {
      shopId
      name
      rating
      responseRate
      responseTime
    }
  }
}
```

### 3.3. Tạo Affiliate Link

```graphql
mutation CreateAffiliateLink($input: AffiliateLinkInput!) {
  createAffiliateLink(input: $input) {
    affiliateUrl
    shortUrl
    trackingId
    expiresAt
  }
}
```

---

## 4. Service Implementation

### File: `ShopeeApiService.java`

```java
@Service
@Slf4j
public class ShopeeApiService {
    
    @Autowired
    private ShopeeApiConfig config;
    
    @Autowired
    private RestTemplate shopeeRestTemplate;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String CACHE_PREFIX = "shopee:product:";
    private static final Duration CACHE_TTL = Duration.ofHours(6);
    
    /**
     * Search products by keyword
     */
    public SearchProductsResponse searchProducts(String keyword, int limit, int offset) {
        String query = buildSearchQuery();
        Map<String, Object> variables = Map.of(
            "keyword", keyword,
            "limit", limit,
            "offset", offset
        );
        
        GraphQLRequest request = new GraphQLRequest(query, variables);
        String signature = generateSignature(request);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + config.getApi().getPartnerKey());
        headers.set("X-Shopee-Signature", signature);
        
        HttpEntity<GraphQLRequest> entity = new HttpEntity<>(request, headers);
        
        try {
            ResponseEntity<SearchProductsResponse> response = shopeeRestTemplate.exchange(
                config.getApi().getUrl() + "/graphql",
                HttpMethod.POST,
                entity,
                SearchProductsResponse.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            log.error("Error searching products: {}", e.getMessage());
            throw new ShopeeApiException("Failed to search products", e);
        }
    }
    
    /**
     * Get product details with caching
     */
    public ProductDTO getProduct(String itemId, String shopId) {
        String cacheKey = CACHE_PREFIX + itemId;
        
        // Try cache first
        ProductDTO cached = (ProductDTO) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.info("Product {} found in cache", itemId);
            return cached;
        }
        
        // Fetch from API
        String query = buildProductQuery();
        Map<String, Object> variables = Map.of(
            "itemId", itemId,
            "shopId", shopId
        );
        
        GraphQLRequest request = new GraphQLRequest(query, variables);
        ProductDTO product = executeGraphQLRequest(request, ProductDTO.class);
        
        // Cache result
        redisTemplate.opsForValue().set(cacheKey, product, CACHE_TTL);
        
        return product;
    }
    
    /**
     * Create affiliate link
     */
    public String createAffiliateLink(String productUrl) {
        // Extract product ID and shop ID from URL
        ProductUrlInfo urlInfo = parseProductUrl(productUrl);
        
        // Build affiliate URL
        String affiliateUrl = String.format(
            "%s?af_siteid=%s&af_click_lookback=7d&af_viewthrough_lookback=1d",
            productUrl,
            config.getAffiliate().getId()
        );
        
        return affiliateUrl;
    }
    
    /**
     * Generate HMAC signature for API request
     */
    private String generateSignature(GraphQLRequest request) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            String baseString = config.getApi().getPartnerId() + 
                               timestamp + 
                               request.toString();
            
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                config.getApi().getPartnerSecret().getBytes(),
                "HmacSHA256"
            );
            sha256HMAC.init(secretKey);
            
            byte[] hash = sha256HMAC.doFinal(baseString.getBytes());
            return Hex.encodeHexString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }
    
    /**
     * Parse product URL to extract IDs
     */
    private ProductUrlInfo parseProductUrl(String url) {
        // Example: https://shopee.vn/product/123456/789012
        Pattern pattern = Pattern.compile(".*/product/(\\d+)/(\\d+)");
        Matcher matcher = pattern.matcher(url);
        
        if (matcher.find()) {
            return new ProductUrlInfo(
                matcher.group(1), // shopId
                matcher.group(2)  // itemId
            );
        }
        
        throw new IllegalArgumentException("Invalid Shopee product URL");
    }
    
    private String buildSearchQuery() {
        return """
            query SearchProducts($keyword: String!, $limit: Int!, $offset: Int!) {
              searchProducts(keyword: $keyword, limit: $limit, offset: $offset) {
                items {
                  itemId
                  shopId
                  name
                  price
                  discount
                  images
                  sold
                  rating
                }
                total
              }
            }
            """;
    }
    
    private String buildProductQuery() {
        return """
            query GetProduct($itemId: String!, $shopId: String!) {
              product(itemId: $itemId, shopId: $shopId) {
                itemId
                shopId
                name
                description
                price
                discount
                images
                sold
                rating
                stock
              }
            }
            """;
    }
}
```

### DTOs

```java
@Data
public class SearchProductsResponse {
    private SearchProductsData data;
    
    @Data
    public static class SearchProductsData {
        private SearchProducts searchProducts;
    }
    
    @Data
    public static class SearchProducts {
        private List<ProductItem> items;
        private Long total;
        private Boolean hasMore;
    }
    
    @Data
    public static class ProductItem {
        private String itemId;
        private String shopId;
        private String name;
        private Double price;
        private Double discount;
        private List<String> images;
        private Long sold;
        private Double rating;
    }
}

@Data
public class ProductDTO {
    private String itemId;
    private String shopId;
    private String name;
    private String description;
    private Double price;
    private Double priceMin;
    private Double priceMax;
    private Double discount;
    private List<String> images;
    private Long sold;
    private Double rating;
    private Long stock;
    private ShopInfo shop;
    
    @Data
    public static class ShopInfo {
        private String shopId;
        private String name;
        private Double rating;
    }
}
```

---

## 5. Controller Implementation

```java
@RestController
@RequestMapping("/api/v1/shopee")
@Slf4j
public class ShopeeController {
    
    @Autowired
    private ShopeeApiService shopeeApiService;
    
    @GetMapping("/products/search")
    public ResponseEntity<ApiResponse> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        
        log.info("Searching products: keyword={}, limit={}, offset={}", 
                 keyword, limit, offset);
        
        SearchProductsResponse result = shopeeApiService.searchProducts(
            keyword, limit, offset
        );
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @GetMapping("/products/{itemId}")
    public ResponseEntity<ApiResponse> getProduct(
            @PathVariable String itemId,
            @RequestParam String shopId) {
        
        log.info("Getting product: itemId={}, shopId={}", itemId, shopId);
        
        ProductDTO product = shopeeApiService.getProduct(itemId, shopId);
        
        return ResponseEntity.ok(ApiResponse.success(product));
    }
    
    @PostMapping("/affiliate/create")
    public ResponseEntity<ApiResponse> createAffiliateLink(
            @RequestBody CreateAffiliateLinkRequest request) {
        
        log.info("Creating affiliate link for: {}", request.getProductUrl());
        
        String affiliateUrl = shopeeApiService.createAffiliateLink(
            request.getProductUrl()
        );
        
        Map<String, String> response = Map.of(
            "affiliateUrl", affiliateUrl,
            "originalUrl", request.getProductUrl()
        );
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
```

---

## 6. Error Handling

```java
@ControllerAdvice
public class ShopeeApiExceptionHandler {
    
    @ExceptionHandler(ShopeeApiException.class)
    public ResponseEntity<ApiResponse> handleShopeeApiException(
            ShopeeApiException ex) {
        
        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(ApiResponse.error(
                "SHOPEE_API_ERROR",
                ex.getMessage()
            ));
    }
    
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponse> handleRateLimitException(
            RateLimitExceededException ex) {
        
        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .body(ApiResponse.error(
                "RATE_LIMIT_EXCEEDED",
                "Too many requests to Shopee API"
            ));
    }
}
```

---

## 7. Testing

```java
@SpringBootTest
class ShopeeApiServiceTest {
    
    @Autowired
    private ShopeeApiService shopeeApiService;
    
    @Test
    void testSearchProducts() {
        SearchProductsResponse response = shopeeApiService.searchProducts(
            "iphone", 10, 0
        );
        
        assertNotNull(response);
        assertNotNull(response.getData());
        assertTrue(response.getData().getSearchProducts().getTotal() > 0);
    }
    
    @Test
    void testGetProduct() {
        ProductDTO product = shopeeApiService.getProduct(
            "123456", "789012"
        );
        
        assertNotNull(product);
        assertEquals("123456", product.getItemId());
    }
    
    @Test
    void testCreateAffiliateLink() {
        String url = "https://shopee.vn/product/123456/789012";
        String affiliateUrl = shopeeApiService.createAffiliateLink(url);
        
        assertNotNull(affiliateUrl);
        assertTrue(affiliateUrl.contains("af_siteid="));
    }
}
```

---

## 8. Rate Limiting

Shopee API có giới hạn:
- **Free Tier**: 100 requests/phút
- **Standard**: 1000 requests/phút
- **Premium**: 5000 requests/phút

Implement rate limiting:

```java
@Component
public class ShopeeRateLimiter {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    private static final String RATE_LIMIT_KEY = "shopee:ratelimit";
    private static final int MAX_REQUESTS = 100;
    private static final Duration WINDOW = Duration.ofMinutes(1);
    
    public boolean allowRequest() {
        String key = RATE_LIMIT_KEY + ":" + 
                     (System.currentTimeMillis() / 60000);
        
        Long count = redisTemplate.opsForValue().increment(key);
        
        if (count == 1) {
            redisTemplate.expire(key, WINDOW);
        }
        
        return count <= MAX_REQUESTS;
    }
}
```

---

## 9. Best Practices

1. **Caching**: Cache product data để giảm API calls
2. **Retry Logic**: Implement retry với exponential backoff
3. **Error Handling**: Xử lý lỗi gracefully
4. **Rate Limiting**: Tuân thủ rate limits
5. **Logging**: Log tất cả API calls
6. **Monitoring**: Monitor API usage và errors
7. **Security**: Bảo mật API credentials

---

## 10. Tài Liệu Tham Khảo

- Shopee Open Platform: https://open.shopee.com/
- API Documentation: https://open.shopee.com/documents
- GraphQL Guide: https://graphql.org/learn/
- Affiliate Program: https://shopee.vn/affiliate
