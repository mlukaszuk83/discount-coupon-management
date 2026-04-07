package com.jml.coupon.domain.model;

import com.jml.coupon.domain.exception.EmptyUserIdException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CouponUsageTest {

  @Test
  void constructor_shouldCreateCouponUsageWithDefaultDataSet() {

    // given
    Coupon coupon = new Coupon(new CouponCode("code"), new Country("country"), 4);
    coupon.setId(2L);

    String userId = "user-id";
    Instant before = Instant.now();

    // when
    CouponUsage result = new CouponUsage(coupon, userId);

    // then
    Instant after = Instant.now();
    assertThat(result.getId()).isNull();
    assertThat(result.getCoupon()).isEqualTo(coupon);
    assertThat(result.getUserId()).isEqualTo(userId);
    assertThat(result.getCouponId()).isEqualTo(coupon.getId());
    assertThat(result.getUsedAt()).isBetween(before, after);
  }

  @Test
  void validateUser_givenUserIdIsNotEmpty_thenExceptionIsNotThrown() {

    // given
    CouponUsage couponUsage = new CouponUsage(mock(Coupon.class), "user-id");

    // when
    couponUsage.validateUser();

    // then
    assertThatNoException();
  }

  @ParameterizedTest
  @NullAndEmptySource
  void validateUser_givenUserIdIsEmpty_thenEmptyUserIdExceptionIsThrown(String userId) {

    // given
    CouponUsage couponUsage = new CouponUsage(mock(Coupon.class), userId);

    // when / then
    assertThatExceptionOfType(EmptyUserIdException.class).isThrownBy(couponUsage::validateUser)
        .withMessage("User id cannot be empty");
  }
}