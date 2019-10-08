package com.iyzico.challenge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * ApplicationConfiguration
 */
@Configuration
@ConfigurationProperties(prefix = "spring.application")
@Data
public class ApplicationConfiguration {

    @Value("${spring.application.payment.apiKey}")
    private String paymentApiKey;

    @Value("${spring.application.payment.apiSecretKey}")
    private String paymentApiSecretKey;

    @Value("${spring.application.payment.apiUrl}")
    private String paymentApiUrl;

    @Value("${spring.application.itemsInSinglePage}")
    private Integer itemsInSinglePage;

}