package com.jml.coupon.api;

import com.jml.coupon.application.CreateCouponCommand;
import com.jml.coupon.application.CreateCouponHandler;
import com.jml.coupon.application.UseCouponCommand;
import com.jml.coupon.application.UseCouponHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    return useCouponHandler.handle(cmd, ip);
  }

  private String extractIp(HttpServletRequest request) {

    final String xf = request.getHeader("X-Forwarded-For");
    if (xf == null) {
      return request.getRemoteAddr();
    }

    return xf.split(",")[0];
  }
}