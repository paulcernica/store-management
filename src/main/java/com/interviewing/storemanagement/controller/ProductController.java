package com.interviewing.storemanagement.controller;

import com.interviewing.storemanagement.dto.ProductDto;
import com.interviewing.storemanagement.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid ProductDto productDto) {
        log.info("Starting inserting a new product with name {}.", productDto.getName());
        return productService.save(productDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDto> findOneEntryById(@PathVariable @NotNull Integer id) {
        log.info("Start fetching information about the product with the id {}", id);
        return productService.getOneEntryById(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@RequestBody @Valid ProductDto productDto, @PathVariable @NotNull Integer id) {
        log.info("Update product details with the id {}", id);
        return productService.update(productDto, id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductDto> delete(@PathVariable @NotNull Integer id) {
        log.info("Delete product with the id {}", id);
        return productService.delete(id);
    }

    @GetMapping
    public ResponseEntity<?> search(
            @RequestParam(value = "_end", defaultValue = "10") @Min(0) int end,
            @RequestParam(value = "_start", defaultValue = "0") @Min(1) int start,
            @RequestParam(value = "_order", required = false) String order,
            @RequestParam(value = "_sort", required = false) String sort,
            @RequestParam(value = "_name", required = false) String name,
            @RequestParam(value = "_description", required = false) String description
    ) {
        log.info("Start searching for products");
        return productService.search(name, description, end, start, order, sort);
    }
}
