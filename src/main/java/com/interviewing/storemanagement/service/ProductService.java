package com.interviewing.storemanagement.service;

import com.interviewing.storemanagement.dto.ProductDto;
import com.interviewing.storemanagement.repository.ProductRepository;
import com.interviewing.storemanagement.util.ProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements ProductConverter, GenericService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<ProductDto> save(ProductDto productDto) {
        return ResponseEntity.ok(toDto(productRepository.save(fromDto(productDto))));
    }

    public ResponseEntity delete(Integer id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    //TODO
}
