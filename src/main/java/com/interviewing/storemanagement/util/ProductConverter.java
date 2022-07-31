package com.interviewing.storemanagement.util;

import com.interviewing.storemanagement.dto.ProductDto;
import com.interviewing.storemanagement.entities.Product;

public interface ProductConverter {

    default Product fromDto(ProductDto dto) {
        return Product.builder().id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantity(dto.getQuantity()).build();
    }

    default ProductDto toDto(Product product) {
        return ProductDto.builder().id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity()).build();
    }
}
