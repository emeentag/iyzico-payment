package com.iyzico.challenge.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.repository.BasketRepository;
import com.iyzico.challenge.repository.MemberRepository;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzipay.Options;
import com.iyzipay.model.Address;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;
import com.iyzipay.model.Currency;
import com.iyzipay.model.Locale;
import com.iyzipay.model.Payment;
import com.iyzipay.model.PaymentCard;
import com.iyzipay.model.PaymentChannel;
import com.iyzipay.model.PaymentGroup;
import com.iyzipay.request.CreatePaymentRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * BasketService
 */
@Slf4j
@Service
public class BasketService {

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

  public Optional<Basket> getBasket(Long basketId, Long memberId) {
    Optional<Member> member = this.memberRepository.findById(memberId);
    Optional<Basket> basket = this.basketRepository.findById(basketId);

    if (!isBasketBelongsToMember(member, basket)) {
      return Optional.empty();
    }

    return basket;
  }

  public Optional<Basket> addBasket(Long memberId) {
    Optional<Basket> basket = Optional.empty();
    Optional<Member> member = this.memberRepository.findById(memberId);

    if (member.isPresent()) {
      Member memberEntity = member.get();

      Basket basketEntity = new Basket();
      basketEntity.setMember(memberEntity);
      this.basketRepository.save(basketEntity);

      basket = Optional.of(basketEntity);
    } else {
      log.info("There is no member with this id!");
    }

    return basket;
  }

  public Optional<Basket> addProductToBasket(Long basketId, Long productId) {
    Optional<Basket> basket = this.basketRepository.findById(basketId);

    if (basket.isPresent()) {
      Optional<Product> product = this.productRepository.findById(productId);

      if (product.isPresent()) {
        Basket basketEntity = basket.get();

        basketEntity.getProducts().add(product.get());
        this.basketRepository.save(basketEntity);
      } else {
        log.info("There is no product with this id!");
      }
    } else {
      log.info("There is no basket with this id!");
    }

    return basket;
  }

  public Optional<Basket> deleteProductFromBasket(Long basketId, Long productId) {
    Optional<Basket> basket = this.basketRepository.findById(basketId);

    if (basket.isPresent()) {
      Optional<Product> product = this.productRepository.findById(productId);

      if (product.isPresent()) {
        Basket basketEntity = basket.get();

        basketEntity.getProducts().remove(product.get());
        this.basketRepository.save(basketEntity);
      } else {
        log.info("There is no product with this id!");
      }
    } else {
      log.info("There is no basket with this id!");
    }

    return basket;
  }

  @Transactional(rollbackFor = Exception.class)
  public Optional<Basket> buyBasket(Long basketId, Long memberId) {

    Optional<Member> member = this.memberRepository.findById(memberId);
    Optional<Basket> basket = this.basketRepository.findById(basketId);

    if (isBasketBelongsToMember(member, basket)) {

      Basket basketEntity = basket.get();

      if (basketEntity.getStatus().equals(BasketStatus.NOT_PAYED)) {
        Set<Product> products = basketEntity.getProducts();

        if (products.size() > 0) {
          if (inStock(products)) {

            // Purchase
            Payment payment = completePurchase(basketEntity);

            if (payment.getStatus().equalsIgnoreCase("success")) {
              
              // Update stock number.
              updateStock(products);

              // Close the basket.
              basketEntity.setStatus(BasketStatus.PAYED);
              this.basketRepository.save(basketEntity);
            } else {
              log.error("Payment failed with error" + payment.getErrorCode() + ":" + payment.getErrorMessage());
            }
          } else {
            log.error("Product out of stock.");
          }
        } else {
          log.error("There is no product in basket.");
        }
      }
    }

    return basket;
  }

  private Payment completePurchase(Basket basketEntity) {

    Options options = new Options();
    options.setApiKey("sandbox-Hx6DvxaWsTe8KkX9IixOCLhdye4YrzoA");
    options.setSecretKey("sandbox-SxBzSskY6zIg038BcqoUNzUxyVL1FnFU");
    options.setBaseUrl("https://sandbox-api.iyzipay.com");

    // This is just for a dummy payment.
    CreatePaymentRequest request = new CreatePaymentRequest();
    request.setLocale(Locale.TR.getValue());
    request.setConversationId("123456789");
    request.setPrice(new BigDecimal("1"));
    request.setPaidPrice(new BigDecimal("1.2"));
    request.setCurrency(Currency.TRY.name());
    request.setInstallment(1);
    request.setBasketId("B67832");
    request.setPaymentChannel(PaymentChannel.WEB.name());
    request.setPaymentGroup(PaymentGroup.PRODUCT.name());

    Set<Product> products = basketEntity.getProducts();
    List<BasketItem> basketItems = products.stream()
        .map(p -> this.productService.toBasketItem(p, BasketItemType.PHYSICAL)).collect(Collectors.toList());

    request.setBasketItems(basketItems);

    PaymentCard paymentCard = new PaymentCard();
    paymentCard.setCardHolderName("John Doe");
    paymentCard.setCardNumber("5528790000000008");
    paymentCard.setExpireMonth("12");
    paymentCard.setExpireYear("2030");
    paymentCard.setCvc("123");
    paymentCard.setRegisterCard(0);
    request.setPaymentCard(paymentCard);

    request.setBuyer(this.memberService.toBuyer(basketEntity.getMember()));

    Address shippingAddress = new Address();
    shippingAddress.setContactName("Jane Doe");
    shippingAddress.setCity("Istanbul");
    shippingAddress.setCountry("Turkey");
    shippingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
    shippingAddress.setZipCode("34742");
    request.setShippingAddress(shippingAddress);

    Address billingAddress = new Address();
    billingAddress.setContactName("Jane Doe");
    billingAddress.setCity("Istanbul");
    billingAddress.setCountry("Turkey");
    billingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
    billingAddress.setZipCode("34742");
    request.setBillingAddress(billingAddress);

    Payment payment = Payment.create(request, options);

    return payment;
  }

  private Set<Product> updateStock(Set<Product> products) {
    for (Product product : products) {
      Long stockCount = product.getStockCount();

      if (stockCount > 0) {
        product.setStockCount(stockCount - 1);
      }
    }

    this.productRepository.saveAll(products);

    return products;
  }

  private boolean inStock(Set<Product> products) {
    for (Product product : products) {
      if (product.getStockCount() == 0) {
        return false;
      }
    }
    return true;
  }

  private boolean isBasketBelongsToMember(Optional<Member> member, Optional<Basket> basket) {
    if (member.isPresent() && basket.isPresent()) {

      Basket basketEntity = basket.get();
      Member memberEntity = member.get();

      if (!memberEntity.equals(basketEntity.getMember())) {
        return true;
      } else {
        log.info("This basket does not belong to that member!");
      }
    } else {
      log.info("There is no basket or member with these ids!");
    }

    return false;
  }
}