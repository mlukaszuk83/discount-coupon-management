package com.jml.coupon.domain;

public interface CouponRepository {

  /**
   * Saves new Coupon
   * @param coupon to save
   */
  void save(Coupon coupon);
}