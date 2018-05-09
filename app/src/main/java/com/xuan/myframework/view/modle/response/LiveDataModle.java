package com.xuan.myframework.view.modle.response;

import java.util.ArrayList;

/**
 * Created by xuan on 2017/6/1.
 */

public class LiveDataModle  {
    public ArrayList<LiveBanner> banner;
    public ArrayList<LiveEntranceIcons> entranceIcons;
    public ArrayList<LivePartitions> partitions;
//    public ArrayList<LiveRecommend_data> recommend_data;

    public class LiveBanner{
        public String img;
        public String link;
        public String remark;
        public String title;
       /* {
            "img": "http://i0.hdslb.com/bfs/live/a254e4e67b88cd0d3e20835bc1a6ff21d6fcbbb4.jpg",
                "link": "http://live.bilibili.com/AppBanner/index?id=523",
                "remark": "神起的声音",
                "title": "神起的声音-2017声优选拔大赛"
        },*/
    }


    public class LiveEntranceIcons{
        public String name;
        public int id;
        public EntranceIcon entrance_icon;
       /* {
            "id": 9,
                "name": "绘画专区"
        },*/
    }
    public class EntranceIcon{
        public String src;
        public String height;
        public String width;
        /*"entrance_icon": {
            "height": "132",
                    "src": "http://static.hdslb.com/live-static/images/mobile/android/big/xxhdpi/9_big.png?20170531140000",
                    "width": "132"
        },*/
    }


    public class LivePartitions{
        public ArrayList<Lives> lives;
        public Partition partition;

    }
    public class Lives{
        public CoverModle cover;
        public OwnerModle owner;

        public String title;
        public int room_id;
        public int check_version;
        public int online;
        public String area;
        public int area_id;
        public String playurl;
        public String accept_quality;
        public int broadcast_type;
        public int is_tv;
        /*{
            "accept_quality": "4",
                "area": "绘画专区",
                "area_id": 9,
                "broadcast_type": 0,
                "check_version": 0,
            "is_tv": 0,
                "online": 1051,
            "playurl": "http://live-play.acgvideo.com/live/418/live_9146767_332_c521e483.flv?wsSecret=9d2a48c10b22d5bd051db2cb2e5c2666&wsTime=590987ad",
                "room_id": 14682,
                "title": "补话痨(✿◡‿◡)"
        },*/
    }
    public class CoverModle{
        public String src;
        public String height;
        public String width;
/*        "cover": {
            "height": 180,
                    "src": "http://i0.hdslb.com/bfs/live/ea530f486cc84baee1c324f6ab8213ffeca9a643.jpg",
                    "width": 320
        },*/
    }
    public class OwnerModle{
        public String face;
        public int mid;
        public String name;
       /*  "owner": {
            "face": "http://i2.hdslb.com/bfs/face/49eb7f9c076c1a0f891d52234373c444aebbb83a.jpg",
                    "mid": 9146767,
                    "name": "奶油小布丁131"
        },*/
    }
    public class Partition{
        public SubIcon sub_icon;

        public String name;
        public String count;
        public String area;
        public String id;
       /* {
            "area": "draw",
                "count": 112,
                "id": 9,
                "name": "绘画专区",
        }*/
    }
    public class SubIcon{
        public String src;
        public String height;
        public String width;
      /*  "sub_icon": {
            "height": "63",
                    "src": "http://static.hdslb.com/live-static/images/mobile/android/small/xxhdpi/9.png?20170531140000",
                    "width": "63"
        }*/
    }


//    public class LiveRecommend_data{
//        public ArrayList<Lives> banner_data;
//        public ArrayList<Lives> lives;
//        public Partition partition;
//
//    }

}
