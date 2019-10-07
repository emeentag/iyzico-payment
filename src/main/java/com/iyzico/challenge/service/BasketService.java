package com.iyzico.challenge.service;

import java.util.Optional;
import java.util.Set;

import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.repository.BasketRepository;
import com.iyzico.challenge.repository.MemberRepository;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzipay.model.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * BasketService
 */
@Slf4j
@Service
public class BasketService {

  @Autowired
  public BasketRepository basketRepository;

  @Autowired
  public MemberRepository memberRepository;

  @Autowired
  public ProductRepository productRepository;

  @Autowired
  public ProductService productService;

  @Autowired
  public MemberService memberService;

  @Autowired
  public PaymentService paymentService;

  public Optional<Basket> getBasket(Long basketId, Long memberId) {
    Optional<Member> member = this.memberRepository.findById(memberId);
    Optional<Basket> basket = this.basketRepository.findByIdAndMember(basketId, member.get());

    if (!basket.isPresent()) {
      return Optional.empty();
    } else {
      log.info("Basket not found!");
    }

    return basket;
  }

  public Optional<Basket> addBasket(Long memberId) {
    Optional<Basket> basket = Optional.empty();
    Optional<Member> member = this.memberRepository.findById(memberId);

    if (member.isPresent()) {
      Member memberEntity = member.get();

      Basket basketEntity = new Basket();
      basketEntity.setMember(memberEntity);
      this.basketRepository.save(basketEntity);

      basket = Optional.of(basketEntity);
    } else {
      log.info("There is no member with this id!");
    }

    return basket;
  }

  public Optional<Basket> addProductToBasket(Long basketId, Long productId) {
    Optional<Basket> basket = this.basketRepository.findById(basketId);

    if (basket.isPresent()) {
      Optional<Product> product = this.productRepository.findById(productId);

      if (product.isPresent()) {
        Basket basketEntity = basket.get();

        basketEntity.getProducts().add(product.get());
        this.basketRepository.save(basketEntity);
      } else {
        log.info("There is no product with this id!");
      }
    } else {
      log.info("There is no basket with this id!");
    }

    return basket;
  }

  public Optional<Basket> deleteProductFromBasket(Long basketId, Long productId) {
    Optional<Basket> basket = this.basketRepository.findById(basketId);

    if (basket.isPresent()) {
      Optional<Product> product = this.productRepository.findById(productId);

      if (product.isPresent()) {
        Basket basketEntity = basket.get();

        basketEntity.getProducts().remove(product.get());
        this.basketRepository.save(basketEntity);
      } else {
        log.info("There is no product with this id!");
      }
    } else {
      log.info("There is no basket with this id!");
    }

    return basket;
  }

  @Transactional(rollbackFor = Exception.class)
  public Optional<Basket> buyBasket(Long basketId, Long memberId) {

    Optional<Member> member = this.memberRepository.findById(memberId);
    Optional<Basket> basket = this.basketRepository.findByIdAndMember(basketId, member.get());

    if (basket.isPresent()) {

      Basket basketEntity = basket.get();

      if (basketEntity.getStatus().equals(BasketStatus.NOT_PAYED)) {
        Set<Product> products = basketEntity.getProducts();

        if (products.size() > 0) {
          if (inStock(products)) {

            // Purchase
            Payment payment = this.paymentService.pay(basketEntity);

            if (payment.getStatus().equalsIgnoreCase("success")) {

              // Update stock count.
              updateStock(products);

              // Close the basket.
              basketEntity.setStatus(BasketStatus.PAYED);
              this.basketRepository.save(basketEntity);
            } else {
              log.error("Payment failed with error" + payment.getErrorCode() + ":" + payment.getErrorMessage());
            }
          } else {
            log.error("Product out of stock.");
          }
        } else {
          log.error("There is no product in basket.");
        }
      }
    } else {
      log.info("Basket not found!");
    }

    return basket;
  }

  private Set<Product> updateStock(Set<Product> products) {
    for (Product product : products) {
      Long stockCount = product.getStockCount();

      if (stockCount > 0) {
        product.setStockCount(stockCount - 1);
      }
    }

    this.productRepository.saveAll(products);

    return products;
  }

  private boolean inStock(Set<Product> products) {
    for (Product product : products) {
      if (product.getStockCount() == 0) {
        return false;
      }
    }
    return true;
  }
}