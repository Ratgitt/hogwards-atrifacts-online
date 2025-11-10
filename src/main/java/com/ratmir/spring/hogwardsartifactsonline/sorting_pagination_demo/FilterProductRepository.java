package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo;

import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto.ProductFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FilterProductRepository {

    List<Product> findAllByFilterQueryDsl(ProductFilter filter);
}
