package com.foodBasket.core.main.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * Created by programmer on 2018/2/24.
 */

public class UserResModel extends ResponseBean {


    public int waitingReceive;
    public User user;
    public List<Merchant> merchant;

    public  class User {
        public String userLogin;
        public String realName;
        public String address;
        public int old;
        public int sex;
        public String mobile;
        public String displaySex;
        public String id;
        public String avatar;
    }

    public  class Merchant {
        public String name;
        public String id;
    }
}
