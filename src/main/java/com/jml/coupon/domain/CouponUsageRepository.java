package com.jml.coupon.domain;

import com.jml.coupon.domain.model.Coupon;
import com.jml.coupon.domain.model.UserId;

public interface CouponUsageRepository {

  /**
   * Checks if given coupon was already used by user with given id
   *
   * @param coupon coupon to check
   * @param userId id of user
   * @return true if coupon was already used by user
   */
  boolean exists(Coupon coupon, UserId userId);

  /**
   * Stores information about usage of the coupon by user
   *
   * @param coupon coupon used by user
   * @param userId id of the user
   */
  void save(Coupon coupon, UserId userId);
}