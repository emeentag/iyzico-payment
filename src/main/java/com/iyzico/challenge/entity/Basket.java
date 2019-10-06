package com.iyzico.challenge.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

/**
 * Basket
 */
@Entity
public class Basket {

  private enum BasketStatus {
    NOT_PAYED, PAYED
  }

  @Id
  @GeneratedValue
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "member_id", referencedColumnName = "id")
  private Member member;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "basket_product", joinColumns = {
      @JoinColumn(name = "basket_id", referencedColumnName = "id") }, inverseJoinColumns = {
          @JoinColumn(name = "product_id", referencedColumnName = "id") })
  private List<Product> products;

  @Enumerated
  private BasketStatus status = BasketStatus.NOT_PAYED;
}