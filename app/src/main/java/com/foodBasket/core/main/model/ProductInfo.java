package com.foodBasket.core.main.model;

/**
 * Created by programmer on 2017/12/27.
 */

public class ProductInfo {
    protected String orderdetailid;
    protected String commodityid;
    protected String commodityname;
    protected String commoditypicture;
    protected String ordertime;
    protected boolean isChoosed = false;
    private String desc;
    private float orderprice;
    private int ordernumber;
    private int position;// 绝对位置，只在ListView构造的购物车中，在删除时有效

    public String getCommodityid() {
        return commodityid;
    }

    public void setCommodityid(String commodityid) {
        this.commodityid = commodityid;
    }

    public String getCommodityname() {
        return commodityname;
    }

    public void setCommodityname(String commodityname) {
        this.commodityname = commodityname;
    }

    public String getCommoditypicture() {
        return commoditypicture;
    }

    public void setCommoditypicture(String commoditypicture) {
        this.commoditypicture = commoditypicture;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }


    public String getOrderdetailid() {
        return orderdetailid;
    }

    public void setOrderdetailid(String orderdetailid) {
        this.orderdetailid = orderdetailid;
    }

    public float getOrderprice() {
        return orderprice;
    }

    public void setOrderprice(float orderprice) {
        this.orderprice = orderprice;
    }

    public int getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(int ordernumber) {
        this.ordernumber = ordernumber;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }
}
