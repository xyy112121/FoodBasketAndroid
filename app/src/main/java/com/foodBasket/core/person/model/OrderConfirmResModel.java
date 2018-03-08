package com.foodBasket.core.person.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * 确认订单
 */

public class OrderConfirmResModel extends ResponseBean {

    public Double couponPayPrice;//优惠金额
    public AddressResModel address;
    public Double totalPrice;//总支付
    public Double realPayPrice;//实际支付
    public Double deliveryPrice;//配送费
    public List<Products> products;

    public  class Products {
        public String headPicture;
        public Double sumPrice;
        public Double salePrice;
        public String name;
        public int count;
        public String id;
    }
}
