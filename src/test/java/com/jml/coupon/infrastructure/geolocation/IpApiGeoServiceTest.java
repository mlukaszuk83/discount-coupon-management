package com.jml.coupon.infrastructure.geolocation;

import com.jml.coupon.domain.model.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IpApiGeoServiceTest {

  private IpApiClient ipApiClient;
  private IpApiGeoService systemUnderTest;

  @BeforeEach
  void setup() {
    ipApiClient = mock(IpApiClient.class);
    systemUnderTest = new IpApiGeoService(ipApiClient);
  }

  @Test
  void resolve_givenIpApiClientSuccessResponse_thenExpectedCuntryIsReturned() {

    // given
    String ip = "proper-ip-address";
    String countryCode = "PL";
    IpApiResponse ipApiResponse = new IpApiResponse(ip, "success", null, countryCode);

    when(ipApiClient.call(ip)).thenReturn(ipApiResponse);

    // when
    Country result = systemUnderTest.resolve(ip);

    // then
    assertThat(result).extracting(Country::code)
        .isEqualTo(countryCode);
  }

  @Test
  void resolve_givenIpApiClientFailResponse_thenNullIsReturned() {

    // given
    String ip = "invalid-ip-address";
    IpApiResponse ipApiResponse = new IpApiResponse(ip, "fail", "private_range", null);

    when(ipApiClient.call(ip)).thenReturn(ipApiResponse);

    // when
    Country result = systemUnderTest.resolve(ip);

    // then
    assertThat(result).isNull();
  }
}