package com.iyzico.challenge.service;

import org.springframework.stereotype.Service;

@Service
public class CardMaskingService {

    public String maskCardNumber(String cardNumber) {

        boolean isFormatted = isFormattedCardNumber(cardNumber);

        boolean isValid = isValidCardNumber(cardNumber, isFormatted);

        if (isValid) {

            cardNumber = mask(cardNumber, isFormatted);
        }
        return cardNumber;
    }

    private boolean isValidCardNumber(String cardNumber, boolean isFormatted) {
        boolean isValid = false;
        int minLength = 15;
        int maxLength = 16;

        if (isFormatted) {
            cardNumber = cardNumber.replace("-", "");
        }

        if ((cardNumber.length() == minLength || cardNumber.length() == maxLength) && cardNumber.matches("^[0-9]*$")) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * If card number is not only numbers then it is formatted.
     * 
     * @param cardNumber
     * @return
     */
    private boolean isFormattedCardNumber(String cardNumber) {
        return !cardNumber.matches("^[0-9]*$");
    }

    private String mask(String cardNumber, boolean isFormatted) {
        StringBuilder sBuilder = new StringBuilder(cardNumber);

        int startIndex = isFormatted ? 7 : 6;
        int endIndex = cardNumber.length() - 4;
        String inBetween = sBuilder.substring(startIndex, endIndex);

        if (isFormatted) {
            inBetween = inBetween.replaceAll("[^-]", "*");

        } else {
            inBetween = inBetween.replaceAll("[0-9]", "*");
        }

        sBuilder.replace(startIndex, endIndex, inBetween);
        return sBuilder.toString();
    }
}
