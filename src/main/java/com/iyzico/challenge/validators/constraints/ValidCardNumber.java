package com.iyzico.challenge.validators.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.iyzico.challenge.validators.CardNumberValidator;

/**
 * ValidCardNumber
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE,ElementType.TYPE_PARAMETER,ElementType.TYPE_USE})
@Constraint(validatedBy = CardNumberValidator.class)
public @interface ValidCardNumber {

  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {}; 
}