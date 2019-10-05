package com.iyzico.challenge.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductController
 */
@RestController
public class ProductController {

  @GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getProduct(@RequestParam(required = true, name = "id") Long id) {
    return null;
  }

  @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getProducts() {
    return null;
  }

  @PostMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> addProduct(@RequestBody String product) {
    return null;
  }

  @PutMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updateProduct(@RequestBody String product) {
   return null; 
  }

  @DeleteMapping(value = "/product")
  public ResponseEntity<String> deleteProduct(@RequestParam(required = true, name = "id") Long id) {
    return null;
  }
}