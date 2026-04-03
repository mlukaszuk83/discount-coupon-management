package com.jml.coupon.application;

public record CreateCouponCommand(String couponCode, int maxUses, String countryCode) {
}