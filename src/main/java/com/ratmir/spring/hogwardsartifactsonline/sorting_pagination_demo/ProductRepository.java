package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo;

import com.querydsl.core.types.Predicate;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.querydsl.QPredicates;
import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto.ProductFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import static com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.QProduct.product;

@Repository
public interface ProductRepository extends
        JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product>,
        FilterProductRepository,
        QuerydslPredicateExecutor<Product> {

    static Predicate buildProductFilter(ProductFilter filter) {
        return QPredicates.builder()
                .add(filter.getName(), product.name::containsIgnoreCase)
                .add(filter.getCategory(), product.category::in)
                .add(filter.getMinPrice(), product.price::goe)
                .add(filter.getMaxPrice(), product.price::loe)
                .add(filter.getAvailable(), product.available::eq)
                .add(filter.getMinRating(), product.rating::goe)
                .add(filter.getCreatedAfter(), product.createdAt::goe)
                .add(filter.getCreatedBefore(), product.createdAt::loe)
                .build();
    }
}