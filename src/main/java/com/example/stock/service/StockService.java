package com.example.stock.service;

import com.example.stock.entity.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        // get stock
        // 재고감소
        // 저장

        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.save(stock);
    }

    //synchronized 사용해도 동시성 처리 실패
    //@Transactional 동작 구조 때문에 실패, 주석해야 성공
    //하지만 이렇게 되면 서버가 여러개 일때는 문제가 발생한다!
    public synchronized void decrease2(Long id, Long quantity) {
        // get stock
        // 재고감소
        // 저장

        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.save(stock);
    }

}
