package com.interviewing.storemanagement.util;

import com.interviewing.storemanagement.dto.ProductDto;
import com.interviewing.storemanagement.entities.Product;
import lombok.extern.slf4j.Slf4j;

public interface ProductConverter {

    default Product fromDto(ProductDto dto) {
        LogHolder.log.debug("Begin conversion from DTO to Product for {}", dto.getName());
        return Product.builder().id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantity(dto.getQuantity()).build();
    }

    default ProductDto toDto(Product product) {
        LogHolder.log.debug("Begin conversion from Product to DTO for {}", product.getName());
        return ProductDto.builder().id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity()).build();
    }

    @Slf4j
    final class LogHolder {
    }
}
