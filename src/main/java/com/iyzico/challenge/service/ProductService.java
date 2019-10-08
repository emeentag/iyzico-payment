package com.iyzico.challenge.service;

import java.util.Optional;

import com.iyzico.challenge.configuration.ApplicationConfiguration;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.middleware.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * ProductService
 */
@Service
public class ProductService {

  @Autowired
  public ProductRepository productRepository;

  @Autowired
  public ApplicationConfiguration applicationConfiguration;

  public Product addProduct(Product product) {
    return this.productRepository.save(product);
  }

  public Optional<Product> getProduct(Long productId) {

    Optional<Product> productInDB = this.productRepository.findById(productId);

    return productInDB;
  }

  public Page<Product> getProducts(Pageable pageable) {
    Page<Product> productsInDB = this.productRepository.findAll(pageable);

    return productsInDB;
  }

  public Optional<Product> updateProduct(Product product) {
    final Long productId = product.getId();

    Optional<Product> productInDB = this.productRepository.findById(productId);

    productInDB = productInDB.map(p -> Optional.of(this.productRepository.save(product)))
        .orElseThrow(() -> new ResourceNotFoundException("Product: " + productId + " not found."));

    return productInDB;
  }

  public Optional<Product> deleteProduct(Long productId) {

    Optional<Product> productInDB = this.productRepository.findById(productId);

    productInDB = productInDB.map(p -> {
      this.productRepository.delete(p);
      return Optional.of(p);
    }).orElseThrow(() -> new ResourceNotFoundException("Product: " + productId + " not found."));

    return productInDB;
  }

  public BasketItem toBasketItem(Product product, BasketItemType type) {
    BasketItem basketItem = new BasketItem();

    basketItem.setId("SS" + product.getId());
    basketItem.setName(product.getName());
    basketItem.setItemType(type.name());
    basketItem.setPrice(product.getPrice());

    return basketItem;
  }
}