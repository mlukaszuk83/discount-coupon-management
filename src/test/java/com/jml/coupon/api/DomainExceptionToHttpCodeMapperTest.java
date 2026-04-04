package com.jml.coupon.api;

import com.jml.coupon.domain.exception.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DomainExceptionToHttpCodeMapperTest {

  private final DomainExceptionToHttpCodeMapper systemUnderTest = new DomainExceptionToHttpCodeMapper();

  @ParameterizedTest
  @MethodSource("dataSource")
  void givenDomainException_shouldReturnExpectedHttpStatus(DomainException exception, HttpStatus expectedStatus) {

    // when
    HttpStatus result = systemUnderTest.toStatus(exception);

    // then
    assertThat(result).isEqualTo(expectedStatus);
  }

  private static Stream<Arguments> dataSource() {
    return Stream.of(Arguments.of(new CouponAlreadyExistsException(), HttpStatus.CONFLICT),
        Arguments.of(new CouponAlreadyUsedException(), HttpStatus.CONFLICT),
        Arguments.of(new CouponLimitReachedException(), HttpStatus.CONFLICT),
        Arguments.of(new CouponNotFoundException(), HttpStatus.NOT_FOUND),
        Arguments.of(new EmptyCouponCodeException(), HttpStatus.BAD_REQUEST),
        Arguments.of(new EmptyCountryException(), HttpStatus.BAD_REQUEST),
        Arguments.of(new InvalidCountryException(), HttpStatus.FORBIDDEN));
  }
}