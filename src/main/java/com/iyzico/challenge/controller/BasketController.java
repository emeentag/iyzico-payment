package com.iyzico.challenge.controller;

import java.util.Optional;

import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.service.BasketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * BasketController
 */
@RestController
public class BasketController {

  @Autowired
  public BasketService basketService;

  @GetMapping(value = "/basket/add", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createBasket(@RequestParam(required = true, name = "member_id") Long memberId) {
    ResponseEntity<String> response;
    Optional<Basket> basket = this.basketService.createBasket(memberId);

    if (basket.isPresent()) {
      response = ResponseEntity.ok("Basket created.");
    } else {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return response;
  }

  @GetMapping(value = "/basket", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Basket> getBasket(@RequestParam(required = true, name = "basket_id") Long basketId,
      @RequestParam(required = true, name = "member_id") Long memberId) {
    ResponseEntity<Basket> response;
    Optional<Basket> basket = this.basketService.getBasket(basketId, memberId);

    if (basket.isPresent()) {
      response = ResponseEntity.ok(basket.get());
    } else {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return response;
  }

  @GetMapping(value = "/basket/add/product", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> addProductToBasket(@RequestParam(required = true, name = "basket_id") Long basketId,
      @RequestParam(required = true, name = "product_id") Long productId) {
    ResponseEntity<String> response;
    Optional<Basket> basket = this.basketService.addProductToBasket(basketId, productId);

    if (basket.isPresent()) {
      response = ResponseEntity.ok("Product added to the basket.");
    } else {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return response;
  }

  @DeleteMapping(value = "/basket", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> deleteProductFromBasket(
      @RequestParam(required = true, name = "basket_id") Long basketId,
      @RequestParam(required = true, name = "product_id") Long productId) {
    ResponseEntity<String> response;
    Optional<Basket> basket = this.basketService.deleteProductFromBasket(basketId, productId);

    if (basket.isPresent()) {
      response = ResponseEntity.ok("Product deleted from the basket.");
    } else {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return response;
  }

  @GetMapping(value = "/basket/buy", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> buyBasket(@RequestParam(required = true, name = "basket_id") Long basketId,
      @RequestParam(required = true, name = "member_id") Long memberId) {
    ResponseEntity<String> response;
    Optional<Basket> basket = this.basketService.buyBasket(basketId, memberId);

    if (basket.isPresent()) {
      response = ResponseEntity.ok("Basket purchased.");
    } else {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return response;
  }
}