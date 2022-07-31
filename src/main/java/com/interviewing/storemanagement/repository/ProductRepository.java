package com.interviewing.storemanagement.repository;

import com.interviewing.storemanagement.entities.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>,
        JpaSpecificationExecutor<Product> {

    Product findByName(String id);

}
