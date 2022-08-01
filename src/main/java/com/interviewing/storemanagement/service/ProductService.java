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

    /**
     * Used to insert a Product in databasse.
     *
     * @param productDto DTO containing the product information
     * @return http response which include header/body/status
     */
    public ResponseEntity<?> save(ProductDto productDto) {
        log.debug("Starting inserting a new product with name {}.", productDto.getName());
        try {
            return ResponseEntity.ok(toDto(productRepository.save(fromDto(productDto))));
        } catch (HibernateException e) {
            log.error("Failed to save entity with cause", e);
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    /**
     * Used to fetch one product from database by a given id.
     *
     * @param id Database id of the product we want to fetch
     * @return http response which include header/body/status
     */
    public ResponseEntity<ProductDto> getOneEntryById(Integer id) {
        try {
            return ResponseEntity.ok(toDto(
                    productRepository.findById(id).orElseThrow(IllegalArgumentException::new)));
        }  catch (IllegalArgumentException e) {
            log.error("Failed to fetch entity with cause ", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Used to delete one product from database by a given id.
     *
     * @param id Database id of the product we want to delete
     * @return http response which include header/body/status
     */
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

    /**
     * Used to update a product from database by a given id.
     * @param productDto DTO containing the product new informations we want to update
     * @param id Database id of the product we want to update
     * @return http response which include header/body/status
     */
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

    /**
     * Fetch a list of products based on a search criteria. We also give pagination and sort capability.
     *
     * @param name search by name.
     * @param description search by description.
     * @param end index of last element of the page
     * @param start index of first element of the page
     * @param order sort type, can be either 'ASC' or 'DESC', serve as a check if we want to sort or not too
     * @param sort sort criteria, contains the field type we want to sort by
     * @return
     */
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
