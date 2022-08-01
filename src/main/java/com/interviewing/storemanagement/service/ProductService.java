package com.interviewing.storemanagement.service;

import static org.h2.util.StringUtils.isNullOrEmpty;
import com.interviewing.storemanagement.dto.ProductDto;
import com.interviewing.storemanagement.repository.ProductRepository;
import com.interviewing.storemanagement.util.GenericSpecification;
import com.interviewing.storemanagement.util.ProductConverter;
import com.interviewing.storemanagement.util.SearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
@Slf4j
public class ProductService implements ProductConverter, GenericService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<?> save(ProductDto productDto) {
        log.debug("Starting inserting a new product with name {}.", productDto.getName());
        try {
            return ResponseEntity.ok(toDto(productRepository.save(fromDto(productDto))));
        } catch (HibernateException e) {
            log.error("Failed to save entity with cause", e);
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    public ResponseEntity<ProductDto> getOneEntryById(Integer id) {
        try {
            return ResponseEntity.ok(toDto(
                    productRepository.findById(id).orElseThrow(IllegalArgumentException::new)));
        }  catch (IllegalArgumentException e) {
            log.error("Failed to fetch entity with cause ", e);
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<ProductDto> delete(Integer id) {
        log.debug("Delete product with the id {}", id);
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Failed to delete entity with cause ", e);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> update(ProductDto productDto, Integer id) {
        log.debug("Update product details with the id {}", id);
        if (productRepository.findById(id).orElse(null) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.ok(toDto(productRepository.save(fromDto(productDto))));
        } catch (HibernateException e) {
            log.error("Failed to save entity with cause", e);
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    public ResponseEntity<List<ProductDto>> search(
            String name, String description, int end, int start, String order, String sort) {

        log.debug("Start searching for products");

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
