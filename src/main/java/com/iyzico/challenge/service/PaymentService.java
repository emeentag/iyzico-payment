package com.iyzico.challenge.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.iyzico.challenge.configuration.ApplicationConfiguration;
import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Product;
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

/**
 * PaymentService
 */
@Service
public class PaymentService {

  @Autowired
  public MemberService memberService;

  @Autowired
  public ProductService productService;

  @Autowired
  public ApplicationConfiguration applicationConfiguration;

  public Payment pay(Basket basketEntity) {
    Options options = new Options();
    options.setApiKey(this.applicationConfiguration.getPaymentApiKey());
    options.setSecretKey(this.applicationConfiguration.getPaymentApiSecretKey());
    options.setBaseUrl(this.applicationConfiguration.getPaymentApiUrl());

    Set<Product> products = basketEntity.getProducts();
    List<BasketItem> basketItems = products.stream()
        .map(p -> this.productService.toBasketItem(p, BasketItemType.PHYSICAL)).collect(Collectors.toList());

    BigDecimal totalPrice = products.stream().map(p -> p.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);

    // This is just for a dummy payment.
    CreatePaymentRequest request = new CreatePaymentRequest();
    request.setLocale(Locale.TR.getValue());
    request.setConversationId("123456789");
    request.setPrice(totalPrice);
    request.setPaidPrice(totalPrice);
    request.setCurrency(Currency.TRY.name());
    request.setInstallment(1);
    request.setBasketId("B67832");
    request.setPaymentChannel(PaymentChannel.WEB.name());
    request.setPaymentGroup(PaymentGroup.PRODUCT.name());

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

    return Payment.create(request, options);
  }
}