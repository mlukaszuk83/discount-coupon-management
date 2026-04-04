package com.jml.coupon.domain;

import org.apache.commons.lang3.StringUtils;

public record Country(String code) {

  public Country {

    code = StringUtils.trimToNull(code);
    if (code == null) {
      throw new EmptyCountryException();
    }
    code = code.toUpperCase();
  }
}