package com.foodBasket.net;


import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;

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
    private String mUrlName = "users/";
//    private String mUrl = "http://1ij8925930.51mypc.cn:4000/";
    private String mUrl = "http://60.172.78.144:4000/";
    private String mToken = "21e44936eda6e949a26de7ccec3f7e4e";
//    protected Gson gson;
    protected Map<String, String> headers = new HashMap<>();


    public void setUrlName(String urlName){
        mUrlName = urlName;
    }

    public BaseAction() {
//        gson = new GsonBuilder().serializeNulls().create();
        headers.put("TOKEN", mToken);
    }

    public void postJsonRun(String url, BaseModel model, Callback callback) throws Exception {
        PostFormBuilder builder = OkHttpUtils.post();
        builder.url(mUrl + mUrlName + url);
        builder.headers(headers);
        String json = JSON.toJSONString(model);

//        String json = gson.toJson(model);
//        //Aes加密
//        String code = AES.encode(json);
       builder.addParams("data",json);
        builder.build().execute(callback);
    }

    public void getJsonRun(String url, BaseModel model, Callback callback) throws Exception {
//        //Aes加密
//        String json = gson.toJson(model);
//        String code = AES.encode(json);
        String json = JSON.toJSONString(model);
        GetBuilder builder = OkHttpUtils.get();
        builder.url(mUrl + mUrlName + url + "?" + getUTF8XMLString(json));
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
