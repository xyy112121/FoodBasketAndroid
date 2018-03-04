package com.foodBasket.core.person.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * 餐馆列表
 */

public class MerchantListResModel extends ResponseBean {

    public int total;
    public List<MerchantRowModel> rows;

}
