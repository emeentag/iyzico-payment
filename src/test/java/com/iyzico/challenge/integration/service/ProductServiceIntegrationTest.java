package com.iyzico.challenge.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.iyzico.challenge.configuration.ApplicationConfiguration;
import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.ProductService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ProductService
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

  private Member member;

  private Basket basket;

  private Product product;

  @Before
  public void setUp() {
    member = new Member(null, "Test Member", "test@test.com", new HashSet<>());
    basket = new Basket(null, member, new HashSet<>(), BasketStatus.NOT_PAYED);
    product = new Product(null, "Test Product", "Test details", new BigDecimal("20"), 10L, new HashSet<>());
    product = new Product(null, "Test Product", "Test details", new BigDecimal("20"), 10L, new HashSet<>());

    member.getBaskets().add(basket);
    basket.getProducts().add(product);

    this.productRepository.save(product);
  }

  @Test
  public void addProduct_should_save_if_not_in_db() {
    // given
    Product p = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());

    // when
    Optional<Product> expectedProduct = this.productService.addProduct(p);
    Optional<Product> productInDB = this.productRepository.findById(2L);

    //then
    assertThat(expectedProduct.get().getId()).isEqualTo(productInDB.get().getId());
    assertThat(expectedProduct.get().getName()).isEqualTo(productInDB.get().getName());
    assertThat(expectedProduct.get().getPrice()).isEqualTo(productInDB.get().getPrice());
  }

  @Test
  public void addProduct_should_not_save_if_exist_in_db() {
    // when
    Optional<Product> expectedProduct = this.productService.addProduct(product);

    //then
    assertThat(expectedProduct).isEmpty();
  }

  @Test
  public void getProduct_should_return_product() {
    // when
    Optional<Product> expectedProduct = this.productService.getProduct(product.getId());

    //then
    assertThat(expectedProduct.get().getId()).isEqualTo(product.getId());
  }

  @Test
  public void getProducts_should_return_product_inPage() {
    // given
    Mockito.when(this.applicationConfiguration.getItemsInSinglePage()).thenReturn(2);
    Product p1 = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());
    Product p2 = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());

    this.productRepository.save(p1);
    this.productRepository.save(p2);

    // when
    List<Product> expectedProducts = this.productService.getProducts(0);

    //then
    assertThat(expectedProducts.size()).isEqualTo(2);

    // when
    expectedProducts = this.productService.getProducts(1);

    //then
    assertThat(expectedProducts.size()).isEqualTo(1);
  }
  
}