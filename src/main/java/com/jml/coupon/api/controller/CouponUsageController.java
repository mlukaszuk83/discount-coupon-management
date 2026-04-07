package com.jml.coupon.api.controller;

import com.jml.coupon.application.dto.CouponUsageDto;
import com.jml.coupon.application.handler.UseCouponHandler;
import com.jml.coupon.application.request.UseCouponRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/coupon-usages")
@RequiredArgsConstructor
public class CouponUsageController {

  private final UseCouponHandler useCouponHandler;

  @PostMapping
  CouponUsageDto use(@RequestBody UseCouponRequest request, HttpServletRequest servletRequest) {
    final String ip = extractIp(servletRequest);
    return useCouponHandler.handle(request, ip);
  }

  private String extractIp(HttpServletRequest request) {

    final String xf = request.getHeader("X-Forwarded-For");
    if (StringUtils.isBlank(xf)) {
      return request.getRemoteAddr();
    }

    return xf.split(",")[0];
  }
}