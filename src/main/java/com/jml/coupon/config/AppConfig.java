package com.jml.coupon.config;

import com.jml.coupon.application.CreateCouponHandler;
import com.jml.coupon.application.GeoService;
import com.jml.coupon.application.UseCouponHandler;
import com.jml.coupon.domain.CouponRepository;
import com.jml.coupon.domain.CouponUsageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  CreateCouponHandler createCouponHandler(CouponRepository repo) {
    return new CreateCouponHandler(repo);
  }

  @Bean
  UseCouponHandler useCouponHandler(CouponRepository couponRepository, CouponUsageRepository couponUsageRepository, GeoService geoService) {
    return new UseCouponHandler(couponRepository, couponUsageRepository, geoService);
  }

}