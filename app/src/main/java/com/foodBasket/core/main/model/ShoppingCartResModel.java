package com.foodBasket.core.main.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * 购物车
 */

public class ShoppingCartResModel extends ResponseBean {

    public int totalPrice;
    public List<ShoppingCartRowsModel> rows;
}
