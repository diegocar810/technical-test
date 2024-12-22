package com.franchises.demo.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Product {

    @Id
    private String id;
    private String name;
    private Integer stock;

    public Product() {
        this.id = new ObjectId().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
