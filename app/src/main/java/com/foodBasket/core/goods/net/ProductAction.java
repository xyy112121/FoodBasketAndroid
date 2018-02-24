package com.foodBasket.core.goods.net;

import com.foodBasket.net.BaseAction;
import com.foodBasket.net.ParamsBean;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品
 */

public class ProductAction extends BaseAction {
    /**
     * 商品详情
     */
    public void productInfo(String objectID, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("objectID", objectID));
        setUrlName2("resource/");
        postRun("ProductBasicNavigate_detail_OL.action", list, callback);
    }

}
