package com.iyzico.challenge.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.repository.ProductRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * ProductControllerIntegrationTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

  @Autowired
  public ProductRepository productRepository;

  @Autowired
  public MockMvc mockMvc;

  private Basket basket;

  private Product product;

  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    basket = new Basket(null, null, new HashSet<>(), BasketStatus.NOT_PAYED);
    product = new Product(null, "Test Product", "Test details", new BigDecimal("20"), 10L, new HashSet<>());

    basket.getProducts().add(product);

    this.productRepository.save(product);

    objectMapper = new ObjectMapper();
  }

  @Test
  public void addProduct_should_return_OK() throws IllegalArgumentException, Exception {
    // given
    Product p = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());

    // when
    mockMvc.perform(MockMvcRequestBuilders.post("/product").contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(p))).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void getProduct_should_return_OK_and_right_content() throws IllegalArgumentException, Exception {
    // given
    Product p = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());
    this.productRepository.save(p);

    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/product?id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    // then
    String content = result.getResponse().getContentAsString();
    assertThat(content.contains("\"id\":1")).isTrue();
  }

  @Test
  public void getProduct_should_return_NOT_FOUND() throws IllegalArgumentException, Exception {
    // given
    Product p = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());
    this.productRepository.save(p);

    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/product?id=100"))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void getProducts_should_return_OK_and_content_in_page() throws IllegalArgumentException, Exception {
    // given
    Product p = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());
    this.productRepository.save(p);
    Product p2 = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());
    this.productRepository.save(p2);
    Product p3 = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());
    this.productRepository.save(p3);
    Product p4 = new Product(null, "Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());
    this.productRepository.save(p4);

    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products?page=0&size=2"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    // then
    assertThat(content.contains("\"id\":1")).isTrue();
    assertThat(content.contains("\"id\":2")).isTrue();
    assertThat(content.contains("\"id\":3")).isFalse();

    // when
    result = mockMvc.perform(MockMvcRequestBuilders.get("/products?page=2&size=2"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    content = result.getResponse().getContentAsString();

    // then
    assertThat(content.contains("\"id\":5")).isTrue();
  }

  @Test
  public void updateProduct_should_return_OK() throws IllegalArgumentException, Exception {
    // given
    Product p = new Product(1L, "Updated Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());

    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/product")
        .contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(p)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    // then
    assertThat(content.contains("Product updated.")).isTrue();
  }

  @Test
  public void updateProduct_should_return_NOT_FOUND() throws IllegalArgumentException, Exception {
    // given
    Product p = new Product(100L, "Updated Test Product", "Test details", new BigDecimal("10"), 10L, new HashSet<>());

    // when
    mockMvc.perform(MockMvcRequestBuilders.put("/product").contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(p))).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void deleteProduct_should_return_OK() throws IllegalArgumentException, Exception {
    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/product?id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    // then
    assertThat(content.contains("Product deleted.")).isTrue();
  }

  @Test
  public void deleteProduct_should_return_NOT_FOUND() throws IllegalArgumentException, Exception {
    // when
    mockMvc.perform(MockMvcRequestBuilders.delete("/product?id=100"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

}