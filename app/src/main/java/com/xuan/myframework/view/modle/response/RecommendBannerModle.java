package com.xuan.myframework.view.modle.response;

import java.util.ArrayList;

/**
 * Created by xuan on 2017/6/19.
 */

public class RecommendBannerModle {
    public int code;

    public ArrayList<BannerModle> data;

    public static class BannerModle {

        public String title;

        public String value;

        public String image;

        public int type;

        public int weight;

        public String remark;

        public String hash;
    }
}
