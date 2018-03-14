package com.foodBasket.core.main.net;

import android.content.Context;

import com.foodBasket.net.BaseAction;
import com.foodBasket.net.ParamsBean;
import com.foodBasket.util.Constants;
import com.foodBasket.util.ShareConfig;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by programmer on 2018/2/23.
 */

public class HomeAction extends BaseAction {

    /**
     * 首页获取特惠商品
     */
    public void discountList(int page, int rows, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("page", page + ""));
        list.add(new ParamsBean("rows", rows + ""));
        setUrlName2("resource/");
        postRun("ProductDiscountNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 首页获取推荐商品
     */
    public void recommendList(int page, int rows, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("page", page + ""));
        list.add(new ParamsBean("rows", rows + ""));
        setUrlName2("resource/");
        postRun("ProductRecommendNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 获取大分类
     */
    public void categoryList(int page, int rows, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("page", page + ""));
        list.add(new ParamsBean("rows", rows + ""));
        setUrlName2("resource/");
        postRun("CategoryNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 获取小分类
     */
    public void categorySmallList(String categoryId, int page, int rows, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("categoryId", categoryId));
        list.add(new ParamsBean("page", page + ""));
        list.add(new ParamsBean("rows", rows + ""));
        setUrlName2("resource/");
        postRun("CategorySmallNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 根据小分类获取产品列表
     */
    public void productBasicList(String categorySmallId, int page, int rows, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("categorySmallId", categorySmallId));
        list.add(new ParamsBean("page", page + ""));
        list.add(new ParamsBean("rows", rows + ""));
        setUrlName2("resource/");
        postRun("ProductBasicNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 获取购物车
     */
    public void shoppingCartList(Context context, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("userId", userId));
        setUrlName2("business/");
        postRun("ShoppingCartNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 加入购物车
     * userId  productBasicId(产品ID) productNumber(数目)
     */
    public void addShoppingCart(Context context, String productBasicId, String count, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("userId", userId));
        list.add(new ParamsBean("productBasicId", productBasicId));
        list.add(new ParamsBean("productNumber", count));
        setUrlName2("business/");
        postRun("ShoppingCartForm_save_OL.action", list, callback);
    }


    /**
     * 个人中心
     */
    public void user(Context context, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("objectID", userId));
        setUrlName2("control/");
        postRun("UserNavigate_getUser_OL.action", list, callback);
    }

    /**
     * 搜索产品
     * productName page rows
     */
    public void searchProductList(String productName, int page, int rows, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        list.add(new ParamsBean("productName", productName));
        list.add(new ParamsBean("page", page + ""));
        list.add(new ParamsBean("rows", rows + ""));
        setUrlName2("resource/");
        postRun("ProductBasicNavigate_searchPageOL_OL.action", list, callback);
    }

    /**
     * 首页右上角发现开关
     */
    public void getDiscovery(StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        setUrlName2("business/");
        postRun("DiscoveryNavigate_getIsDiscovery_OL.action", list, callback);
    }

    /**
     * 获取待收货数量
     */
    public void getDebitAndWaiting(Context context, StringCallback callback) throws Exception {
        List<ParamsBean> list = new ArrayList<>();
        String userId = ShareConfig.getConfigString(context, Constants.USERID, "");
        list.add(new ParamsBean("userId", userId));
        setUrlName2("business/");
        postRun("OrderNavigate_getDebitAndWaitingR_OL.action", list, callback);
    }
}
