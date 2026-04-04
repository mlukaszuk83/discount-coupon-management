package com.jml.coupon.domain;

public class InvalidCountryException extends RuntimeException {

  public InvalidCountryException() {
    super("Given coupon country is different than user country of origin");
  }
}