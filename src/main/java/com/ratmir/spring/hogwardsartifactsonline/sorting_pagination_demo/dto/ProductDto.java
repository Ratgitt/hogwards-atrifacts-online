package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private Boolean available;
    private Double rating;
    private OffsetDateTime createdAt;
}
