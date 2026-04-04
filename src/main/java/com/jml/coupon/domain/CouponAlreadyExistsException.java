package com.jml.coupon.domain;

public class CouponAlreadyExistsException extends RuntimeException {

  public CouponAlreadyExistsException() {
    super("A coupon with given code already exists");
  }
}