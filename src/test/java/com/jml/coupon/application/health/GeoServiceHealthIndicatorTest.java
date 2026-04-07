package com.jml.coupon.application.health;

import com.jml.coupon.application.GeoService;
import com.jml.coupon.domain.model.Country;
import org.junit.jupiter.api.Test;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeoServiceHealthIndicatorTest {

  private final GeoService geoService = mock(GeoService.class);
  private final GeoServiceHealthIndicator systemUnderTest = new GeoServiceHealthIndicator(geoService);

  @Test
  void health_givenGeoServiceIsUp_thenHealthWithStatusUpIsReturned() {

    // given
    Country country = mock(Country.class);
    when(geoService.resolve("8.8.8.8")).thenReturn(country);

    // when
    Health result = systemUnderTest.health();

    // then
    assertThat(result).extracting(Health::getStatus, Health::getDetails)
        .containsExactly(Status.UP, Map.of("geoService", "available"));
  }

  @Test
  void health_givenGeoServiceIsDown_thenHealthWithStatusDownIsReturned() {

    // given
    when(geoService.resolve("8.8.8.8")).thenThrow(HttpServerErrorException.ServiceUnavailable.class);

    // when
    Health result = systemUnderTest.health();

    // then
    assertThat(result).extracting(Health::getStatus, Health::getDetails)
        .containsExactly(Status.DOWN,
            Map.of("geoService", "unavailable", "error", "org.springframework.web.client.HttpServerErrorException$ServiceUnavailable: null"));
  }
}