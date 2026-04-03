package com.jml.coupon.domain;

public record Country(String code) {

  public Country {
    code = code.toUpperCase();
  }
}