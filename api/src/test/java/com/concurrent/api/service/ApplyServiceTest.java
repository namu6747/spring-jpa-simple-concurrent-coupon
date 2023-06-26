package com.concurrent.api.service;

import com.concurrent.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplyServiceTest {

    @Autowired
    ApplyService applyService;

    @Autowired
    CouponRepository couponRepository;

    @Test
    public void apply_one() {
        applyService.apply(1L);

        long count = couponRepository.count();

        assertThat(count).isEqualTo(1L);
    }

    Consumer basicConsumer = (id) -> applyService.apply((Long) id);

    @Test
    public void apply_many_times_simultaneously() throws Exception {

        final int TASK_COUNT = 1000;
        final int THREAD_COUNT = 16;
        final var executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        final var latch = new CountDownLatch(TASK_COUNT);

        IntStream.range(0, TASK_COUNT).forEach(i -> {
            final long userId = 1L;
            executorService.submit(() -> {
                try {
                    basicConsumer.accept(userId);
                } finally {
                    latch.countDown();
                }
            });
        });

        latch.await();

        Thread.sleep(5000);

        long count = couponRepository.count();
        assertThat(count).isEqualTo(1L);
    }

}