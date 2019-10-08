package com.iyzico.challenge.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Product
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Product extends Auditable {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "name", columnDefinition = "VARCHAR(120)", nullable = false)
  private String name;

  @Column(name = "details", columnDefinition = "VARCHAR(255)", nullable = true)
  private String details;

  @Column(name = "price", columnDefinition = "DECIMAL", nullable = false)
  private BigDecimal price;

  @Column(name = "stock_count", columnDefinition = "BIGINT", nullable = false)
  private Long stockCount;

  @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Basket> baskets = new HashSet<>();
}