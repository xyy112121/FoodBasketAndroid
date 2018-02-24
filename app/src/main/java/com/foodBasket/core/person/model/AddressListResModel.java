package com.foodBasket.core.person.model;

import com.foodBasket.net.ResponseBean;

import java.util.List;

/**
 * Created by programmer on 2018/2/24.
 */

public class AddressListResModel extends ResponseBean {
    public int total;
    public List<AddressResModel> rows;

}
