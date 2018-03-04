package com.foodBasket.core.person.model;

import com.foodBasket.net.ResponseBean;

/**
 * 登录
 */

public class LoginResponseModel extends ResponseBean {

    public User user;

    // "user":{"userLogin":"222","address":"","old":0,"mobile":"222",
    // "displaySex":"保密","id":"1519397919719a1mq9h","niceName":"","avatar":""},

    public class User {
        public String userLogin;
        public String address;
        public int old;
        public String mobile;
        public String displaySex;
        public String id;
        public String niceName;
        public String avatar;
        public int userType;
    }
}
