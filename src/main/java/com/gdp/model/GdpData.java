package com.gdp.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GdpData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer year;
    private String quarter;
    private BigDecimal value;
    private String category;
    private String categoryCodeArabic;
    private String categoryCodeEnglish;
    private String dataSource;
    private String indicator;
    private LocalDateTime importedAt;
}