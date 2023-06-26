package com.concurrent.consumer.repository;

import com.concurrent.consumer.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
