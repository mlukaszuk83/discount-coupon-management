package com.jml.coupon.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

class CouponTest {

  @Test
  void constructor_shouldCreateCouponWithDefaultDataSet() {

    // given
    CouponCode couponCode = new CouponCode("code");
    Country country = new Country("country");
    Instant before = Instant.now();

    // when
    Coupon result = new Coupon(couponCode, country, 4);

    // then
    Instant after = Instant.now();
    assertThat(result.getId()).isNull();
    assertThat(result.getCode()).isEqualTo(couponCode);
    assertThat(result.getCountry()).isEqualTo(country);
    assertThat(result.getMaxUses()).isEqualTo(4);
    assertThat(result.getCurrentUses()).isZero();
    assertThat(result.getCreatedAt()).isBetween(before, after);
  }

  @Test
  void validateCountry_givenTestedCountryIsSameAsCouponCountry_thenExceptionIsNotThrown() {

    // given
    Country country = new Country("same-code");
    Coupon coupon = new Coupon(null, new Country("same-code"), 2);

    // when
    coupon.validateCountry(country);

    // then
    assertThatNoException();
  }

  @Test
  void validateCountry_givenTestedCountryIsDifferentThanTheCouponCountry_thenInvalidCountryExceptionIsThrown() {

    // given
    Country country = new Country("code");
    Coupon coupon = new Coupon(null, new Country("different-code"), 2);

    // when / then
    assertThatExceptionOfType(InvalidCountryException.class).isThrownBy(() -> coupon.validateCountry(country))
        .withMessage("Given coupon country is different than user country of origin");
  }
}