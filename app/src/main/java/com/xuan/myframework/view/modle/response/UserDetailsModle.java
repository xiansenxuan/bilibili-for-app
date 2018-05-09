package com.xuan.myframework.view.modle.response;

import java.util.List;

/**
 * Created by xuan on 2017/6/12.
 */

public class UserDetailsModle {
    public CardModle card;

    public class CardModle {
        public String mid;

        public String name;

        public boolean approve;

        public String sex;

        public String rank;

        public String face;

        public int coins;

        public String DisplayRank;

        public int regtime;

        public int spacesta;

        public String birthday;

        public String place;

        public String description;

        public int article;

        public int fans;

        public int friend;

        public int attention;

        public String sign;

        /**
         * current_level : 6
         * current_min : 28800
         * current_exp : 298859
         * next_exp : -
         */

        public LevelInfoBean level_info;

        /**
         * pid : 0
         * name :
         * image :
         * expire : 0
         */

        public PendantBean pendant;

        /**
         * nid : 1
         * name : 黄金殿堂
         * image : http://i2.hdslb.com/bfs/face/82896ff40fcb4e7c7259cb98056975830cb55695.png
         * image_small : http://i2.hdslb.com/bfs/face/627e342851dfda6fe7380c2fa0cbd7fae2e61533.png
         * level : 稀有勋章
         * condition : 单个自制视频总播放数>=100万
         */

        public NameplateBean nameplate;

        /**
         * type : -1
         * desc :
         */

        public OfficialVerifyBean official_verify;

        public List<Integer> attentions;
    }

    public static class PendantBean {

        public int pid;

        public String name;

        public String image;

        public int expire;
    }

    public static class LevelInfoBean {

        public int current_level;

        public int current_min;

        public int current_exp;

        public String next_exp;
    }


    public static class NameplateBean {

        public int nid;

        public String name;

        public String image;

        public String image_small;

        public String level;

        public String condition;
    }

    public static class OfficialVerifyBean {

        public int type;

        public String desc;
    }

}
