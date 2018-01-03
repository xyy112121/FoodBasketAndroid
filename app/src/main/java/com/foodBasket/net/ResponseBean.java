package com.foodBasket.net;

/**
 * Created by programmer on 2017/12/20.
 */

public class ResponseBean {
    private String code;
    private String msg;
    public Object data;

    public String getErrorcode() {
        return code;
    }

    public void setErrorcode(String errorcode) {
        this.code = errorcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
