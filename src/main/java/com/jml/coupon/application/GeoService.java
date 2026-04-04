package com.jml.coupon.application;

import com.jml.coupon.domain.model.Country;

public interface GeoService {

  /**
   * Retrieves country information by given ip address
   *
   * @param ip address to check
   * @return country
   */
  Country resolve(String ip);
}