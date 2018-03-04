package com.foodBasket.core.person.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * Created by programmer on 2018/3/3.
 */

public class DeliveryListResModel extends ResponseBean {

    public int total;
    public List<Rows> rows;

    public class Rows {
        public String order_userAddress_city;
        public String order_userAddress_userName;
        public String deliveryTime;
        public String order_disDeliveryStateForDeliveryUser;
        public String order_userAddress_province;
        public int order_deliveryState;
        public String order_userAddress_mobile;
        public String order_id;
        public String order_orderNo;
        public String order_userAddress_county;
        public String order_userAddress_address;
    }
}
