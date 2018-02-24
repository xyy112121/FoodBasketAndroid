package com.foodBasket.core.main.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * Created by programmer on 2018/2/24.
 */

public class CategoryResModel extends ResponseBean {

    public int total;
    public List<CategoryRowsModel> rows;
}
