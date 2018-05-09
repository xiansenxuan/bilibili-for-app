package com.xuan.myframework.view.modle.response;

import com.google.gson.annotations.SerializedName;
import com.xuan.myframework.view.fragment.RecommendFragment;

import java.util.ArrayList;

/**
 * Created by xuan on 2017/6/14.
 */

public class RecommendModle {

    //自我包装的数据modle
    public class IconHeadTypeModle   implements RecommendFragment.Visitable{
        public HeadModle head;

        public IconHeadTypeModle(HeadModle head) {
            this.head = head;
        }

        @Override
        public int type(RecommendFragment.TypeFactory typeFactory) {
            return typeFactory.type(this);
        }
    }
    public class MoreHeadTypeModle   implements RecommendFragment.Visitable{
        public HeadModle head;

        public MoreHeadTypeModle(HeadModle head) {
            this.head = head;
        }

        @Override
        public int type(RecommendFragment.TypeFactory typeFactory) {
            return typeFactory.type(this);
        }
    }
    public class LiveHeadTypeModle   implements RecommendFragment.Visitable{
        public HeadModle head;

        public LiveHeadTypeModle(HeadModle head) {
            this.head = head;
        }

        @Override
        public int type(RecommendFragment.TypeFactory typeFactory) {
            return typeFactory.type(this);
        }
    }


    public class IconBodyTypeModle   implements RecommendFragment.Visitable{
        public BodyModle body;

        public IconBodyTypeModle(BodyModle body) {
            this.body = body;
        }

        @Override
        public int type(RecommendFragment.TypeFactory typeFactory) {
            return typeFactory.type(this);
        }
    }
    public class LiveBodyTypeModle   implements RecommendFragment.Visitable{
        public BodyModle body;

        public LiveBodyTypeModle(BodyModle body) {
            this.body = body;
        }

        @Override
        public int type(RecommendFragment.TypeFactory typeFactory) {
            return typeFactory.type(this);
        }
    }
    public class BangumiBodyTypeModle   implements RecommendFragment.Visitable{
        public BodyModle body;

        public BangumiBodyTypeModle(BodyModle body) {
            this.body = body;
        }

        @Override
        public int type(RecommendFragment.TypeFactory typeFactory) {
            return typeFactory.type(this);
        }
    }


    public class RefreshFoodTypeModle   implements RecommendFragment.Visitable{
        public String type;

        public RefreshFoodTypeModle(String type) {
            this.type = type;
        }

        @Override
        public int type(RecommendFragment.TypeFactory typeFactory) {
            return typeFactory.type(this);
        }
    }

    public class ChangeFoodTypeModle   implements RecommendFragment.Visitable{
        public String type;

        public ChangeFoodTypeModle(String type) {
            this.type = type;
        }

        @Override
        public int type(RecommendFragment.TypeFactory typeFactory) {
            return typeFactory.type(this);
        }
    }

    public class BangumiFoodTypeModle   implements RecommendFragment.Visitable{
        public String type;

        public BangumiFoodTypeModle(String type) {
            this.type = type;
        }

        @Override
        public int type(RecommendFragment.TypeFactory typeFactory) {
            return typeFactory.type(this);
        }
    }

    //自我包装的数据modle


    public ArrayList<ResultModle> result;
    public class ResultModle{
        public ArrayList<BodyModle> body;
        public HeadModle head;
        public String type;
    }

    public class BodyModle{
        public String title;

        public String style;

        public String cover;

        public String param;

        @SerializedName("goto")
        public String gotoX;

        public int width;

        public int height;

        public String play;

        public String danmaku;

        public String up;

        @SerializedName("up_face")
        public String upFace;

        public int online;

        public String desc1;
    }

    public class HeadModle{
        public String param;

        @SerializedName("goto")
        public String gotoX;

        public String style;

        public String title;

        public int count;

    }
}
