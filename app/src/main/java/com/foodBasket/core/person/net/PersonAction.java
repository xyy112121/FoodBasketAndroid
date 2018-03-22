package com.foodBasket.core.person.net;

import android.content.Context;

import com.foodBasket.net.BaseAction;
import com.foodBasket.net.ParamsBean;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
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
    public void userAddrAdd(Map<String, String> params, StringCallback callback) throws Exception {
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

    /**
     * 收货地址编辑
     */
    public void userAddrEdit(Map<String, String> params, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        Set<String> keySet = params.keySet();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String value = params.get(key);
            list.add(new ParamsBean(key, value));
        }
        setUrlName2("resource/");
        postRun("UserAddressForm_update_OL.action", list, callback);
    }

    /**
     * 收货地址添加
     */
    public void userAddrDelete(String id, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("objectID", id));
        setUrlName2("resource/");
        postRun("UserAddressForm_deleteEntity_OL.action", list, callback);
    }

    /**
     * 设置默认地址
     */
    public void setDefault(String id, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("objectID", id));
        setUrlName2("resource/");
        postRun("UserAddressNavigate_setDefault_OL.action", list, callback);
    }

    /**
     * 餐馆列表
     */
    public void merchantList(Context context, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("userId", userId));
        setUrlName2("control/");
        postRun("MerchantNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 添加新餐馆信息
     * userId name address files
     */
    public void merchantEdit(Context context, String name, String address, List<File> files, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("userId", userId));
        list.add(new ParamsBean("name", name));
        list.add(new ParamsBean("address", address));
        setUrlName2("control/");
        postRun("MerchantForm_save_OL.action", list, files, callback);
    }

    /**
     * 获取餐馆信息
     * userId name address files
     */
    public void merchantInfo(String objectID, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("objectID", objectID));
        setUrlName2("control/");
        postRun("MerchantNavigate_detail_OL.action", list, callback);
    }


    /**
     * 上传头像
     * userId name address files
     */
    public void uploadAvatar(Context context, File file, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("objectID", userId));
        setUrlName2("control/");
        postRun("UserForm_uploadAvatar_OL.action", list, file, callback);
    }

    /**
     * 餐馆列表
     */
    public void couponList(Context context, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("userId", userId));
        list.add(new ParamsBean("isCoupon", 1 + ""));
        setUrlName2("business/");
        postRun("OrderNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 餐馆列表
     */
    public void getVersion(StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("phoneType", "android"));
        setUrlName2("control/");
        postRun("VersionNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 申请成为商户
     * objectID realName merchantName
     */
    public void merchantAdd(Context context, String realName, String merchantName, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("objectID", userId));
        list.add(new ParamsBean("realName", realName));
        list.add(new ParamsBean("merchantName", merchantName));
        setUrlName2("control/");
        postRun("UserForm_applyMerchant_OL.action", list, callback);
    }
}
