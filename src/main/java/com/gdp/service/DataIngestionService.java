package com.gdp.service;

import com.gdp.client.GastatApiClient;
import com.gdp.model.GdpData;
import com.gdp.repository.GdpDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataIngestionService {

    private final GastatApiClient apiClient;
    private final GdpDataRepository repository;

    @Scheduled(cron = "0 57 06 1 * ?", zone = "Africa/Lagos")
    public void fetchAndStoreGdpData() {
        log.info("Starting scheduled GDP data ingestion");

        // Fetch data for last 10 years
        int currentYear = LocalDateTime.now().getYear();
        for (int year = currentYear - 10; year <= currentYear; year++) {
            try {
                fetchGdpByEconomicActivities(Integer.toString(year));
                fetchGdpByExpenditure(Integer.toString(year));
                // Add more data types as needed
            } catch (Exception e) {
                log.error("Error fetching data for year {}: {}", year, e.getMessage());
            }
        }

        log.info("Completed GDP data ingestion");
    }

    public void fetchGdpByEconomicActivities(String year) {
        Map<String, Object> response = apiClient.getGdpByEconomicActivities(
                new String[]{"ISIC_NAME", "YEAR"},
                year,
                "JSON"
        );

        List<GdpData> dataList = new ArrayList<>();

        // Parse response and map to entities
        List<Map<String, Object>> values = (List<Map<String, Object>>) response.get("value");
        for (Map<String, Object> item : values) {
            GdpData data = GdpData.builder()
                    .year(Integer.parseInt(year))
                    .category("Economic Activity")
                    .categoryCodeEnglish((String) item.get("ISIC_NAME_ENGL"))
                    .categoryCodeArabic((String) item.get("ISIC_NAME_ARAB"))
                    .value(new BigDecimal(item.get("OBSVALUE_OBSV").toString()))
                    .indicator("GDP at Current Price")
                    .dataSource("DPV_NA_ISIC_EFNA0101")
                    .importedAt(LocalDateTime.now())
                    .build();

            dataList.add(data);
        }

        repository.saveAll(dataList);
        log.info("Saved {} GDP by economic activity records for year {}", dataList.size(), year);
    }

    public void fetchGdpByExpenditure(String year) {
        // Similar implementation for expenditure data
        // ...
    }
}