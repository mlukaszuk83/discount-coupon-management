CREATE TABLE coupons (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    max_uses INT NOT NULL,
    current_uses INT NOT NULL,
    country VARCHAR(10) NOT NULL
);

CREATE UNIQUE INDEX unique_coupon_code_index ON coupons (LOWER(code));