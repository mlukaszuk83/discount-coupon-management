package com.jml.coupon.infrastructure;

import com.jml.coupon.application.GeoService;
import com.jml.coupon.domain.Country;
import org.springframework.stereotype.Component;

@Component
class DummyGeoService implements GeoService {

  @Override
  public Country resolve(String ip) {
    return new Country("PL");
  }
}