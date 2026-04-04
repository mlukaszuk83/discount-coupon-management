package com.jml.coupon.api;

import com.jml.coupon.application.CreateCouponCommand;
import com.jml.coupon.application.CreateCouponHandler;
import com.jml.coupon.application.UseCouponCommand;
import com.jml.coupon.application.UseCouponHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

  private final CreateCouponHandler createCouponHandler;
  private final UseCouponHandler useCouponHandler;

  @PostMapping
  public String create(@RequestBody CreateCouponCommand cmd) {
    createCouponHandler.handle(cmd);
    return "CREATED";
  }

  @PostMapping("/use")
  public String use(@RequestBody UseCouponCommand cmd, HttpServletRequest request) {
    final String ip = extractIp(request);
    useCouponHandler.handle(cmd, ip);
    return "SUCCESS";
  }

  private String extractIp(HttpServletRequest request) {

    final String xf = request.getHeader("X-Forwarded-For");
    if (StringUtils.isBlank(xf)) {
      return request.getRemoteAddr();
    }

    return xf.split(",")[0];
  }
}