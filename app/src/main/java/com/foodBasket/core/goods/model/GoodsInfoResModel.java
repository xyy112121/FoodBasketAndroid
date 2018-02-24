package com.foodBasket.core.goods.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * Created by programmer on 2018/2/24.
 */

public class GoodsInfoResModel extends ResponseBean {

    public ProductBasic productBasic;
    public List<Attributes> attributes;
    public List<Pictures> pictures;

    public class ProductBasic {
        public String summary;
        public int salePrice;
        public String name;
        public int soldNumber;
        public String alias;
        public String memo;
        public String displayUnit;
        public String id;
        public String detail;
    }

    public class Attributes {
        public String productAttribute_name;
        public String basicValue;
        public String id;
    }

    public class Pictures {
        public String picture_pictureUrl;
        public String picture_id;
    }
}
