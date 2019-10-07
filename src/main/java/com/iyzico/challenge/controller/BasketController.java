package com.iyzico.challenge.controller;

import com.iyzico.challenge.entity.Basket;

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

  @GetMapping(value = "/basket/add", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> addBasket(@RequestParam(required = true, name = "member_id") Long memberId) {
    return null;
  }

  @GetMapping(value = "/basket", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Basket> getBasket(@RequestParam(required = true, name = "basket_id") Long basketId,
      @RequestParam(required = true, name = "member_id") Long memberId) {
    return null;
  }

  @GetMapping(value = "/basket/add/product", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> addProductToBasket(@RequestParam(required = true, name = "basket_id") Long basketId,
      @RequestParam(required = true, name = "product_id") Long productId) {
    return null;
  }

  @DeleteMapping(value = "/basket", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> deleteProductFromBasket(@RequestParam(required = true, name = "basket_id") Long basketId,
      @RequestParam(required = true, name = "product_id") Long productId) {
    return null;
  }

  @GetMapping(value = "/basket/buy", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> buyBasket(@RequestParam(required = true, name = "basket_id") Long basketId,
      @RequestParam(required = true, name = "member_id") Long memberId) {
    return null;
  }
}