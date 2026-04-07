package com.jml.coupon;

import com.jml.coupon.application.GeoService;
import com.jml.coupon.domain.model.Country;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

  @Bean
  GeoService geoService() {
    return ip -> new Country("PL");
  }
}