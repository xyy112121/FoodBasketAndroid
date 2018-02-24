package com.foodBasket.core.main.model;

import com.foodBasket.net.ResponseBean;

import java.util.ArrayList;

/**
 * 特惠商品
 */

public class ProductDiscountModelRes extends ResponseBean {

    public int total;
    public ArrayList<ProductListModel> rows;
}
