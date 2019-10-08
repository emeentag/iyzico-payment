package com.iyzico.challenge.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import javax.transaction.Transactional;

import com.iyzico.challenge.configuration.ApplicationConfiguration;
import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.middleware.exception.EntityNotAcceptableException;
import com.iyzico.challenge.middleware.exception.PaymentFailedException;
import com.iyzico.challenge.middleware.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.BasketRepository;
import com.iyzico.challenge.repository.MemberRepository;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.BasketService;
import com.iyzico.challenge.service.MemberService;
import com.iyzico.challenge.service.PaymentService;
import com.iyzico.challenge.service.ProductService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * BasketServiceIntegrationTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class BasketServiceIntegrationTest {

  @Autowired
  private BasketService basketService;

  @Autowired
  public BasketRepository basketRepository;

  @Autowired
  public MemberRepository memberRepository;

  @Autowired
  public ProductRepository productRepository;

  @Autowired
  public ProductService productService;

  @Autowired
  public MemberService memberService;

  @Autowired
  public PaymentService paymentService;

  @Autowired
  public ApplicationConfiguration applicationConfiguration;

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

  @Test(expected = ResourceNotFoundException.class)
  public void getBasket_should_thorow_exception_if_no_member() {
    // when
    this.basketService.getBasket(1L, 100L);

    // then throw exception
  }

  @Test(expected = ResourceNotFoundException.class)
  public void getBasket_should_thorow_exception_if_no_basket() {
    // when
    this.basketService.getBasket(100L, 1L);

    // then throw exception
  }

  @Test
  public void getBasket_should_return_basket() {
    // when
    Optional<Basket> b = this.basketService.getBasket(1L, 1L);

    // then
    assertThat(b.get().getId()).isEqualTo(basket.getId());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void createBasket_should_thorow_exception_if_no_member() {
    // when
    this.basketService.createBasket(100L);

    // then throw exception
  }

  @Test
  public void createBasket_should_create_a_basket_for_member() {
    // when
    Optional<Basket> b = this.basketService.createBasket(1L);

    // then throw exception
    assertThat(b.get().getMember().getEmail()).isEqualTo(member.getEmail());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void addProductToBasket_should_thorow_exception_if_no_basket() {
    // when
    this.basketService.addProductToBasket(100L, 1L);

    // then throw exception
  }

  @Test(expected = ResourceNotFoundException.class)
  public void addProductToBasket_should_thorow_exception_if_no_product() {
    // when
    this.basketService.addProductToBasket(1L, 100L);

    // then throw exception
  }

  @Test
  public void addProductToBasket_should_add_a_ptoduct_for_basket() {
    // when
    Optional<Basket> b = this.basketService.addProductToBasket(1L, 1L);

    // then throw exception
    assertThat(b.get().getProducts().size()).isEqualTo(1);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void deleteProductFromBasket_should_thorow_exception_if_no_basket() {
    // when
    this.basketService.deleteProductFromBasket(100L, 1L);

    // then throw exception
  }

  @Test(expected = ResourceNotFoundException.class)
  public void deleteProductFromBasket_should_thorow_exception_if_no_product() {
    // when
    this.basketService.deleteProductFromBasket(1L, 100L);

    // then throw exception
  }

  @Test
  public void deleteProductFromBasket_should_remove_a_ptoduct_from_basket() {
    // given
    basket.getProducts().add(product);
    this.basketRepository.save(basket);

    // when
    Optional<Basket> b = this.basketService.deleteProductFromBasket(1L, 1L);

    // then throw exception
    assertThat(b.get().getProducts().size()).isEqualTo(0);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void buyBasket_should_thorow_exception_if_no_member() {
    // when
    this.basketService.buyBasket(1L, 100L);

    // then throw exception
  }

  @Test(expected = ResourceNotFoundException.class)
  public void buyBasket_should_thorow_exception_if_no_basket() {
    // when
    this.basketService.buyBasket(100L, 1L);

    // then throw exception
  }

  @Test(expected = EntityNotAcceptableException.class)
  public void buyBasket_should_thorow_exception_if_satatus_is_not_acceptable() {
    // given
    basket.setStatus(BasketStatus.PAYED);
    this.basketRepository.save(basket);

    // when
    this.basketService.buyBasket(1L, 1L);

    // then throw exception
  }

  @Test(expected = EntityNotAcceptableException.class)
  public void buyBasket_should_thorow_exception_if_no_product_in_basket() {
    // when
    this.basketService.buyBasket(1L, 1L);

    // then throw exception
  }

  @Test(expected = EntityNotAcceptableException.class)
  public void buyBasket_should_thorow_exception_if_product_out_stock() {
    // given
    product.setStockCount(0L);
    basket.getProducts().add(product);
    this.basketRepository.save(basket);

    // when
    this.basketService.buyBasket(1L, 1L);

    // then throw exception
  }

  @Test(expected = PaymentFailedException.class)
  public void buyBasket_should_thorow_exception_if_payment_failed() {
    // given
    this.applicationConfiguration.setPaymentApiKey("Fake API");
    basket.getProducts().add(product);
    this.basketRepository.save(basket);

    // when
    this.basketService.buyBasket(1L, 1L);

    // then throw exception
  }

  @Test
  public void buyBasket_should_update_stock_count_basket_status_as_PAYED() {
    // given
    Long currentStockCount = product.getStockCount();
    basket.getProducts().add(product);
    this.basketRepository.save(basket);

    // when
    this.basketService.buyBasket(1L, 1L);

    // then
    assertThat(product.getStockCount()).isEqualTo(currentStockCount - 1);
    assertThat(basket.getStatus()).isEqualTo(BasketStatus.PAYED);
  }
}