package com.jml.coupon.domain.model;

import com.jml.coupon.domain.exception.InvalidCountryException;
import lombok.Data;

import java.time.Instant;
import java.util.Objects;

@Data
public class Coupon {

  private Long id;
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

  public void validateCountry(Country country) {

    if (!Objects.equals(this.country, country)) {
      throw new InvalidCountryException();
    }
  }
}