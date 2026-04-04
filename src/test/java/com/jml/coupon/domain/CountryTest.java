package com.jml.coupon.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CountryTest {

  @Test
  void constructor_givenNonEmptyCode_shouldConvertCodeToUpperCase() {

    // given
    String code = "lower-case-code";

    // when
    Country result = new Country(code);

    // then
    assertThat(result.code()).isEqualTo("LOWER-CASE-CODE");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void constructor_givenEmptyCode_shouldThrowEmptyCountryException(String code) {

    // when / then
    assertThatExceptionOfType(EmptyCountryException.class).isThrownBy(() -> new Country(code))
        .withMessage("Country code cannot be empty");
  }
}