package com.foodBasket.net;

/**
 * 返回值
 */

public class ResponseBean {
    private String resultInfo;
    private String success;

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getResultInfo() {
        return resultInfo;
    }


    public boolean getSuccess() {
        if("true".equals(success)){
            return true;
        }
        return false;
    }


}
