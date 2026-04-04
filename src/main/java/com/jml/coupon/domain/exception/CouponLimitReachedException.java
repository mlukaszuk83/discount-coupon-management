package com.jml.coupon.domain.exception;

public class CouponLimitReachedException extends RuntimeException {

  public CouponLimitReachedException() {
    super("Coupon can't be used anymore, the usage limit has been reached");
  }
}