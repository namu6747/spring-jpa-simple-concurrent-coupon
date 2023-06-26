package com.concurrent.api.service;

import com.concurrent.api.producer.CouponCreateProducer;
import com.concurrent.api.repository.AppliedUserRepository;
import com.concurrent.api.repository.CouponCountRepository;
import com.concurrent.api.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final AppliedUserRepository appliedUserRepository;
    private final CouponCreateProducer couponCreateProducer;

    public void apply(Long userId) {
        Long applied = appliedUserRepository.add(userId);

        if (applied != 1) {
            return;
        }

        Long count = couponCountRepository.increment();

        if (count > 100){
            return;
        }

        couponCreateProducer.create(userId);
    }
}
