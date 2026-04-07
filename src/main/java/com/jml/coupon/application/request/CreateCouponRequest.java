package com.jml.coupon.application.request;

public record CreateCouponRequest(String couponCode, String countryCode, int maxUses) {
}