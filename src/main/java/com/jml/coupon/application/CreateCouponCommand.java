package com.jml.coupon.application;

public record CreateCouponCommand(String couponCode, String countryCode, int maxUses) {
}