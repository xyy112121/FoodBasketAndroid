package com.foodBasket.core.person.model;

import com.foodBasket.net.ResponseBean;

/**
 * 餐馆详情
 */

public class MerchantInfoResModel  extends ResponseBean {

    public Merchant merchant;

    public  class Merchant {
        public String license;
        public String displayState;
        public String address;
        public String name;
        public String cardReverse;
        public String id;
        public int state;
        public String cardPositive;
    }
}
