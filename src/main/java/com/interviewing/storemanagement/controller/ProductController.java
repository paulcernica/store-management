package com.interviewing.storemanagement.controller;

import com.interviewing.storemanagement.dto.ProductDto;
import com.interviewing.storemanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> save(@RequestBody ProductDto productDto) {
        return productService.save(productDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDto> findOneEntryById(@PathVariable Integer id) {
        return productService.getOneEntryById(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@RequestBody ProductDto productDto, @PathVariable Integer id) {
        return productService.update(productDto, id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductDto> delete(@PathVariable Integer id) {
        return productService.delete(id);
    }

    @GetMapping
    public ResponseEntity search(
            @RequestParam(value = "_end", defaultValue = "10") @Min(0) int end,
            @RequestParam(value = "_start", defaultValue = "0") @Min(1) int start,
            @RequestParam(value = "_order", required = false) String order,
            @RequestParam(value = "_sort", required = false) String sort,
            @RequestParam(value = "_name", required = false) String name,
            @RequestParam(value = "_description", required = false) String description
    ) {
        return productService.search(name, description, end, start, order, sort);
    }
}
