package com.jml.coupon.domain.model;

import com.jml.coupon.domain.exception.EmptyCountryException;
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