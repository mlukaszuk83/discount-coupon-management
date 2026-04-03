CREATE TABLE coupon_usage (
    id BIGSERIAL PRIMARY KEY,
    coupon_id BIGINT NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    used_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_coupon
      FOREIGN KEY (coupon_id)
          REFERENCES coupons(id)
          ON DELETE CASCADE
);

CREATE UNIQUE INDEX unique_coupon_user_index ON coupon_usage (coupon_id, user_id);