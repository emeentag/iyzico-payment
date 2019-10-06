package com.iyzico.challenge.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.CardMaskingService;
import com.iyzico.challenge.service.ProductService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

/**
 * ProductService
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CardMaskingService.class)
public class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  private ProductService productService;

  @Before
  public void setUp() {
    productService = new ProductService();
    productService.productRepository = productRepository;
  }

  @Test
  public void addProduct_should_return_empty_when_has_id() {
    // given
    Product product = new Product(1L, "Test Product", "Product details", BigDecimal.valueOf(120L), 20L);

    // when
    Optional<Product> returnProduct = productService.addProduct(product);

    // then
    assertThat(returnProduct).isEmpty();
  }

  @Test
  public void addProduct_should_return_product_when_has_not_id() throws Exception {
    // given
    Product product = new Product(null, "Test Product", "Product details", BigDecimal.valueOf(120L), 20L);
    Mockito.when(productRepository.save(product)).thenReturn(product);

    // when
    Optional<Product> returnProduct = productService.addProduct(product);

    // then
    assertThat(returnProduct).isEqualTo(Optional.of(product));
  }

  @Test
  public void updateProduct_should_return_empty_when_has_not_id() throws Exception {
    // given
    Product product = new Product(null, "Test Product", "Product details", BigDecimal.valueOf(120L), 20L);

    // when
    Optional<Product> returnProduct = productService.updateProduct(product);

    // then
    assertThat(returnProduct).isEmpty();
  }

  @Test
  public void updateProduct_should_return_product_when_has_id() throws Exception {
    // given
    Product product = new Product(1L, "Test Product", "Product details", BigDecimal.valueOf(120L), 20L);
    Optional<Product> returnProduct = Optional.of(product);
    Mockito.when(productRepository.save(product)).thenReturn(product);
    Mockito.when(productRepository.findById(product.getId())).thenReturn(returnProduct);

    // when
    returnProduct = productService.updateProduct(product);

    // then
    assertThat(returnProduct).isEqualTo(Optional.of(product));
  }

  @Test
  public void updateProduct_should_return_empty_when_is_not_present() throws Exception {
    // given
    Product product = new Product(1L, "Test Product", "Product details", BigDecimal.valueOf(120L), 20L);
    Optional<Product> returnProduct = Optional.empty();
    Mockito.when(productRepository.findById(product.getId())).thenReturn(returnProduct);

    // when
    returnProduct = productService.updateProduct(product);

    // then
    assertThat(returnProduct).isEmpty();
  }

  @Test
  public void getProduct_should_return_product() throws Exception {
    // given
    Product product = new Product(1L, "Test Product", "Product details", BigDecimal.valueOf(120L), 20L);
    Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

    // when
    Optional<Product> returnProduct = productService.getProduct(product.getId());

    // then
    assertThat(returnProduct).isEqualTo(Optional.of(product));
  }

  @Test
  public void getProducts_should_return_products() throws Exception {
    // given
    Product product = new Product(1L, "Test Product", "Product details", BigDecimal.valueOf(120L), 20L);
    Page<Product> page = new PageImpl<>(Arrays.asList(product, product));
    Mockito.when(productRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

    // when
    List<Product> returnProduct = productService.getProducts(0);

    // then
    assertThat(returnProduct.size()).isEqualTo(2);
  }

  @Test
  public void deleteProduct_should_return_deleted_when_there_is_sth_do_delete() throws Exception {
    // given
    Product product = new Product(1L, "Test Product", "Product details", BigDecimal.valueOf(120L), 20L);
    Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

    // when
    String returnString = productService.deleteProduct(product.getId());

    // then
    assertThat(returnString).isEqualTo("Deleted.");
  }

  @Test
  public void deleteProduct_should_return_not_deleted_when_there_is_sth_do_delete() throws Exception {
    // given
    Product product = new Product(1L, "Test Product", "Product details", BigDecimal.valueOf(120L), 20L);
    Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

    // when
    String returnString = productService.deleteProduct(product.getId());

    // then
    assertThat(returnString).isEqualTo("No product to delete.");
  }

}