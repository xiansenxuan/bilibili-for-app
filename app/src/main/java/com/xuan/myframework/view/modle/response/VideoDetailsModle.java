package com.xuan.myframework.view.modle.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuan on 2017/6/21.
 */

public class VideoDetailsModle implements Serializable{
    public int aid;

    public int attribute;

    public int copyright;

    public int ctime;

    public String desc;

    public int duration;
    /**
     * count : 4
     * list : [{"avatar":"http://i1.hdslb.com/bfs/face/ed2c9df24f653a59bbbff6c71afbc0505697187f.jpg","message":"","mid":10900408,"msg_deleted":0,"pay_mid":17129795,"rank":1,"trend_type":1,"uname":"三无Marblack","vip_info":{"vipStatus":0,"vipType":0}},{"avatar":"http://i1.hdslb.com/bfs/face/c283b17aaf5b32291187e57773ee323f23bebd78.jpg","message":"","mid":10900408,"msg_deleted":0,"pay_mid":21233153,"rank":2,"trend_type":1,"uname":"吾王无名丶","vip_info":{"vipStatus":0,"vipType":0}},{"avatar":"http://i2.hdslb.com/bfs/face/30512550a0d33d0e08aa49844f21f0b724b7010d.jpg","message":"","mid":10900408,"msg_deleted":0,"pay_mid":18859502,"rank":3,"trend_type":2,"uname":"某科学的静电卷毛","vip_info":{"vipStatus":0,"vipType":0}},{"avatar":"http://i2.hdslb.com/bfs/face/dd1fa7bb352fadc6d79e60dd4982c22ca273d76b.jpg","mid":10900408,"pay_mid":11705318,"rank":4,"trend_type":2,"uname":"水无月觞","vip_info":{"vipStatus":1,"vipType":1}}]
     * show : true
     * total : 93
     * user : null
     */

    public ElecBean elec;
    public class ElecBean {

        public int count;

        public boolean show;

        public int total;

        /**
         * avatar : http://i1.hdslb.com/bfs/face/ed2c9df24f653a59bbbff6c71afbc0505697187f.jpg
         * message :
         * mid : 10900408
         * msg_deleted : 0
         * pay_mid : 17129795
         * rank : 1
         * trend_type : 1
         * uname : 三无Marblack
         * vip_info : {"vipStatus":0,"vipType":0}
         */

        public List<ListBean> list;
    }
    public class ListBean {

        public String avatar;

        public String message;

        public int mid;

        public int msg_deleted;

        public int pay_mid;

        public int rank;

        public int trend_type;

        public String uname;

        /**
         * vipStatus : 0
         * vipType : 0
         */

        public VipInfoBean vip_info;
    }
    public class VipInfoBean {

        public int vipStatus;

        public int vipType;
    }
    
    
        /**
     * face : http://i0.hdslb.com/bfs/face/d7deccd01b89ac864a39c527d8e17064074c21e1.jpg
     * mid : 10900408
     * name : 司徒·Earl
     */

    public OwnerBean owner;
    public class OwnerBean {

        public String face;

        public int mid;

        public String name;
    }
    /**
     * vip : {"accessStatus":1,"dueRemark":"","vipDueDate":1501257600000,"vipStatus":1,"vipStatusWarn":"","vipType":1}
     */

    public OwnerExtBean owner_ext;
    public class OwnerExtBean {

        /**
         * accessStatus : 1
         * dueRemark :
         * vipDueDate : 1501257600000
         * vipStatus : 1
         * vipStatusWarn :
         * vipType : 1
         */

        public VipBean vip;
    }
    public class VipBean {

        public int accessStatus;

        public String dueRemark;

        public long vipDueDate;

        public int vipStatus;

        public String vipStatusWarn;

        public int vipType;
    }
    
    public String pic;

    public int pubdate;

    /**
     * attention : -999
     * favorite : 0
     */

    public ReqUserBean req_user;

    public class ReqUserBean {

        public int attention;

        public int favorite;
    }
    /**
     * bp : 0
     * download : 1
     * elec : 1
     * hd5 : 0
     * movie : 0
     * pay : 0
     */

    public RightsBean rights;
    public class RightsBean {

        public int bp;

        public int download;

        public int elec;

        public int hd5;

        public int movie;

        public int pay;
    }
    /**
     * coin : 5152
     * danmaku : 859
     * favorite : 7880
     * his_rank : 0
     * now_rank : 0
     * reply : 555
     * share : 276
     * view : 155975
     */

    public StatBean stat;
    public class StatBean {

        public int coin;

        public int danmaku;

        public int favorite;

        public int his_rank;

        public int now_rank;

        public int reply;

        public int share;

        public int view;
    }
    public int state;

    public int tid;

    public String title;

    public String tname;

    /**
     * cid : 11261901
     * from : vupload
     * has_alias : 0
     * link :
     * page : 1
     * part :
     * rich_vid :
     * vid : vupload_11261901
     * weblink :
     */

    public List<PagesBean> pages;
    public class PagesBean {

        public int cid;

        public String from;

        public int has_alias;

        public String link;

        public int page;

        public String part;

        public String rich_vid;

        public String vid;

        public String weblink;
    }
    /**
     * aid : 6906182
     * owner : {"face":"","mid":0,"name":"美芽美妆"}
     * pic : http://i0.hdslb.com/bfs/archive/96d40a4170c07e27c8a0651c12285418cc52b5b1.jpg
     * stat : {"coin":0,"danmaku":25,"favorite":515,"his_rank":0,"now_rank":0,"reply":12,"share":0,"view":4851}
     * title : 美芽|妩媚勾魂狐狸妆 魅惑迷死人
     */

    public List<RelatesBean> relates;
    public class RelatesBean {

        public int aid;

        /**
         * face :
         * mid : 0
         * name : 美芽美妆
         */

        public OwnerBean owner;

        public String pic;

        /**
         * coin : 0
         * danmaku : 25
         * favorite : 515
         * his_rank : 0
         * now_rank : 0
         * reply : 12
         * share : 0
         * view : 4851
         */

        public StatBean stat;

        public String title;
    }
    /**
     * cover : http://static.hdslb.com/images/transparent.gif
     * hates : 0
     * likes : 2
     * tag_id : 1568421
     * tag_name : cos化妆教程
     */

    public List<TagBean> tag;
    public class TagBean {

        public String cover;

        public int hates;

        public int likes;

        public int tag_id;

        public String tag_name;
    }
    public List<String> tags;

}
