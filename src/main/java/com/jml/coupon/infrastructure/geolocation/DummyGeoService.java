package com.jml.coupon.infrastructure.geolocation;

import com.jml.coupon.application.GeoService;
import com.jml.coupon.domain.model.Country;
import org.springframework.stereotype.Component;

@Component
class DummyGeoService implements GeoService {

  @Override
  public Country resolve(String ip) {
    return new Country("PL");
  }
}