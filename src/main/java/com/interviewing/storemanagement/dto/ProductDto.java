package com.interviewing.storemanagement.dto;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {

    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;

}
