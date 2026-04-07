package com.jml.coupon.domain;

import com.jml.coupon.domain.model.CouponUsage;

public interface CouponUsageRepository {

  /**
   * Checks if given coupon was already used by user with given id
   *
   * @param couponUsage coupon usage to check
   * @return true if coupon was already used by user
   */
  boolean exists(CouponUsage couponUsage);

  /**
   * Stores information about usage of the coupon by user
   *
   * @param couponUsage coupon used by user
   * @return id of the created coupon usage
   */
  Long save(CouponUsage couponUsage);
}