package com.gdp.repository;

import com.gdp.model.GdpData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GdpDataRepository extends JpaRepository<GdpData, Long> {
    List<GdpData> findByYear(Integer year);
    List<GdpData> findByCategory(String category);
    List<GdpData> findByCategoryAndYear(String category, Integer year);

    @Query("SELECT g.year, SUM(g.value) FROM GdpData g WHERE g.category = ?1 GROUP BY g.year ORDER BY g.year")
    List<Object[]> getYearlyTotalsByCategory(String category);

    @Query("SELECT g.year, g.category, SUM(g.value) FROM GdpData g WHERE g.indicator = ?1 GROUP BY g.year, g.category ORDER BY g.year")
    List<Object[]> getYearlyCategoryBreakdown(String indicator);

    @Query("SELECT MIN(g.year), MAX(g.year) FROM GdpData g")
    Object[] getYearRange();
}