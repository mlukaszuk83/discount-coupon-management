package com.jml.coupon.domain;

public class EmptyCountryException extends RuntimeException {

  public EmptyCountryException() {
    super("Country code cannot be empty");
  }
}