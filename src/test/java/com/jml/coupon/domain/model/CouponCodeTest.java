package com.jml.coupon.domain.model;

import com.jml.coupon.domain.exception.EmptyCouponCodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CouponCodeTest {

  @Test
  void constructor_givenNonEmptyCode_shouldConvertCodeToLowerCase() {

    // given
    String code = "UPPER-CASE-CODE";

    // when
    CouponCode result = new CouponCode(code);

    // then
    assertThat(result.value()).isEqualTo("upper-case-code");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void constructor_givenEmptyCode_shouldThrowEmptyCouponCodeException(String code) {

    // when / then
    assertThatExceptionOfType(EmptyCouponCodeException.class).isThrownBy(() -> new CouponCode(code))
        .withMessage("Coupon code cannot be empty");
  }

}