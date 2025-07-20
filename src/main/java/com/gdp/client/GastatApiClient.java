package com.gdp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "gastat-api", url = "${gastat.api.url}")
public interface GastatApiClient {

    @GetMapping("/v1/stats/DPV_NA_ISIC_EFNA0101")
    Map<String, Object> getGdpByEconomicActivities(
            @RequestParam("dimensions[]") String[] dimensions,
            @RequestParam("YEAR_TIME") String year,
            @RequestParam("format") String format
    );

    @GetMapping("/v1/stats/DPV_NA_EXP_EFNA0101")
    Map<String, Object> getGdpByExpenditure(
            @RequestParam("dimensions[]") String[] dimensions,
            @RequestParam("YEAR_TIME") String year,
            @RequestParam("format") String format
    );

    // Add more endpoints as needed
}