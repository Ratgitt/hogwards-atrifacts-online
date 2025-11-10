package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFilter {
    private String name;          // partial match
    private List<String> category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean available;
    private Double minRating;
    private OffsetDateTime createdAfter;
    private OffsetDateTime createdBefore;
}