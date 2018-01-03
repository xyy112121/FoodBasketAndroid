package com.foodBasket.net;


import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by programmer on 2017/6/16.
 */

public abstract class MyStringCallBack extends StringCallback {


    @Override
    public void onError(Call call, Exception e, int id) {
        onResult(null);
    }

    @Override
    public void onResponse(String response, int id) {
        if (response == null || "".equals(response)) {
            onResult("error");
        } else {
            onResult(response);
//            String json = AES.decode(response);
//            Log.i("Tag",json);
//            ResponseBean result = JSON.parseObject(response,ResponseBean.class);
//            if(result.getErrorcode().equals("0")){//0替换为错误代码
//                onResult(result);
//            }else {
//                onResult(result);
//            }
        }

    }

    public abstract void onResult(String result);
}