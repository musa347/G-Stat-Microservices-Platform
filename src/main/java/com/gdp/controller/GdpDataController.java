package com.gdp.controller;

import com.gdp.model.GdpData;
import com.gdp.repository.GdpDataRepository;
import com.gdp.service.DataIngestionService;
import com.gdp.service.GdpAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gdp")
public class GdpDataController {

    private final GdpDataRepository repository;
    private final GdpAnalysisService analysisService;
    private final DataIngestionService ingestionService;


    public GdpDataController(GdpDataRepository repository, GdpAnalysisService analysisService, DataIngestionService ingestionService) {
        this.repository = repository;
        this.analysisService = analysisService;
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest")
    @PreAuthorize("hasRole('ADMIN')")
    public String triggerIngestion() {
        ingestionService.fetchAndStoreGdpData();
        return "Ingestion triggered";
    }

    @GetMapping
    public List<GdpData> getAllData(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String category
    ) {
        if (year != null && category != null) {
            return repository.findByCategoryAndYear(category, year);
        } else if (year != null) {
            return repository.findByYear(year);
        } else if (category != null) {
            return repository.findByCategory(category);
        }
        return repository.findAll();
    }

    @GetMapping("/years")
    public Map<String, Integer> getAvailableYears() {
        Object[] range = repository.getYearRange();
        return Map.of(
                "minYear", (Integer) range[0],
                "maxYear", (Integer) range[1]
        );
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return analysisService.getUniqueCategories();
    }

    @GetMapping("/analysis/yearly-totals")
    public Map<Integer, BigDecimal> getYearlyTotals(
            @RequestParam String category
    ) {
        return analysisService.getYearlyTotalsByCategory(category);
    }

    @GetMapping("/analysis/category-breakdown")
    public Map<Integer, Map<String, BigDecimal>> getCategoryBreakdownByYear(
            @RequestParam String indicator
    ) {
        return analysisService.getCategoryBreakdownByYear(indicator);
    }

    @GetMapping("/analysis/growth-rates")
    public Map<Integer, Double> getYearlyGrowthRates(
            @RequestParam String category
    ) {
        return analysisService.calculateYearlyGrowthRates(category);
    }
}