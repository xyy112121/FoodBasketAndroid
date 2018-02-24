package com.foodBasket.core.person.net;

import android.content.Context;

import com.foodBasket.net.BaseAction;
import com.foodBasket.net.ParamsBean;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by programmer on 2018/2/24.
 */

public class OrderAction extends BaseAction {

    /**
     * 去结算
     * userId
     * products( json字符串 包括每样产品的id和对应的数目如： {productId:"123" count:23})
     */
    public void orderFrom(Context context, String products, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("userId", userId));
        list.add(new ParamsBean("products", products));
        setUrlName2("business/");
        postRun("OrderForm_settleOrder_OL.action", list, callback);
    }

    /**
     * 提交订单
     * userId  products(json字符串 包括每样产品的id和对应的数目如： {productId:"123" count:23})
     * addressId  payChannel(0："货到付款"; 1： "到店取货"; 2： "欠账订单";)   deliveryTime(送达时间 2018-01-01  12：00：00这种形式)
     */
    public void confirmOrder(Context context, String products, String addressId, String payChannel, String deliveryTime, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("userId", userId));
        list.add(new ParamsBean("products", products));
        list.add(new ParamsBean("addressId", addressId));
        list.add(new ParamsBean("payChannel", payChannel));
        list.add(new ParamsBean("deliveryTime", deliveryTime));
        setUrlName2("business/");
        postRun("OrderForm_confirmOrder_OL.action", list, callback);
    }

    /**
     * 订单列表
     */
    public void orderList(Context context, String deliveryState, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("userId", userId));
        list.add(new ParamsBean("deliveryState", deliveryState));
        setUrlName2("business/");
        postRun("OrderNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 订单详情
     */
    public void orderInfo( String id, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("objectID", id));
        setUrlName2("business/");
        postRun("OrderNavigate_detail_OL.action", list, callback);
    }

    /**
     * 确认收货
     */
    public void confirmReceipt( String id, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("orderId", id));
        setUrlName2("business/");
        postRun("DeliveryNavigate_confirmReceipt_OL.action", list, callback);
    }
}
