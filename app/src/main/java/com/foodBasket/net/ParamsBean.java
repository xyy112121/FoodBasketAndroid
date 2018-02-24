package com.foodBasket.net;

/**
 * Created by programmer on 2018/2/23.
 */

public class ParamsBean {
    private String key;
    private String value;

    public ParamsBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
