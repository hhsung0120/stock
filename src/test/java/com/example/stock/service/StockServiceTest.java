package com.example.stock.service;

import com.example.stock.entity.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private PessimisticLockStockService pessimisticLockStockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        Stock stock = new Stock(1L, 100L);
        stockRepository.save(stock);
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    void stock_decrease() {
        stockService.decrease(1L, 1L);

        //100-1=99

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertThat(99L).isEqualTo(stock.getQuantity());
    }

    @Test
    void 동시에_100개_요청() throws InterruptedException {
        int threadCount = 100;

        //비동기로 실행하는 자바 API
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        //다른스레드에서 작업이 완료할때까지 기다려주도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        //레이스 컨디션 발생? 두개이상의 스레드에서 같은 값을 변경할때 생기는 현상
        assertThat(stock.getQuantity()).isEqualTo(0L);
    }

    //synchronized
    @Test
    void 동시에_100개_요청2() throws InterruptedException {
        int threadCount = 100;

        //비동기로 실행하는 자바 API
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        //다른스레드에서 작업이 완료할때까지 기다려주도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease2(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        //레이스 컨디션 발생? 두개이상의 스레드에서 같은 값을 변경할때 생기는 현상
        assertThat(stock.getQuantity()).isEqualTo(0L);
    }

    //PESSIMISTIC_LOCK
    //비관적 락, 충돌이 빈번하다면 낙관적보다 성능이 좋을 수 있음
    @Test
    void 동시에_100개_요청3() throws InterruptedException {
        int threadCount = 100;

        //비동기로 실행하는 자바 API
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        //다른스레드에서 작업이 완료할때까지 기다려주도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    pessimisticLockStockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        //레이스 컨디션 발생? 두개이상의 스레드에서 같은 값을 변경할때 생기는 현상
        assertThat(stock.getQuantity()).isEqualTo(0L);
    }

}