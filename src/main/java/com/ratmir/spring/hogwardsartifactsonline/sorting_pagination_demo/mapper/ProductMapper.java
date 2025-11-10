package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.mapper;

import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.Product;
import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto entityToDto(Product product);
}
