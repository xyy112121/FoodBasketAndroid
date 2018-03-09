package com.foodBasket.core.person.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * 订单列表
 */

public class OrderListResModel extends ResponseBean {


    public int total;
    public int wallet;
    public List<Rows> rows;

    public static class Rows {
        public int couponPay;
        public String orderNo;
        public int realPay;
        public int isPay;
        public int totalPrice;
        public int debtPrice;
        public int deliveryState;
        public String id;
        public String disDeliveryState;
        public String displayIsPay;
        public int productCount;
        public List<Products> products;

        public static class Products {
            public int buyNumber;
            public int totalPrice;
            public String productBasic_name;
            public String productBasic_id;
            public int productBasic_salePrice;
            public String productBasic_headPicture;
        }
    }
}
