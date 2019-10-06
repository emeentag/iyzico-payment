package com.iyzico.challenge.service;

import java.util.List;
import java.util.Optional;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * ProductService
 */
@Service
public class ProductService {

  @Autowired
  public ProductRepository productRepository;

  public Optional<Product> addProduct(Product product) {
    Optional<Product> returnProduct = Optional.empty();

    if (product.getId() == null) {
      returnProduct = Optional.of(this.productRepository.save(product));
    }

    return returnProduct;
  }

  public Optional<Product> getProduct(Long productId) {

    Optional<Product> returnProduct = this.productRepository.findById(productId);

    return returnProduct;
  }

  public List<Product> getProducts(int page) {
    Pageable resultForCurrentPage = PageRequest.of(page, 10);

    Page<Product> returnProducts = this.productRepository.findAll(resultForCurrentPage);

    return returnProducts.getContent();
  }

  public Optional<Product> updateProduct(Product product) {

    Long productId = product.getId();
    Optional<Product> returnProduct = Optional.empty();

    if (productId != null) {
      returnProduct = this.productRepository.findById(productId);

      if (returnProduct.isPresent()) {
        this.productRepository.save(product);
      }
    }

    return returnProduct;
  }

  public String deleteProduct(Long productId) {

    String returnString;

    Optional<Product> returnProduct = this.productRepository.findById(productId);

    if (returnProduct.isPresent()) {
      this.productRepository.delete(returnProduct.get());
      returnString = "Deleted.";
    } else {
      returnString = "No product to delete.";
    }

    return returnString;
  }
}