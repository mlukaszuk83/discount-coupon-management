package com.jml.coupon.application.command;

public record CreateCouponCommand(String couponCode, String countryCode, int maxUses) {
}