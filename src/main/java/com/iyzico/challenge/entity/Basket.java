package com.iyzico.challenge.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basket
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Basket {

  public enum BasketStatus {
    NOT_PAYED, PAYED
  }

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id", referencedColumnName = "id")
  private Member member;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "basket_product", joinColumns = {
      @JoinColumn(name = "basket_id", referencedColumnName = "id") }, inverseJoinColumns = {
          @JoinColumn(name = "product_id", referencedColumnName = "id") })
  private Set<Product> products;

  @Enumerated(EnumType.STRING)
  private BasketStatus status = BasketStatus.NOT_PAYED;
}