package com.xuan.myframework.view.modle.response;

import java.util.List;

/**
 * com.xuan.myframework.view.modle.response
 * Created by xuan on 2017/7/25.
 * version
 * desc
 */

public class BangumiDetailsRecommendedModle {

    /**
     * code : 0
     * message : success
     * result : {"list":[{"bangumi_id":"2995","cover":"http://i0.hdslb.com/bfs/bangumi/e2b46584e29f2349748b48c5fb26ce02fcbca4e8.jpg","follow":"699942","isfinish":"1","season_id":"5058","title":"路人超能100(灵能百分百)","total_count":"12"},{"bangumi_id":"3282","cover":"http://i0.hdslb.com/bfs/bangumi/eb53e1b66b8f25c87449dda817ece92f0bfe8308.jpg","follow":"30098","isfinish":"0","season_id":"5473","title":"心灵的声音","total_count":"70"},{"bangumi_id":"3326","cover":"http://i0.hdslb.com/bfs/bangumi/dcbf3c193f4f8c0433d8dee2ee02580de15948b6.jpg","follow":"98686","isfinish":"0","season_id":"5538","title":"青鬼","total_count":"12"},{"bangumi_id":"3300","cover":"http://i0.hdslb.com/bfs/bangumi/9925ece99e3458760fc074e8564f74a1d6f46e1d.jpg","follow":"48257","isfinish":"0","season_id":"5504","title":"喵阿楞","total_count":"26"},{"bangumi_id":"2975","cover":"http://i0.hdslb.com/bfs/bangumi/65b007cb885f74804c6662180e4d6a5e0ec57dd0.jpg","follow":"490202","isfinish":"1","season_id":"5033","title":"腐男子高校生活","total_count":"12"},{"bangumi_id":"3331","cover":"http://i0.hdslb.com/bfs/bangumi/c74ba43e76c346476c8c09e653d35de69073cc49.jpg","follow":"112141","isfinish":"0","season_id":"5543","title":"学园Handsome","total_count":"12"}],"season_id":"5070","title":"齐木楠雄的灾难（日播版）"}
     */

    public int code;

    public String message;

    /**
     * list : [{"bangumi_id":"2995","cover":"http://i0.hdslb.com/bfs/bangumi/e2b46584e29f2349748b48c5fb26ce02fcbca4e8.jpg","follow":"699942","isfinish":"1","season_id":"5058","title":"路人超能100(灵能百分百)","total_count":"12"},{"bangumi_id":"3282","cover":"http://i0.hdslb.com/bfs/bangumi/eb53e1b66b8f25c87449dda817ece92f0bfe8308.jpg","follow":"30098","isfinish":"0","season_id":"5473","title":"心灵的声音","total_count":"70"},{"bangumi_id":"3326","cover":"http://i0.hdslb.com/bfs/bangumi/dcbf3c193f4f8c0433d8dee2ee02580de15948b6.jpg","follow":"98686","isfinish":"0","season_id":"5538","title":"青鬼","total_count":"12"},{"bangumi_id":"3300","cover":"http://i0.hdslb.com/bfs/bangumi/9925ece99e3458760fc074e8564f74a1d6f46e1d.jpg","follow":"48257","isfinish":"0","season_id":"5504","title":"喵阿楞","total_count":"26"},{"bangumi_id":"2975","cover":"http://i0.hdslb.com/bfs/bangumi/65b007cb885f74804c6662180e4d6a5e0ec57dd0.jpg","follow":"490202","isfinish":"1","season_id":"5033","title":"腐男子高校生活","total_count":"12"},{"bangumi_id":"3331","cover":"http://i0.hdslb.com/bfs/bangumi/c74ba43e76c346476c8c09e653d35de69073cc49.jpg","follow":"112141","isfinish":"0","season_id":"5543","title":"学园Handsome","total_count":"12"}]
     * season_id : 5070
     * title : 齐木楠雄的灾难（日播版）
     */

    public ResultBean result;


    public  class ResultBean {

        public String season_id;

        public String title;

        /**
         * bangumi_id : 2995
         * cover : http://i0.hdslb.com/bfs/bangumi/e2b46584e29f2349748b48c5fb26ce02fcbca4e8.jpg
         * follow : 699942
         * isfinish : 1
         * season_id : 5058
         * title : 路人超能100(灵能百分百)
         * total_count : 12
         */

        public List<ListBean> list;


        public  class ListBean {

            public String bangumi_id;

            public String cover;

            public String follow;

            public String isfinish;

            public String season_id;

            public String title;

            public String total_count;
        }
    }
}
