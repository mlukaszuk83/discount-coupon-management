package com.jml.coupon.domain.exception;

public class EmptyCountryException extends DomainException {

  public EmptyCountryException() {
    super(DomainExceptionCode.COUNTRY_EMPTY, "Country code cannot be empty");
  }
}