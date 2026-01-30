package com.affiliate.shortlink.repository;

import com.affiliate.shortlink.model.entity.AffiliateLink;
import com.affiliate.shortlink.model.entity.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Repository interface for Click entity
 */
@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {

    /**
     * Count clicks by link
     */
    long countByLink(AffiliateLink link);

    /**
     * Count clicks by link and date range
     */
    long countByLinkAndClickedAtBetween(AffiliateLink link, 
                                        LocalDateTime startDate, 
                                        LocalDateTime endDate);

    /**
     * Get clicks by date for analytics
     */
    @Query("SELECT DATE(c.clickedAt) as date, COUNT(c) as count " +
           "FROM Click c WHERE c.link = :link " +
           "AND c.clickedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(c.clickedAt) " +
           "ORDER BY DATE(c.clickedAt)")
    List<Object[]> getClicksByDate(@Param("link") AffiliateLink link,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    /**
     * Get clicks by device type
     */
    @Query("SELECT c.deviceType, COUNT(c) FROM Click c " +
           "WHERE c.link = :link GROUP BY c.deviceType")
    List<Object[]> getClicksByDeviceType(@Param("link") AffiliateLink link);

    /**
     * Get clicks by country
     */
    @Query("SELECT c.country, COUNT(c) FROM Click c " +
           "WHERE c.link = :link AND c.country IS NOT NULL " +
           "GROUP BY c.country ORDER BY COUNT(c) DESC")
    List<Object[]> getClicksByCountry(@Param("link") AffiliateLink link);

    /**
     * Get recent clicks
     */
    List<Click> findTop10ByLinkOrderByClickedAtDesc(AffiliateLink link);
}
