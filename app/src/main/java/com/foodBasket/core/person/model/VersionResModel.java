package com.foodBasket.core.person.model;

import com.foodBasket.net.ResponseBean;

/**
 * 版本更新
 */

public class VersionResModel extends ResponseBean {

    public Version version;

    public class Version {
        public String fileName;
        public String filePath;
        public String name;
        public String memo;
        public String id;
        public String version;
    }
}
