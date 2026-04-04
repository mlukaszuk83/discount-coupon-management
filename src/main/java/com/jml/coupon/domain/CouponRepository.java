package com.jml.coupon.domain;

import java.util.Optional;

public interface CouponRepository {

  /**
   * Stores new Coupon
   * @param coupon to save
   */
  void save(Coupon coupon);

  /**
   * Fetches a coupon by given coupon code
   *
   * @param couponCode code of searched coupon
   * @return Optional containing coupon if found, empty Optional otherwise
   */
  Optional<Coupon> findByCode(CouponCode couponCode);

  /**
   * Increments current use count of the coupon
   *
   * @param couponCode code of the coupon
   * @return true if current use count was incremented, false otherwise
   */
  boolean incrementIfAvailable(CouponCode couponCode);
}