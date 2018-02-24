package com.foodBasket.net;


import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by programmer on 2017/5/11.
 */

public class BaseAction {
    private String mUrlName = "Basket/";
    private String mUrlName2="control/";
    private String mUrl = "http://120.78.254.71/";
//    protected Gson gson;
    protected Map<String, String> headers = new HashMap<>();


    public void setUrlName(String urlName){
        mUrlName = urlName;
    }
    public void setUrlName2(String urlName2){
        mUrlName2 = urlName2;
    }

    public BaseAction() {
//        gson = new GsonBuilder().serializeNulls().create();
    }

    public void postJsonRun(String url, BaseModel model, Callback callback) throws Exception {
        PostFormBuilder builder = OkHttpUtils.post();
        builder.url(mUrl + mUrlName+mUrlName2 + url);
        builder.headers(headers);
        String json = JSON.toJSONString(model);

//        String json = gson.toJson(model);
//        //Aes加密
//        String code = AES.encode(json);
       builder.addParams("data",json);
        builder.build().execute(callback);
    }

    public void postRun(String method, List<ParamsBean> list,StringCallback callback){
        PostFormBuilder builder = OkHttpUtils.post();
        builder.url(mUrl + mUrlName+mUrlName2 + method);
        builder.headers(headers);
        Map<String,String> map = new HashMap<>();
        if(list.size() >0){
            for (ParamsBean bean: list) {
                map.put(bean.getKey(),bean.getValue());
            }
        }
        builder.params(map);
        builder.build().execute(callback);
    }

    public void getJsonRun(String url, BaseModel model, Callback callback) throws Exception {
//        //Aes加密
//        String json = gson.toJson(model);
//        String code = AES.encode(json);
        String json = JSON.toJSONString(model);
        GetBuilder builder = OkHttpUtils.get();
        builder.url(mUrl + mUrlName+mUrlName2 + url + "?" + getUTF8XMLString(json));
        builder.headers(headers);
        builder.build().execute(callback);
    }

    public static String getUTF8XMLString(String xml) {
        // A StringBuffer Object
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmString;
        String xmlUTF8 = "";
        try {
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
            System.out.println("utf-8 编码：" + xmlUTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return xmlUTF8;
    }

    /**
     * 下载文件
     *
     * @param url      文件下载地址
     * @param callBack
     */
    public void downFileRun(String url, FileCallBack callBack) {
        PostFormBuilder builder = OkHttpUtils.post();
        builder.url(url);
        builder.build().execute(callBack);
    }
}
