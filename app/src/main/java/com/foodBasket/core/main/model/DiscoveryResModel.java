package com.foodBasket.core.main.model;

import com.foodBasket.net.ResponseBean;

/**
 * 发现开关
 */

public class DiscoveryResModel extends ResponseBean {

    public Discovery discovery;

    public  class Discovery {
        public String isDiscovery;
        public String url;
    }
}
