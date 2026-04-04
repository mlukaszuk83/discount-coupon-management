package com.jml.coupon.domain.exception;

public class EmptyCouponCodeException extends RuntimeException {

  public EmptyCouponCodeException() {
    super("Coupon code cannot be empty");
  }
}