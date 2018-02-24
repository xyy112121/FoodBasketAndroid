package com.foodBasket.core.main.model;

/**
 * Created by programmer on 2017/12/27.
 */

public class OrderDetailid {
    private String productId;
    private int count;

    public OrderDetailid(String id, int count) {
        this.productId = id;
        this.count = count;

    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProductId() {
        return productId;
    }
}
