package com.example.stock.repository;

import com.example.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LockRepository extends JpaRepository<Stock, Long> {

    @Query(value = "select GET_LOCK(:key, 3000)", nativeQuery = true)
    void getLock(String key);

    @Query(value = "select RELEASE_LOCK(:key)", nativeQuery = true)
    void releaseLock(String key);
}
