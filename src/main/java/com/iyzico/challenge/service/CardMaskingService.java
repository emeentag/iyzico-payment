package com.iyzico.challenge.service;

import com.iyzico.challenge.validator.CardNumberValidator;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CardMaskingService {

    public String maskCardNumber(String cardNumber) {

        CardNumberValidator cardNumberValidator = new CardNumberValidator();

        if (cardNumberValidator.isValidCardNumber(cardNumber)) {
            cardNumber = mask(cardNumber, cardNumberValidator.isFormattedCardNumber(cardNumber));
        } else {
            log.info("Card is not valid!");
        }
        return cardNumber;
    }

    private String mask(String cardNumber, boolean isFormatted) {
        StringBuilder sBuilder = new StringBuilder(cardNumber);

        int startIndex = isFormatted ? 7 : 6;
        int endIndex = cardNumber.length() - 4;
        String inBetween = sBuilder.substring(startIndex, endIndex);

        inBetween = inBetween.replaceAll("[0-9]", "*");

        sBuilder.replace(startIndex, endIndex, inBetween);

        return sBuilder.toString();
    }
}
