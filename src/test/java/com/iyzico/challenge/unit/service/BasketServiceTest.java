package com.iyzico.challenge.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.middleware.exception.EntityNotAcceptableException;
import com.iyzico.challenge.middleware.exception.PaymentFailedException;
import com.iyzico.challenge.middleware.exception.ResourceNotFoundException;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.repository.BasketRepository;
import com.iyzico.challenge.repository.MemberRepository;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.BasketService;
import com.iyzico.challenge.service.MemberService;
import com.iyzico.challenge.service.PaymentService;
import com.iyzico.challenge.service.ProductService;
import com.iyzipay.model.Payment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * BasketServiceTest
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BasketService.class)
public class BasketServiceTest {

  @Mock
  public BasketRepository basketRepository;

  @Mock
  public MemberRepository memberRepository;

  @Mock
  public ProductRepository productRepository;

  @Mock
  public ProductService productService;

  @Mock
  public MemberService memberService;

  @Mock
  public PaymentService paymentService;

  private BasketService basketService;

  private Optional<Member> optionalMember;

  private Optional<Basket> optionalBasket;

  private Member member;

  private Basket basket;

  @Before
  public void setUp() {
    basketService = new BasketService();

    basketService.basketRepository = this.basketRepository;
    basketService.memberRepository = this.memberRepository;
    basketService.productRepository = this.productRepository;
    basketService.productService = this.productService;
    basketService.memberService = this.memberService;
    basketService.paymentService = this.paymentService;

    member = new Member(1L, "Test", "test@test.com");
    basket = new Basket(1L, member, new HashSet<Product>(), BasketStatus.NOT_PAYED);

    optionalMember = Optional.of(member);
    optionalBasket = Optional.of(basket);
  }

  @Test
  public void getBasket_should_return_a_basket_if_exist_in_db() {
    // given
    Long memberId = member.getId();
    Long basketId = basket.getId();
    Mockito.when(basketService.memberRepository.existsById(memberId)).thenReturn(true);
    Mockito.when(basketService.basketRepository.findByIdAndMemberId(basketId, memberId)).thenReturn(optionalBasket);

    // when
    Optional<Basket> expectedBasket = this.basketService.getBasket(basketId, memberId);

    // then
    assertThat(expectedBasket.get()).isEqualTo(basket);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void getBasket_should_throw_exception_if_member_not_exist() {
    // given
    Long memberId = member.getId();
    Long basketId = basket.getId();
    Mockito.when(basketService.memberRepository.existsById(memberId)).thenReturn(false);

    // when
    this.basketService.getBasket(basketId, memberId);

    // then throw exception
  }

  @Test(expected = ResourceNotFoundException.class)
  public void getBasket_should_throw_exception_if_basket_not_exist() {
    // given
    Long memberId = member.getId();
    Long basketId = basket.getId();
    Mockito.when(basketService.memberRepository.existsById(memberId)).thenReturn(true);
    Mockito.when(basketService.basketRepository.findByIdAndMemberId(basketId, memberId)).thenReturn(Optional.empty());

    // when
    this.basketService.getBasket(basketId, memberId);

    // then throw exception
  }

  @Test
  public void createBasket_should_return_basket_if_member_has_not_got() {
    // given
    Long memberId = member.getId();
    Mockito.when(basketService.memberRepository.findById(memberId)).thenReturn(optionalMember);

    // when
    Optional<Basket> expectedBasket = this.basketService.createBasket(memberId);

    // then
    assertThat(expectedBasket.get().getMember()).isEqualTo(basket.getMember());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void createBasket_should_throw_exception_if_no_member_exist() {
    // given
    Long memberId = member.getId();
    Mockito.when(basketService.memberRepository.findById(memberId)).thenReturn(Optional.empty());

    // when
    this.basketService.createBasket(memberId);

    // then throw exception
  }

  @Test
  public void addProductToBasket_should_add_product_to_basket() {
    // given
    Long basketId = basket.getId();

    Optional<Product> optionalProduct = Optional
        .of(new Product(1L, "Test product", "details", new BigDecimal("20"), 20L, null));
    Long productId = optionalProduct.get().getId();

    Mockito.when(basketService.productRepository.findById(productId)).thenReturn(optionalProduct);
    Mockito.when(basketService.basketRepository.findById(basketId)).thenReturn(optionalBasket);

    // when
    Optional<Basket> expectedBasket = this.basketService.addProductToBasket(basketId, productId);

    // then
    assertThat(expectedBasket.get().getProducts().size()).isEqualTo(1);
    assertThat(expectedBasket.get().getProducts().toArray()[0]).isEqualTo(optionalProduct.get());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void addProductToBasket_should_throw_exception_if_no_basket() {
    // given
    Long basketId = basket.getId();

    Optional<Product> optionalProduct = Optional
        .of(new Product(1L, "Test product", "details", new BigDecimal("20"), 20L, null));
    Long productId = optionalProduct.get().getId();

    Mockito.when(basketService.productRepository.findById(productId)).thenReturn(optionalProduct);
    Mockito.when(basketService.basketRepository.findById(basketId)).thenReturn(Optional.empty());

    // when
    this.basketService.addProductToBasket(basketId, productId);

    // then throw exception
  }

  @Test(expected = ResourceNotFoundException.class)
  public void addProductToBasket_should_throw_exception_if_no_product() {
    // given
    Long basketId = basket.getId();

    Optional<Product> optionalProduct = Optional
        .of(new Product(1L, "Test product", "details", new BigDecimal("20"), 20L, null));
    Long productId = optionalProduct.get().getId();

    Mockito.when(basketService.productRepository.findById(productId)).thenReturn(Optional.empty());
    Mockito.when(basketService.basketRepository.findById(basketId)).thenReturn(optionalBasket);

    // when
    this.basketService.addProductToBasket(basketId, productId);

    // then throw exception
  }

  @Test
  public void deleteProductFromBasket_should_delete_product_from_basket() {
    // given
    Long basketId = basket.getId();

    Optional<Product> optionalProduct = Optional
        .of(new Product(1L, "Test product", "details", new BigDecimal("20"), 20L, null));
    Long productId = optionalProduct.get().getId();

    optionalBasket.get().getProducts().add(optionalProduct.get());

    Mockito.when(basketService.productRepository.findById(productId)).thenReturn(optionalProduct);
    Mockito.when(basketService.basketRepository.findById(basketId)).thenReturn(optionalBasket);

    // when
    Optional<Basket> expectedBasket = this.basketService.deleteProductFromBasket(basketId, productId);

    // then
    assertThat(expectedBasket.get().getProducts().size()).isEqualTo(0);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void deleteProductFromBasket_should_throw_exception_if_no_basket() {
    // given
    Long basketId = basket.getId();

    Optional<Product> optionalProduct = Optional
        .of(new Product(1L, "Test product", "details", new BigDecimal("20"), 20L, null));
    Long productId = optionalProduct.get().getId();

    Mockito.when(basketService.productRepository.findById(productId)).thenReturn(optionalProduct);
    Mockito.when(basketService.basketRepository.findById(basketId)).thenReturn(Optional.empty());

    // when
    this.basketService.deleteProductFromBasket(basketId, productId);

    // then throw exception
  }

  @Test(expected = ResourceNotFoundException.class)
  public void deleteProductFromBasket_should_not_delete_product_from_basket_if_no_product() {
    // given
    Long basketId = basket.getId();

    Optional<Product> optionalProduct = Optional
        .of(new Product(1L, "Test product", "details", new BigDecimal("20"), 20L, null));
    Long productId = optionalProduct.get().getId();

    Mockito.when(basketService.productRepository.findById(productId)).thenReturn(Optional.empty());
    Mockito.when(basketService.basketRepository.findById(basketId)).thenReturn(optionalBasket);

    // when
    this.basketService.deleteProductFromBasket(basketId, productId);

    // then throw exception
  }

  @Test(expected = ResourceNotFoundException.class)
  public void buyBasket_should_throw_exception_if_no_member_exist() {
    // given
    Long memberId = member.getId();
    Long basketId = basket.getId();

    Mockito.when(basketService.memberRepository.existsById(memberId)).thenReturn(false);

    // when
    Optional<Basket> expectedBasket = this.basketService.buyBasket(basketId, memberId);

    // then
    assertThat(expectedBasket).isEmpty();
  }

  @Test(expected = ResourceNotFoundException.class)
  public void buyBasket_should_throw_exception_if_no_basket_exist() {
    // given
    Long memberId = member.getId();
    Long basketId = basket.getId();

    Mockito.when(basketService.memberRepository.existsById(memberId)).thenReturn(true);
    Mockito.when(basketService.basketRepository.findByIdAndMemberId(basketId, memberId)).thenReturn(Optional.empty());

    // when
    Optional<Basket> expectedBasket = this.basketService.buyBasket(basketId, memberId);

    // then
    assertThat(expectedBasket).isEmpty();
  }

  @Test(expected = EntityNotAcceptableException.class)
  public void buyBasket_should_thorow_exception_if_satatus_is_not_acceptable() {
    // given
    Long memberId = member.getId();
    Long basketId = basket.getId();

    basket.setStatus(BasketStatus.PAYED);

    Mockito.when(basketService.memberRepository.existsById(memberId)).thenReturn(true);
    Mockito.when(basketService.basketRepository.findByIdAndMemberId(basketId, memberId)).thenReturn(optionalBasket);

    // when
    this.basketService.buyBasket(basketId, memberId);

    // then throw exception
  }

  @Test(expected = EntityNotAcceptableException.class)
  public void buyBasket_should_thorow_exception_if_no_product_in_basket() {
    // given
    Long memberId = member.getId();
    Long basketId = basket.getId();

    Mockito.when(basketService.memberRepository.existsById(memberId)).thenReturn(true);
    Mockito.when(basketService.basketRepository.findByIdAndMemberId(basketId, memberId)).thenReturn(optionalBasket);

    basket.setProducts(new HashSet<Product>());

    // when
    this.basketService.buyBasket(basketId, memberId);

    // then throw exception
  }

  @Test(expected = EntityNotAcceptableException.class)
  public void buyBasket_should_thorow_exception_if_product_out_stock() {
    // given
    Long memberId = member.getId();
    Long basketId = basket.getId();

    Mockito.when(basketService.memberRepository.existsById(memberId)).thenReturn(true);
    Mockito.when(basketService.basketRepository.findByIdAndMemberId(basketId, memberId)).thenReturn(optionalBasket);
    Optional<Product> optionalProduct = Optional
        .of(new Product(1L, "Test product", "details", new BigDecimal("20"), 20L, null));

    Product consumedProduct = optionalProduct.get();
    consumedProduct.setStockCount(0L);

    basket.getProducts().add(consumedProduct);

    // when
    this.basketService.buyBasket(basketId, memberId);

    // then throw exception
  }

  @Test(expected = PaymentFailedException.class)
  public void buyBasket_should_thorow_exception_if_payment_failed() {
    // given
    Long memberId = member.getId();
    Long basketId = basket.getId();

    Mockito.when(basketService.memberRepository.existsById(memberId)).thenReturn(true);
    Mockito.when(basketService.basketRepository.findByIdAndMemberId(basketId, memberId)).thenReturn(optionalBasket);
    Optional<Product> optionalProduct = Optional
        .of(new Product(1L, "Test product", "details", new BigDecimal("20"), 20L, null));

    Product consumedProduct = optionalProduct.get();
    consumedProduct.setStockCount(2L);

    basket.getProducts().add(consumedProduct);

    Payment payment = new Payment();
    payment.setStatus("failed");

    Mockito.when(this.basketService.paymentService.pay(basket)).thenReturn(payment);

    // when
    this.basketService.buyBasket(basketId, memberId);

    // then throw exception
  }

  @Test
  public void buyBasket_should_update_stock_count_and_set_status_PAYED() {
    // given
    Long memberId = member.getId();
    Long basketId = basket.getId();

    Mockito.when(basketService.memberRepository.existsById(memberId)).thenReturn(true);
    Mockito.when(basketService.basketRepository.findByIdAndMemberId(basketId, memberId)).thenReturn(optionalBasket);
    Optional<Product> optionalProduct = Optional
        .of(new Product(1L, "Test product", "details", new BigDecimal("20"), 20L, null));

    Product consumedProduct = optionalProduct.get();
    consumedProduct.setStockCount(2L);

    basket.getProducts().add(consumedProduct);

    Payment payment = new Payment();
    payment.setStatus("success");

    Mockito.when(this.basketService.paymentService.pay(basket)).thenReturn(payment);

    // when
    this.basketService.buyBasket(basketId, memberId);

    // then
    assertThat(consumedProduct.getStockCount()).isEqualTo(1L);
    assertThat(basket.getStatus()).isEqualTo(BasketStatus.PAYED);
  }
}