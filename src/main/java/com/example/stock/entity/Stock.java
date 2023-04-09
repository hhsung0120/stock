package com.example.stock.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private Long quantity;

    public Stock() {
    }

    public Stock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Stock(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void decrease(Long quantity) {
        long result = this.quantity - quantity;
        if (result < 0) {
            throw new RuntimeException("foo");
        }

        this.quantity = result;
    }
}
