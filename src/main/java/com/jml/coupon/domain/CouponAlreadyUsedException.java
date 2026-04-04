package com.jml.coupon.domain;

public class CouponAlreadyUsedException extends RuntimeException {

  public CouponAlreadyUsedException() {
    super("Coupon with given code was already used by given user");
  }
}