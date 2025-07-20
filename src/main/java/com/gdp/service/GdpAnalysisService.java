package com.gdp.service;

import com.gdp.repository.GdpDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gdp.model.GdpData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GdpAnalysisService {

    private final GdpDataRepository repository;

    public List<String> getUniqueCategories() {
        return repository.findAll().stream()
                .map(GdpData::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    public Map<Integer, BigDecimal> getYearlyTotalsByCategory(String category) {
        List<Object[]> results = repository.getYearlyTotalsByCategory(category);
        Map<Integer, BigDecimal> yearlyTotals = new LinkedHashMap<>();

        for (Object[] result : results) {
            yearlyTotals.put((Integer) result[0], (BigDecimal) result[1]);
        }

        return yearlyTotals;
    }

    public Map<Integer, Map<String, BigDecimal>> getCategoryBreakdownByYear(String indicator) {
        List<Object[]> results = repository.getYearlyCategoryBreakdown(indicator);
        Map<Integer, Map<String, BigDecimal>> breakdown = new TreeMap<>();

        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            String category = (String) result[1];
            BigDecimal value = (BigDecimal) result[2];

            breakdown.computeIfAbsent(year, k -> new HashMap<>())
                    .put(category, value);
        }

        return breakdown;
    }

    public Map<Integer, Double> calculateYearlyGrowthRates(String category) {
        Map<Integer, BigDecimal> yearlyTotals = getYearlyTotalsByCategory(category);
        Map<Integer, Double> growthRates = new TreeMap<>();

        Integer previousYear = null;
        BigDecimal previousValue = null;

        for (Map.Entry<Integer, BigDecimal> entry : yearlyTotals.entrySet()) {
            Integer year = entry.getKey();
            BigDecimal value = entry.getValue();

            if (previousYear != null) {
                double growthRate = value.subtract(previousValue)
                        .divide(previousValue, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();

                growthRates.put(year, growthRate);
            }

            previousYear = year;
            previousValue = value;
        }

        return growthRates;
    }
}