package com.foodBasket.core.person.net;

import android.content.Context;

import com.foodBasket.net.BaseAction;
import com.foodBasket.net.ParamsBean;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by programmer on 2018/2/23.
 */

public class PersonAction extends BaseAction {

    /**
     * 登录
     */
    public void login(String mobile, String code, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("mobile", mobile));
        list.add(new ParamsBean("code", code));
        setUrlName2("control/");
        postRun("UserNavigate_logIn_OL.action", list, callback);
    }

    /**
     * 发送验证码
     */
    public void sendCode(String mobile, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("mobile", mobile));
        setUrlName2("control/");
        postRun("SMSCodeNavigate_getSMSCode_OL.action", list, callback);
    }

    /**
     * 收货地址列表
     */
    public void userAddrList(Context context, int page, int rows, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("userId", userId));
        list.add(new ParamsBean("page", page + ""));
        list.add(new ParamsBean("rows", rows + ""));
        setUrlName2("resource/");
        postRun("UserAddressNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 收货地址添加
     */
    public void userAddrAdd( Map<String, String> params, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        Set<String> keySet = params.keySet();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String value = params.get(key);
            list.add(new ParamsBean(key, value));
        }
        setUrlName2("resource/");
        postRun("UserAddressForm_save_OL.action", list, callback);
    }
}
