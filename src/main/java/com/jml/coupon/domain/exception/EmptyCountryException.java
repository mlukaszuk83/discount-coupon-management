package com.jml.coupon.domain.exception;

public class EmptyCountryException extends RuntimeException {

  public EmptyCountryException() {
    super("Country code cannot be empty");
  }
}