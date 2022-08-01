package com.interviewing.storemanagement.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {

    private Integer id;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Double price;
    @NotNull
    private Integer quantity;

}
