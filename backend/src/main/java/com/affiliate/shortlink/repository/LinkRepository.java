package com.affiliate.shortlink.repository;

import com.affiliate.shortlink.model.entity.AffiliateLink;
import com.affiliate.shortlink.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AffiliateLink entity
 */
@Repository
public interface LinkRepository extends JpaRepository<AffiliateLink, Long> {

    /**
     * Find link by short code
     */
    Optional<AffiliateLink> findByShortCode(String shortCode);

    /**
     * Find all links by user
     */
    Page<AffiliateLink> findByUser(User user, Pageable pageable);

    /**
     * Find all active links by user
     */
    Page<AffiliateLink> findByUserAndIsActiveTrue(User user, Pageable pageable);

    /**
     * Find links by Shopee product ID
     */
    List<AffiliateLink> findByShopeeProductId(String shopeeProductId);

    /**
     * Check if short code exists
     */
    boolean existsByShortCode(String shortCode);

    /**
     * Count links by user
     */
    long countByUser(User user);

    /**
     * Get top links by click count
     */
    @Query("SELECT l FROM AffiliateLink l WHERE l.user = :user ORDER BY l.clickCount DESC")
    List<AffiliateLink> findTopLinksByUser(@Param("user") User user, Pageable pageable);

    /**
     * Search links by title or description
     */
    @Query("SELECT l FROM AffiliateLink l WHERE l.user = :user AND " +
           "(LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<AffiliateLink> searchLinks(@Param("user") User user, 
                                     @Param("keyword") String keyword, 
                                     Pageable pageable);
}
