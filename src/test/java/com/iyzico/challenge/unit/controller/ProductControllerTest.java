package com.iyzico.challenge.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import com.iyzico.challenge.controller.ProductController;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.service.ProductService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * ProductControllerTest
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ProductController.class)
public class ProductControllerTest {

  @Mock
  private ProductService productService;

  private ProductController productController;

  Product productWithId;

  Product productWithoutId;

  @Before
  public void setUp() {
    productController = new ProductController();
    productController.productService = productService;
    productWithId = new Product(1L, "Test product", "details", new BigDecimal("20"), 10L, new HashSet<>());
    productWithoutId = new Product(null, "Test product", "details", new BigDecimal("20"), 10L, new HashSet<>());
  }

  @Test
  public void getProduct_should_return_OK() throws Exception {
    // given
    Product product = productWithId;
    Mockito.when(this.productService.getProduct(product.getId())).thenReturn(Optional.of(product));

    // when
    ResponseEntity<Product> returnProduct = this.productController.getProduct(product.getId());

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void getProduct_should_return_NOT_FOUND() throws Exception {
    // given
    Product product = productWithId;
    Mockito.when(this.productService.getProduct(product.getId())).thenReturn(Optional.empty());

    // when
    ResponseEntity<Product> returnProduct = this.productController.getProduct(product.getId());

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void addProduct_should_return_OK() throws Exception {
    // given
    Product product = productWithId;
    Mockito.when(this.productService.addProduct(product)).thenReturn(product);

    // when
    ResponseEntity<String> returnProduct = this.productController.addProduct(product);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void updateProduct_should_return_OK() throws Exception {
    // given
    Product product = productWithId;
    Mockito.when(this.productService.updateProduct(product)).thenReturn(Optional.of(product));

    // when
    ResponseEntity<String> returnProduct = this.productController.updateProduct(product);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void updateProduct_should_return_NOT_FOUND() throws Exception {
    // given
    Product product = productWithId;
    Mockito.when(this.productService.updateProduct(product)).thenReturn(Optional.empty());

    // when
    ResponseEntity<String> returnProduct = this.productController.updateProduct(product);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void deleteMember_should_return_OK() throws Exception {
    // given
    Product product = productWithId;
    Mockito.when(this.productService.deleteProduct(product.getId())).thenReturn(Optional.of(product));

    // when
    ResponseEntity<String> returnProduct = this.productController.deleteProduct(product.getId());

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void deleteProduct_should_return_NOT_FOUND() throws Exception {
    // given
    Product product = productWithId;
    Mockito.when(this.productService.deleteProduct(product.getId())).thenReturn(Optional.empty());

    // when
    ResponseEntity<String> returnProduct = this.productController.deleteProduct(product.getId());

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

}