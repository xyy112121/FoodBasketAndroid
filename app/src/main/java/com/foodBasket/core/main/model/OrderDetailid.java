package com.foodBasket.core.main.model;

/**
 * Created by programmer on 2017/12/27.
 */

public class OrderDetailid {
    private String orderdetailid;
    private int ordernumber;
    private String url;

    public OrderDetailid(String id, int number, String url) {
        this.orderdetailid = id;
        this.ordernumber = number;
        this.url = url;
    }

    public String getOrderdetailid() {
        return orderdetailid;
    }
}
