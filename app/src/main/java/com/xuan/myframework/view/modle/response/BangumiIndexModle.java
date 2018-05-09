package com.xuan.myframework.view.modle.response;

import java.util.List;

/**
 * com.xuan.myframework.view.modle.response
 * Created by xuan on 2017/8/3.
 * version
 * desc
 */

public class BangumiIndexModle {
    public int code;

    public String message;

    public ResultBean result;



    public  class ResultBean {

        /**
         * cover : http://i2.hdslb.com/u_user/f7a16e4a28fe524ceddfe0860b52d057.jpg
         * tag_id : 117
         * tag_name : 轻改
         */

        public List<CategoryBean> category;

        public List<String> years;


        public  class CategoryBean {

            public String cover;

            public String tag_id;

            public String tag_name;

        }
    }
}
