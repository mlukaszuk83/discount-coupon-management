package com.jml.coupon.domain.exception;

public class CouponNotFoundException extends RuntimeException {

  public CouponNotFoundException() {
    super("Coupon with given code was not found");
  }
}