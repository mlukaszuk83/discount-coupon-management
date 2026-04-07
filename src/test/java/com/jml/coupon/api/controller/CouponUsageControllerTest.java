package com.jml.coupon.api.controller;

import com.jml.coupon.application.dto.CouponUsageDto;
import com.jml.coupon.application.handler.UseCouponHandler;
import com.jml.coupon.application.request.UseCouponRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CouponUsageControllerTest {

  private final UseCouponHandler useCouponHandler = mock(UseCouponHandler.class);
  private final CouponUsageController systemUnderTest = new CouponUsageController(useCouponHandler);

  @Test
  void use_givenRequest_shouldUseHandler_thenReturnDto() {

    // given
    UseCouponRequest request = mock(UseCouponRequest.class);
    CouponUsageDto dto = mock(CouponUsageDto.class);
    when(useCouponHandler.handle(eq(request), any())).thenReturn(dto);

    String ip = "some-ip-address";
    HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    when(servletRequest.getRemoteAddr()).thenReturn(ip);

    // when
    CouponUsageDto result = systemUnderTest.use(request, servletRequest);

    // then
    assertThat(result).isSameAs(dto);
    verify(useCouponHandler).handle(request, ip);
  }
}