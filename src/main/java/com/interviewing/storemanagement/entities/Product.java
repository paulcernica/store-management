package com.interviewing.storemanagement.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue
    private Integer id;

    @Basic
    private String name;

    @Basic
    private String description;

    @Basic
    private Double price;

    @Basic
    private Integer quantity;

}
