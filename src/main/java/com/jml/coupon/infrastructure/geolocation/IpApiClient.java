package com.jml.coupon.infrastructure.geolocation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
class IpApiClient {

  private static final String URL = "http://ip-api.com/json";
  private static final String QUERY_PATTERN = "/%s?fields=query,status,message,countryCode";

  private final RestClient client;

  IpApiClient(RestClient.Builder builder) {
    this.client = builder.baseUrl(URL)
        .build();
  }

  IpApiResponse call(String ip) {
    final String parsedIp = parseIp(ip);
    return client.get()
        .uri(QUERY_PATTERN.formatted(parsedIp))
        .retrieve()
        .body(IpApiResponse.class);
  }

  private String parseIp(String ip) {
    return StringUtils.trimToEmpty(ip)
        .replace("[", "")
        .replace("]", "");
  }
}