package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo;

import com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.dto.ProductFilter;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.QProduct.*;

public final class ProductSpecifications {

    public static Specification<Product> byFilter(ProductFilter filter) {
        Specification<Product> spec = Specification.where(null);

        if (filter == null) return spec;

        if (filter.getName() != null && !filter.getName().isBlank()) {
            spec = spec.and(nameContains(filter.getName()));
        }
        if (filter.getCategory() != null && !filter.getCategory().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("category").in(filter.getCategory()));
        }
        if (filter.getMinPrice() != null) {
            spec = spec.and((root, query, cb) -> cb.ge(root.get("price"), filter.getMinPrice()));
        }
        if (filter.getMaxPrice() != null) {
            spec = spec.and((root, query, cb) -> cb.le(root.get("price"), filter.getMaxPrice()));
        }
        if (filter.getAvailable() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("available"), filter.getAvailable()));
        }
        if (filter.getMinRating() != null) {
            spec = spec.and((root, query, cb) -> cb.ge(root.get("rating"), filter.getMinRating()));
        }
        if (filter.getCreatedAfter() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAfter()));
        }
        if (filter.getCreatedBefore() != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedBefore()));
        }

        return spec;
    }

    public static Specification<Product> nameContains(String text) {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(cb.lower(root.get("name")), "%" + text.toLowerCase() + "%");
            }
        };
    }
}
