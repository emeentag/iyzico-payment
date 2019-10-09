package com.iyzico.challenge.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;

import javax.transaction.Transactional;

import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.repository.BasketRepository;
import com.iyzico.challenge.repository.MemberRepository;
import com.iyzico.challenge.repository.ProductRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * BasketControllerIntegrationTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Transactional
public class BasketControllerIntegrationTest {

  @Autowired
  public BasketRepository basketRepository;

  @Autowired
  public MemberRepository memberRepository;

  @Autowired
  public ProductRepository productRepository;

  @Autowired
  public MockMvc mockMvc;

  private Member member;

  private Basket basket;

  Product product;

  @Before
  public void setUp() {
    member = new Member(null, "Test Member", "test@test.com");
    this.memberRepository.save(member);

    product = new Product(null, "Test product", "details", new BigDecimal("10"), 10L, new HashSet<>());
    this.productRepository.save(product);

    basket = new Basket(null, member, new HashSet<>(), BasketStatus.NOT_PAYED);
    this.basketRepository.save(basket);
  }

  @Test
  public void createBasket_should_return_OK_with_right_content() throws IllegalArgumentException, Exception {
    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/basket/add?member_id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    // then
    assertThat(content.contains("Basket created.")).isTrue();
  }

  @Test
  public void createBasket_should_return_NOT_FOUND() throws IllegalArgumentException, Exception {
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/basket/add?member_id=100"))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void getBasket_should_return_OK_with_right_content() throws IllegalArgumentException, Exception {
    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/basket?basket_id=1&member_id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    // then
    assertThat(content.contains("\"id\":1")).isTrue();
  }

  @Test
  public void getBasket_should_return_NOT_FOUND_when_no_member() throws IllegalArgumentException, Exception {
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/basket?basket_id=1&member_id=100"))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void getBasket_should_return_NOT_FOUND_when_no_basket_for_member() throws IllegalArgumentException, Exception {
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/basket?basket_id=100&member_id=1"))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void addProductToBasket_should_return_OK_with_right_content() throws IllegalArgumentException, Exception {
    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/basket/add/product?basket_id=1&product_id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    // then
    assertThat(content.contains("Product added to the basket.")).isTrue();
  }

  @Test
  public void addProductToBasket_should_return_NOT_FOUND_when_no_basket() throws IllegalArgumentException, Exception {
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/basket/add/product?basket_id=100&product_id=1"))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void addProductToBasket_should_return_NOT_FOUND_when_no_product_for_basket() throws IllegalArgumentException, Exception {
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/basket/add/product?basket_id=1&product_id=100"))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void deleteProductFromBasket_should_return_OK_with_right_content() throws IllegalArgumentException, Exception {
    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/basket/product?basket_id=1&product_id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    // then
    assertThat(content.contains("Product deleted from the basket.")).isTrue();
  }

  @Test
  public void deleteProductFromBasket_should_return_NOT_FOUND_when_no_basket() throws IllegalArgumentException, Exception {
    // when
    mockMvc.perform(MockMvcRequestBuilders.delete("/basket/product?basket_id=100&product_id=1"))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void deleteProductFromBasket_should_return_NOT_FOUND_when_no_product_for_basket() throws IllegalArgumentException, Exception {
    // when
    mockMvc.perform(MockMvcRequestBuilders.delete("/basket/product?basket_id=1&product_id=100"))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void buyBasket_should_return_OK_with_right_content() throws IllegalArgumentException, Exception {
    // given
    basket.getProducts().add(product);
    this.basketRepository.save(basket);
    
    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/basket/buy?basket_id=1&member_id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    // then
    assertThat(content.contains("Basket purchased.")).isTrue();
    assertThat(basket.getStatus()).isEqualTo(BasketStatus.PAYED);
  }

  @Test
  public void buyBasket_should_return_NOT_FOUND_when_no_basket() throws IllegalArgumentException, Exception {
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/basket/buy?basket_id=100&member_id=1"))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
  }

  @Test
  public void buyBasket_should_return_NOT_FOUND_when_basket_status_not_valid() throws IllegalArgumentException, Exception {    // given
    // given
    basket.setStatus(BasketStatus.PAYED);
    this.basketRepository.save(basket);

    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/basket/buy?basket_id=1&member_id=1"))
        .andExpect(MockMvcResultMatchers.status().isExpectationFailed()).andReturn();
  }

  @Test
  public void buyBasket_should_return_NOT_FOUND_when_no_product_in_basket() throws IllegalArgumentException, Exception {
    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/basket/buy?basket_id=1&member_id=1"))
        .andExpect(MockMvcResultMatchers.status().isExpectationFailed()).andReturn();
  }
}