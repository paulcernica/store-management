package com.interviewing.storemanagement.service;

import static org.h2.util.StringUtils.isNullOrEmpty;
import com.interviewing.storemanagement.dto.ProductDto;
import com.interviewing.storemanagement.repository.ProductRepository;
import com.interviewing.storemanagement.util.GenericSpecification;
import com.interviewing.storemanagement.util.ProductConverter;
import com.interviewing.storemanagement.util.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ProductService implements ProductConverter, GenericService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<ProductDto> save(ProductDto productDto) {
        return ResponseEntity.ok(toDto(productRepository.save(fromDto(productDto))));
    }

    public ResponseEntity<ProductDto> getOneEntryById(Integer id) {
        return ResponseEntity.ok(toDto(productRepository.findById(id).orElse(null)));
    }

    public ResponseEntity<ProductDto> delete(Integer id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> update(ProductDto productDto, Integer id) {
        if (productRepository.findById(id).orElse(null) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDto(productRepository.save(fromDto(productDto))));
    }

    public ResponseEntity<List<ProductDto>> search(
            String name, String description, int end, int start, String order, String sort) {

        LinkedHashMap<String, GenericSpecification> params = new LinkedHashMap<>();
        if (!isNullOrEmpty(name)) {
            params.put("name", new GenericSpecification(
                    new SearchCriteria("name", ":", "name")));
        }

        if (!isNullOrEmpty(description)) {
            params.put("description", new GenericSpecification(
                    new SearchCriteria("description", ":", "description")));
        }
        List<ProductDto> productDtoList = productRepository
                .findAll(withSpecification(params), withPage(end,start, order, sort)).getContent();
        return withHeader(productDtoList);
    }
}
