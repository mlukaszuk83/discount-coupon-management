package com.jml.coupon.domain.exception;

public class InvalidCountryException extends DomainException {

  public InvalidCountryException() {
    super(DomainExceptionCode.COUNTRY_INVALID, "The coupon cannot be used in this country");
  }
}