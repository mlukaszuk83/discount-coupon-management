package com.jml.coupon.infrastructure.geolocation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(IpApiClient.class)
class IpApiClientTest {

  @Autowired
  private MockRestServiceServer server;

  @Autowired
  private IpApiClient systemUnderTest;

  @Test
  void call_shouldCallIpApiWebsite_thenReturnExpectedResponse() {

    // given
    String ip = "1.2.3.4";
    String expectedBody = """
        {
          "query" : "1.2.3.4",
          "status" : "success",
          "countryCode" : "PL"
        }
        """;
    server.expect(requestTo("http://ip-api.com/json/1.2.3.4?fields=query,status,message,countryCode"))
        .andRespond(withSuccess(expectedBody, MediaType.APPLICATION_JSON));

    // when
    IpApiResponse result = systemUnderTest.call(ip);

    // then
    assertThat(result).extracting(IpApiResponse::query, IpApiResponse::status, IpApiResponse::message, IpApiResponse::countryCode)
        .containsExactly(ip, "success", null, "PL");
  }

  @Test
  void call_givenIpAddressSurroundedWithBrackets_thenShouldRemoveThemAndReturnExpectedResponse() {

    // given
    String ip = "[2001:0db8::0001:0000]";
    String expectedBody = """
        {
          "query" : "2001:0db8::0001:0000",
          "status" : "success",
          "countryCode" : "PL"
        }
        """;
    server.expect(requestTo("http://ip-api.com/json/2001:0db8::0001:0000?fields=query,status,message,countryCode"))
        .andRespond(withSuccess(expectedBody, MediaType.APPLICATION_JSON));

    // when
    IpApiResponse result = systemUnderTest.call(ip);

    // then
    assertThat(result).extracting(IpApiResponse::query, IpApiResponse::status, IpApiResponse::message, IpApiResponse::countryCode)
        .containsExactly("2001:0db8::0001:0000", "success", null, "PL");
  }
}