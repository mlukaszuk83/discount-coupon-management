package com.jml.coupon.domain;

import java.time.Instant;

public class Coupon {

  private CouponCode code;
  private Country country;
  private int maxUses;
  private int currentUses;
  private Instant createdAt;

  public Coupon(CouponCode code, Country country, int maxUses) {
    this.code = code;
    this.country = country;
    this.maxUses = maxUses;
    this.currentUses = 0;
    this.createdAt = Instant.now();
  }
}