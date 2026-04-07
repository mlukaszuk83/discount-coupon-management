package com.jml.coupon.api.controller;

import com.jml.coupon.application.dto.CouponDto;
import com.jml.coupon.application.handler.CreateCouponHandler;
import com.jml.coupon.application.request.CreateCouponRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/coupons")
@RequiredArgsConstructor
class CouponController {

  private final CreateCouponHandler createCouponHandler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  CouponDto create(@RequestBody CreateCouponRequest request) {
    return createCouponHandler.handle(request);
  }
}