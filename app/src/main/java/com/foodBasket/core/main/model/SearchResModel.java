package com.foodBasket.core.main.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * 搜索结果
 */

public class SearchResModel extends ResponseBean {

    public int total;
    public List<Rows> rows;

    public  class Rows {
        public String headPicture;
        public String summary;
        public int salePrice;
        public String name;
        public String alias;
        public String displayUnit;
        public String id;
    }
}
