package com.iyzico.challenge.controller;

import java.util.Optional;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

  @Autowired
  public ProductService productService;

  @GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> getProduct(@RequestParam(required = true, name = "id") Long id) {
    ResponseEntity<Product> response;
    Optional<Product> product = this.productService.getProduct(id);

    if (product.isPresent()) {
      response = ResponseEntity.ok(product.get());
    } else {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return response;
  }

  @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<Product> getProducts(@RequestParam(required = false) Pageable pageable) {
    return this.productService.getProducts(pageable);
  }

  @PostMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> addProduct(@RequestBody Product product) {
    this.productService.addProduct(product);
    return ResponseEntity.ok("Product saved.");
  }

  @PutMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> updateProduct(@RequestBody Product product) {
    ResponseEntity<String> response;
    Optional<Product> p = this.productService.updateProduct(product);

    if (p.isPresent()) {
      response = ResponseEntity.ok("Product updated.");
    } else {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return response;
  }

  @DeleteMapping(value = "/product", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> deleteProduct(@RequestParam(required = true, name = "id") Long id) {
    ResponseEntity<String> response;
    Optional<Product> p = this.productService.deleteProduct(id);

    if (p.isPresent()) {
      response = ResponseEntity.ok("Product deletd.");
    } else {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return response;
  }
}