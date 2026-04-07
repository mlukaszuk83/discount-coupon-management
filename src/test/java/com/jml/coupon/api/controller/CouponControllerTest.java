package com.jml.coupon.api.controller;

import com.jml.coupon.application.dto.CouponDto;
import com.jml.coupon.application.handler.CreateCouponHandler;
import com.jml.coupon.application.request.CreateCouponRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CouponControllerTest {

  private final CreateCouponHandler createCouponHandler = mock(CreateCouponHandler.class);
  private final CouponController systemUnderTest = new CouponController(createCouponHandler);

  @Test
  void create_givenRequest_shouldUseHandler_thenReturnDto() {

    // given
    CreateCouponRequest request = mock(CreateCouponRequest.class);
    CouponDto dto = mock(CouponDto.class);
    when(createCouponHandler.handle(request)).thenReturn(dto);

    // when
    CouponDto result = systemUnderTest.create(request);

    // then
    assertThat(result).isSameAs(dto);
  }
}