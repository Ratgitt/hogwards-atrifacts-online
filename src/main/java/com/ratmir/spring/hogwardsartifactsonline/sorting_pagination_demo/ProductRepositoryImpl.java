package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo;

import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto.ProductFilter;

import java.util.Collections;
import java.util.List;

import static com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.ProductRepository.buildProductFilter;

public class ProductRepositoryImpl implements FilterProductRepository {

    @Override
    public List<Product> findAllByFilterQueryDsl(ProductFilter filter) {
        var predicate = buildProductFilter(filter);
        return Collections.emptyList();
    }
}
