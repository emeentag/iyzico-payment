package com.iyzico.challenge.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import com.iyzico.challenge.configuration.ApplicationConfiguration;
import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.middleware.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.ProductService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ProductServiceIntegrationTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductServiceIntegrationTest {

  @Autowired
  private ProductService productService;

  @Autowired
  public ProductRepository productRepository;

  @MockBean
  public ApplicationConfiguration applicationConfiguration;

  private Basket basket;

  private Product product;

  @Before
  public void setUp() {
    basket = new Basket(null, null, new HashSet<>(), BasketStatus.NOT_PAYED);
    product = new Product(null, "Test Product", "Test details", new BigDecimal("20"), 10L, new HashSet<>());

    basket.getProducts().add(product);

    this.productRepository.save(product);
  }

  @Test
  public void addProduct_should_save_and_return_saved_product() {
    // given
    Product p = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());

    // when
    Product expectedProduct = this.productService.addProduct(p);
    Optional<Product> productInDB = this.productRepository.findById(2L);

    // then
    assertThat(expectedProduct.getId()).isEqualTo(productInDB.get().getId());
    assertThat(expectedProduct.getName()).isEqualTo(productInDB.get().getName());
    assertThat(expectedProduct.getPrice()).isEqualTo(productInDB.get().getPrice());
  }

  @Test
  public void getProduct_should_return_product() {
    // when
    Optional<Product> expectedProduct = this.productService.getProduct(product.getId());

    // then
    assertThat(expectedProduct.get().getId()).isEqualTo(product.getId());
  }

  @Test
  public void getProducts_should_return_product_inPage() {
    // given
    Product p1 = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());
    Product p2 = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());

    this.productRepository.save(p1);
    this.productRepository.save(p2);

    // when
    Page<Product> expectedProducts = this.productService.getProducts(PageRequest.of(0, 2));

    // then
    assertThat(expectedProducts.getContent().size()).isEqualTo(2);

    // when
    expectedProducts = this.productService.getProducts(PageRequest.of(1, 2));

    // then
    assertThat(expectedProducts.getContent().size()).isEqualTo(1);
  }

  @Test
  public void updateProduct_should_update_product() {
    // given
    product.setName("Updated name");

    // when
    Optional<Product> expectedProduct = this.productService.updateProduct(product);

    // then
    assertThat(expectedProduct.get().getName()).isEqualTo(product.getName());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void updateProduct_should_throw_exception_if_not_product_exist() {
    // given
    Product p1 = new Product(2000L, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());

    // when
    this.productService.updateProduct(p1);

    // then throw exception
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void updateProduct_should_throw_exception_if_no_product_id() {
    // given
    Product p1 = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());

    // when
    this.productService.updateProduct(p1);

    // then throw exception
  }

  @Test
  public void deleteProduct_should_delete_product() {
    // given
    this.productService.productRepository = Mockito.mock(ProductRepository.class);
    Mockito.when(this.productService.productRepository.findById(product.getId())).thenReturn(Optional.of(product));

    // when
    this.productService.deleteProduct(product.getId());

    // then
    Mockito.verify(this.productService.productRepository, Mockito.atLeast(1)).delete(product);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void deleteProduct_should_throw_exception_product_if_not_exist() {
    // given
    this.productService.productRepository = Mockito.mock(ProductRepository.class);
    Mockito.when(this.productService.productRepository.findById(product.getId())).thenReturn(Optional.empty());

    // when
    this.productService.deleteProduct(product.getId());

    // then throw exception
  }

}