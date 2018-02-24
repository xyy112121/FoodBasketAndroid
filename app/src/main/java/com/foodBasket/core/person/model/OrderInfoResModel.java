package com.foodBasket.core.person.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * 订单详情
 */

public class OrderInfoResModel extends ResponseBean {

    public String orderNo;
    public int isPay;
    public String couponPayPrice;//优惠金额
    public AddressResModel address;
    public String deliveryTime;
    public String totalPrice;//总支付
    public String realPayPrice;//实际支付
    public String deliveryPrice;//配送费
    public String disDeliveryState;
    public String displayIsPay;
    public int productCount;
    public String displayPayChannel;
    public int deliveryState;
    public int payChannel;
    public String id;
    public List<Products> products;

    public static class Address {
        public String address;
        public String province;
        public String city;
        public String mobile;
        public String county;
        public String id;
        public String userName;
    }

    public static class Products {
        public String headPicture;
        public String sumPrice;
        public String salePrice;
        public String name;
        public int count;
        public String id;
    }
}
