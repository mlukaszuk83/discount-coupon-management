package com.jml.coupon.config;

import com.jml.coupon.application.GeoService;
import com.jml.coupon.application.handler.CreateCouponHandler;
import com.jml.coupon.application.handler.UseCouponHandler;
import com.jml.coupon.domain.CouponRepository;
import com.jml.coupon.domain.CouponUsageRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
class AppConfig {

  @Bean
  CreateCouponHandler createCouponHandler(CouponRepository repo) {
    return new CreateCouponHandler(repo);
  }

  @Bean
  UseCouponHandler useCouponHandler(CouponRepository couponRepository, CouponUsageRepository couponUsageRepository, GeoService geoService) {
    return new UseCouponHandler(couponRepository, couponUsageRepository, geoService);
  }

  @Bean
  FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
    final FilterRegistrationBean<ForwardedHeaderFilter> filterRegBean = new FilterRegistrationBean<>();
    filterRegBean.setFilter(new ForwardedHeaderFilter());
    filterRegBean.setOrder(0); // to make sure it runs early
    return filterRegBean;
  }
}