package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo;

import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto.ProductDto;
import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto.ProductFilter;
import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.mapper.ProductMapper;
import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.ProductRepository.buildProductFilter;

@Service
@RequiredArgsConstructor
public class ProductService {

    // allowed sort mapping: client field -> entity field
    private static final Map<String, String> SORT_MAP = Map.of(
            "id", "id",
            "name", "name",
            "category", "category",
            "price", "price",
            "rating", "rating",
            "createdAt", "createdAt"
    );

    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Page<ProductDto> findAll(ProductFilter filter, int page, int size, Sort sort) {

//        int pageSize = Math.min(size, MAX_PAGE_SIZE);
        int pageSize = size <= 0 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        var spec = ProductSpecifications.byFilter(filter);
        return productRepository.findAll(spec, pageable)
                .map(productMapper::entityToDto);
    }

    public Page<ProductDto> findAll(ProductFilter filter, Pageable pageable) {

//        Sort defaultSort = Sort.by(Sort.Order.desc("createdAt"));
        Sort defaultSort = Sort.by(Sort.Order.asc("id"));
        Pageable safePageable = PageableUtils.normalizePageable(pageable, SORT_MAP, defaultSort);

//        var spec = ProductSpecifications.byFilter(filter);
//        return productRepository.findAll(spec, pageable)
//                .map(this::toDto);

        var predicate = buildProductFilter(filter);
        Page<Product> page;
        if(predicate != null) {
            page = productRepository.findAll(predicate, safePageable);
        } else {
            page = productRepository.findAll(safePageable);
        }
        return page.map(productMapper::entityToDto);
    }

    // helper to build Sort from client input safely
    public Sort buildSortFromRequest(List<String> sortParams) {

        var defaultSort = new Sort.Order(Sort.Direction.ASC, "id");
        // Incoming sortParams could be like: ["name,asc", "price,desc"]
        List<Sort.Order> orders = new ArrayList<>();
        if (sortParams == null || sortParams.isEmpty()) {
            // default sort
            orders.add(defaultSort);
        } else {
            for (String s : sortParams) {
                String[] parts = s.split(",");
                String clientField = parts[0].trim();
                Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;

                String entityField = SORT_MAP.get(clientField);
                if (entityField != null) {
                    orders.add(new Sort.Order(dir, entityField));
                } // else ignore invalid fields
            }
//            if (orders.isEmpty()) {
//                orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));
//            }
            if (orders.isEmpty()) {
                orders.add(defaultSort);
            }
        }
        return Sort.by(orders);
    }
}