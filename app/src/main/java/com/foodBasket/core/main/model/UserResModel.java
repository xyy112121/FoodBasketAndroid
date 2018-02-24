package com.foodBasket.core.main.model;

import com.foodBasket.net.ResponseBean;

/**
 * Created by programmer on 2018/2/24.
 */

public class UserResModel extends ResponseBean {

    public int waitingReceive;
    public User user;

    public class User {
        public String mobile;
        public String niceName;
        public String avatar;
    }
}
