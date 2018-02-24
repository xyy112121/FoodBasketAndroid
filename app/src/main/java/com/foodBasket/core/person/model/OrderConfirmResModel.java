package com.foodBasket.core.person.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * 确认订单
 */

public class OrderConfirmResModel extends ResponseBean {

    public String couponPayPrice;//优惠金额
    public AddressResModel address;
    public String totalPrice;//总支付
    public String realPayPrice;//实际支付
    public String deliveryPrice;//配送费
    public List<Products> products;

    public  class Products {
        public String headPicture;
        public String sumPrice;
        public int salePrice;
        public String name;
        public int count;
        public String id;
    }
}
