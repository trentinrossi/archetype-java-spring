package com.example.demo.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Map invalid/legacy field names to valid entity field names
    private static final Map<String, String> FIELD_NAME_MAPPINGS = new HashMap<>();
    
    static {
        // Transaction entity field mappings
        FIELD_NAME_MAPPINGS.put("processingTimestamp", "createdAt");
        FIELD_NAME_MAPPINGS.put("timestamp", "createdAt");
        FIELD_NAME_MAPPINGS.put("processing_timestamp", "createdAt");
        FIELD_NAME_MAPPINGS.put("originationTimestamp", "createdAt");
        FIELD_NAME_MAPPINGS.put("origination_timestamp", "createdAt");
        
        // Add more mappings as needed for other entities
        FIELD_NAME_MAPPINGS.put("customerId", "customer.customerId");
        FIELD_NAME_MAPPINGS.put("accountId", "card.account.accountId");
    }

    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver() {
            @Override
            @NonNull
            public Pageable resolveArgument(
                    @NonNull org.springframework.core.MethodParameter methodParameter,
                    @Nullable org.springframework.web.method.support.ModelAndViewContainer mavContainer,
                    @NonNull org.springframework.web.context.request.NativeWebRequest webRequest,
                    @Nullable org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
                
                Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
                
                // Map and validate sort parameters
                Sort mappedSort = mapSort(pageable.getSort());
                
                // Return new Pageable with mapped sort
                return PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    mappedSort
                );
            }
        };
        
        pageableResolver.setFallbackPageable(PageRequest.of(0, 20, Sort.by("createdAt").descending()));
        resolvers.add(pageableResolver);
    }
    
    private Sort mapSort(Sort sort) {
        if (sort.isUnsorted()) {
            return Sort.by("createdAt").descending();
        }
        
        List<Sort.Order> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            String property = order.getProperty();
            
            // Map the property name if it exists in the mapping
            String mappedProperty = FIELD_NAME_MAPPINGS.getOrDefault(property, property);
            
            orders.add(new Sort.Order(order.getDirection(), mappedProperty));
        }
        
        return Sort.by(orders);
    }
}
