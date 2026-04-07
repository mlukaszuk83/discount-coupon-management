package com.jml.coupon.infrastructure.geolocation;

import com.jml.coupon.domain.model.Country;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
record IpApiResponse(String query, String status, String message, String countryCode) {

  Optional<Country> toCountry() {

    if ("fail".equals(status)) {
      log.warn("Ip Api responded with fail result: {}", this);
      return Optional.empty();
    }

    return Optional.of(new Country(countryCode));
  }
}