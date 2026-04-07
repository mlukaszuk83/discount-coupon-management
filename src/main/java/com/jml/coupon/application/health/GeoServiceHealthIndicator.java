package com.jml.coupon.application.health;

import com.jml.coupon.application.GeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class GeoServiceHealthIndicator implements HealthIndicator {

  private final GeoService geoService;

  @Override
  public Health health() {

    try {
      geoService.resolve("8.8.8.8"); // test call
      return Health.up()
          .withDetail("geoService", "available")
          .build();

    } catch (Exception ex) {
      return Health.down()
          .withDetail("geoService", "unavailable")
          .withException(ex)
          .build();
    }
  }
}