package com.xuan.myframework.view.modle.response;

import java.util.List;

/**
 * com.xuan.myframework.view.modle.response
 * Created by xuan on 2017/8/2.
 * version
 * desc
 */

public class ChaseBangumiModle {

    /**
     * status : true
     * data : {"count":3,"pages":1,"result":[{"season_id":"5523","share_url":"http://bangumi.bilibili.com/anime/5523/","title":"3月的狮子","is_finish":0,"favorites":201926,"newest_ep_index":1,"last_ep_index":0,"total_count":22,"cover":"http://i0.hdslb.com/bfs/bangumi/7bfd5b9a4aabee8df09df12939d2f32c2f41a0d7.jpg","evaluate":"","brief":"独自居住在东京旧市街的17岁职业将棋棋士\u2014\u2014桐山零。\n他是个幼时就因为意外失去家人，怀抱着深沉孤独的..."},{"season_id":"5063","share_url":"http://bangumi.bilibili.com/anime/5063/","title":"DAYS","is_finish":0,"favorites":177725,"newest_ep_index":14,"last_ep_index":0,"total_count":24,"cover":"http://i0.hdslb.com/bfs/bangumi/20f05ecc4c50d560814e511ced4205f37e640501.jpg","evaluate":"","brief":"【本周更新的13话将改为10月2日播出】电视动画《DAYS》改编自日本漫画家安田刚士原作的同名漫画。..."},{"season_id":"5029","share_url":"http://bangumi.bilibili.com/anime/5029/","title":"天真与闪电","is_finish":1,"favorites":410969,"newest_ep_index":12,"last_ep_index":0,"total_count":12,"cover":"http://i0.hdslb.com/bfs/bangumi/5626f7afbd39a0b4561dea5bd267ba1ef2248c0d.jpg","evaluate":"","brief":"妻子亡故后，独自努力养育女儿的数学教师·犬冢。不擅长料理又是个味觉白痴的他，在偶然之下和学生·饭田小..."}]}
     */

    public boolean status;

    /**
     * count : 3
     * pages : 1
     * result : [{"season_id":"5523","share_url":"http://bangumi.bilibili.com/anime/5523/","title":"3月的狮子","is_finish":0,"favorites":201926,"newest_ep_index":1,"last_ep_index":0,"total_count":22,"cover":"http://i0.hdslb.com/bfs/bangumi/7bfd5b9a4aabee8df09df12939d2f32c2f41a0d7.jpg","evaluate":"","brief":"独自居住在东京旧市街的17岁职业将棋棋士\u2014\u2014桐山零。\n他是个幼时就因为意外失去家人，怀抱着深沉孤独的..."},{"season_id":"5063","share_url":"http://bangumi.bilibili.com/anime/5063/","title":"DAYS","is_finish":0,"favorites":177725,"newest_ep_index":14,"last_ep_index":0,"total_count":24,"cover":"http://i0.hdslb.com/bfs/bangumi/20f05ecc4c50d560814e511ced4205f37e640501.jpg","evaluate":"","brief":"【本周更新的13话将改为10月2日播出】电视动画《DAYS》改编自日本漫画家安田刚士原作的同名漫画。..."},{"season_id":"5029","share_url":"http://bangumi.bilibili.com/anime/5029/","title":"天真与闪电","is_finish":1,"favorites":410969,"newest_ep_index":12,"last_ep_index":0,"total_count":12,"cover":"http://i0.hdslb.com/bfs/bangumi/5626f7afbd39a0b4561dea5bd267ba1ef2248c0d.jpg","evaluate":"","brief":"妻子亡故后，独自努力养育女儿的数学教师·犬冢。不擅长料理又是个味觉白痴的他，在偶然之下和学生·饭田小..."}]
     */

    public DataBean data;


    public class DataBean{

        public int count;

        public int pages;

        /**
         * season_id : 5523
         * share_url : http://bangumi.bilibili.com/anime/5523/
         * title : 3月的狮子
         * is_finish : 0
         * favorites : 201926
         * newest_ep_index : 1
         * last_ep_index : 0
         * total_count : 22
         * cover : http://i0.hdslb.com/bfs/bangumi/7bfd5b9a4aabee8df09df12939d2f32c2f41a0d7.jpg
         * evaluate :
         * brief : 独自居住在东京旧市街的17岁职业将棋棋士——桐山零。
         * 他是个幼时就因为意外失去家人，怀抱着深沉孤独的...
         */

        public List<ResultBean> result;


        public class ResultBean{

            public String season_id;

            public String share_url;

            public String title;

            public int is_finish;

            public int favorites;

            public int newest_ep_index;

            public int last_ep_index;

            public int total_count;

            public String cover;

            public String evaluate;

            public String brief;
        }


    }
}
