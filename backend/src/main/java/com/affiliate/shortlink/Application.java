package com.affiliate.shortlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Application Class for Shopee Affiliate Link Shortener
 * 
 * @author Your Name
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("\n=================================================");
        System.out.println("🚀 Shopee Affiliate Link Shortener Started!");
        System.out.println("📝 Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("🗄️  H2 Console: http://localhost:8080/api/h2-console");
        System.out.println("=================================================\n");
    }
}
