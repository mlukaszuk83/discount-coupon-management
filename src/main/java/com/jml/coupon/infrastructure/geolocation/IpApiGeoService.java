package com.jml.coupon.infrastructure.geolocation;

import com.jml.coupon.application.GeoService;
import com.jml.coupon.domain.model.Country;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"prod", "docker"})
@Component
@RequiredArgsConstructor
class IpApiGeoService implements GeoService {

  private final IpApiClient ipApiClient;

  @Override
  @Nullable
  public Country resolve(String ip) {
    return ipApiClient.call(ip)
        .toCountry()
        .orElse(null);
  }
}