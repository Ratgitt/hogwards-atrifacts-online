package com.ratmir.spring.hogwardsartifactsonline.sorting_pagination_demo.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class PageableUtils {

    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * Map client-provided Sort (pageable.getSort()) to entity fields using sortMap.
     * If resulting Sort is empty, returns defaultSort.
     */
    private Sort normalizeSort(Sort incoming, Map<String,String> sortMap, Sort defaultSort) {
        if (incoming == null || incoming.isUnsorted()) {
            return defaultSort;
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (Sort.Order order : incoming) {
            String clientProp = order.getProperty();
            String mapped = sortMap.get(clientProp);               // map client name -> entity name
            if (mapped != null && !mapped.isBlank()) {
                orders.add(new Sort.Order(order.getDirection(), mapped));
            } // else ignore invalid property
        }
        return orders.isEmpty() ? defaultSort : Sort.by(orders);
    }

    /**
     * Return a safe Pageable: same page/size as incoming but with normalized Sort.
     */
    public Pageable normalizePageable(Pageable incoming, Map<String,String> sortMap, Sort defaultSort) {

        int safeSize = incoming.getPageSize() <= 0 ? DEFAULT_PAGE_SIZE
                : Math.min(incoming.getPageSize(), MAX_PAGE_SIZE);
        int safePage = Math.max(incoming.getPageNumber(), 0);
        Sort safeSort = normalizeSort(incoming.getSort(), sortMap, defaultSort);

        return PageRequest.of(safePage, safeSize, safeSort);
    }
}

