package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo;

import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto.PageResponse;
import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto.ProductDto;
import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto.ProductFilter;
import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.validation.ValidSortParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @Validated
    @GetMapping
    public PageResponse<ProductDto> searchProducts(
            @ModelAttribute ProductFilter filter,

            @PageableDefault
            @ValidSortParams(allowed = {"id","name","category","price","available","rating","createdAt"})
            Pageable pageable
    ) {
        Page<ProductDto> products = service.findAll(filter, pageable);

        return PageResponse.<ProductDto>builder()
                .content(products.getContent())
                .page(products.getNumber())
                .size(products.getSize())
                .totalElements(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .build();
    }
}
