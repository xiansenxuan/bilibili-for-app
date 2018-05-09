package com.xuan.myframework.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.view.CommonHeader;
import com.jakewharton.rxbinding2.view.RxView;
import com.xuan.myframework.R;
import com.xuan.myframework.base.adapter.BaseRecyclerViewAdapter;
import com.xuan.myframework.base.fragment.RxBaseFragment;
import com.xuan.myframework.glide.GlideUtils;
import com.xuan.myframework.utils.NumberUtils;
import com.xuan.myframework.utils.SnackbarUtils;
import com.xuan.myframework.view.activity.UserDetailsActivity;
import com.xuan.myframework.view.activity.VideoDetailsActivity;
import com.xuan.myframework.view.modle.response.VideoDetailsModle;
import com.xuan.myframework.view.presenterImpl.VideoIntroductionPresenterImpl;
import com.xuan.myframework.view.view.VideoIntroductionView;
import com.xuan.myframework.widget.CircleProgressDialog;
import com.xuan.myframework.widget.EmptyView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static android.view.View.inflate;

/**
 * Created by xuan on 2017/6/14.
 */

public class VideoIntroductionFragment extends RxBaseFragment<VideoIntroductionPresenterImpl> implements VideoIntroductionView {

    @BindView(R.id.recycler_view)LRecyclerView recycler_view;
    @BindView(R.id.empty_view)EmptyView empty_view;
    @BindView(R.id.circle_progress)CircleProgressDialog circle_progress;

    private MyRecyclerViewAdapter adapter;
    private LRecyclerViewAdapter lAdapter;

    private int avId;
    private List<VideoDetailsModle.RelatesBean> itemList;

    private void addHeader(final VideoDetailsModle data) {
        lAdapter.removeHeaderView();

        CommonHeader view=new CommonHeader(getActivity(),R.layout.item_video_introduction_head);

        TextView tv_title= (TextView) view.findViewById(R.id.tv_title);
        TextView tv_play= (TextView) view.findViewById(R.id.tv_play);
        TextView tv_comment= (TextView) view.findViewById(R.id.tv_comment);
        TextView tv_description= (TextView) view.findViewById(R.id.tv_description);

        tv_title.setText(data.title);
        tv_play.setText(NumberUtils.convertThousandsString(data.stat.view));
        tv_comment.setText(NumberUtils.convertThousandsString(data.stat.danmaku));
        tv_description.setText(data.desc);

        LinearLayout ll_button= (LinearLayout) view.findViewById(R.id.ll_button);

        int[] iconMiddle={R.drawable.video_share_middle,R.drawable.video_coin_middle,
                R.drawable.video_download_middle,R.drawable.video_collect_middle};
        String[] tags={"分享","投硬币","收藏","缓存"};
        int[] colors={R.color.base_green_text,R.color.base_yellow,R.color.base_blue,R.color.base_pink};
        int[] counts={data.stat.share,data.stat.favorite,data.stat.coin};

        for (int i = 0; i < iconMiddle.length; i++) {
            View childView= inflate(getContext(),R.layout.include_video_introduction_button,null);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                    ,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight=1;
            params.width=0;
            childView.setLayoutParams(params);

            TextView tv_count= (TextView) childView.findViewById(R.id.tv_count);
            ImageButton iv_photo= (ImageButton) childView.findViewById(R.id.iv_photo);
            TextView tv_desc= (TextView) childView.findViewById(R.id.tv_desc);

            if(i!=iconMiddle.length-1){
                tv_count.setTextColor(android.support.v4.content.ContextCompat.getColor(getContext(),colors[i]));
                tv_count.setText(NumberUtils.convertThousandsString(counts[i]));
            }
            iv_photo.setBackgroundResource(R.drawable.video_button_bg);
            iv_photo.setImageResource(iconMiddle[i]);
            tv_desc.setText(tags[i]);

            ll_button.addView(childView);
        }

        ImageView iv_user_head= (ImageView) view.findViewById(R.id.iv_user_head);
        TextView tv_user_name= (TextView) view.findViewById(R.id.tv_user_name);
        TextView tv_attention= (TextView) view.findViewById(R.id.tv_attention);

        GlideUtils.loadCircleImage(getActivity(),data.owner.face,iv_user_head);
        tv_user_name.setText(data.owner.name);
        RxView.clicks(iv_user_head).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if(data.owner.mid!=-1){
                    UserDetailsActivity.launchActivity(getActivity(),data.owner.name,data.owner.mid,
                            data.owner.face);
                }
            }
        });

        CardView card_view= (CardView) view.findViewById(R.id.card_view);
        RxView.clicks(card_view)
                .throttleFirst(800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        SnackbarUtils.showShortSnackbar(getView(),"敬请期待");
                    }
                });

        lAdapter.addHeaderView(view);
    }


    @Override
    public void loadDataSuccess(VideoDetailsModle data) {
        SnackbarUtils.showShortSnackbar(this.getView(),"loadDataSuccess");

        VideoDetailsActivity activity = (VideoDetailsActivity) getActivity();
        activity.setTabLayoutText(data.stat.reply+"");

        addHeader(data);

        itemList=data.relates;
        if(itemList==null){
            recycler_view.setVisibility(View.GONE);
        }else{
            recycler_view.setVisibility(View.VISIBLE);

            adapter.addItem(itemList);
            recycler_view.scrollToPosition(0);
        }
    }

    @Override
    public void showProgress() {
        circle_progress.setVisibility(View.VISIBLE);
        circle_progress.spin();
    }

    @Override
    public void hideProgress() {
        circle_progress.setVisibility(View.GONE);
        circle_progress.stopSpinning();
    }

    @Override
    public void showMsg(String message) {
        SnackbarUtils.showShortSnackbar(this.getView(),"数据加载失败,请重新加载或者检查网络是否链接");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_video_introduction;
    }

    @Override
    public void initView(View view) {

        if(getArguments()!=null){
            avId=getArguments().getInt(VideoDetailsActivity.key_av_id);
        }

        initEmptyView();
        initRecyclerView();

        mPresenter=new VideoIntroductionPresenterImpl(this);
    }

    private void initRecyclerView() {
        if(adapter==null){
            adapter=new MyRecyclerViewAdapter();
            lAdapter=new LRecyclerViewAdapter(adapter);

            LinearLayoutManager gridLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            recycler_view.setLayoutManager(gridLayoutManager);

            recycler_view.setAdapter(lAdapter);

            //是否允许嵌套滑动
            recycler_view.setNestedScrollingEnabled(true);
            recycler_view.setPullRefreshEnabled(false);
            recycler_view.setLoadMoreEnabled(false);
            recycler_view.setHasFixedSize(true);

            adapter.addOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(itemList!=null && itemList.size()>0){
                        VideoDetailsActivity.launchActivity(getActivity(),
                                itemList.get(position).aid,itemList.get(position).pic);
                    }

                }
            });

        }


    }

    @Override
    public void initData() {
        mPresenter.queryVideoIntroductionData(avId,this.bindToLifecycle());
    }

    @Override
    public void initToolBar() {

    }

    private void initEmptyView() {
        empty_view.setVisibility(View.GONE);
        empty_view.setEmptyImage(R.drawable.load_error);
        empty_view.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
    }

    @Override
    public void showEmptyView() {
        recycler_view.setVisibility(View.GONE);
        empty_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        recycler_view.setVisibility(View.VISIBLE);
        empty_view.setVisibility(View.GONE);
    }

    public class MyRecyclerViewAdapter extends BaseRecyclerViewAdapter<MyViewHolder>{

        @Override
        public int getLayoutResId() {
            return R.layout.item_video_introduction_content;
        }

        @Override
        public void onBindItemViewHolder(MyViewHolder holder, int position) {
            VideoDetailsModle.RelatesBean modle= (VideoDetailsModle.RelatesBean) itemList.get(position);

            GlideUtils.loadDefaultImage(getActivity(),modle.pic,holder.iv_photo);
            holder.tv_title.setText(modle.title);
            holder.tv_up.setText(modle.owner.name);
            holder.tv_play.setText(NumberUtils.convertThousandsString(modle.stat.view));
            holder.tv_comment.setText(NumberUtils.convertThousandsString(modle.stat.danmaku));
        }

        @Override
        public MyViewHolder onCreateViewHolder(View view) {
            return new MyViewHolder(view);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_photo)ImageView iv_photo;
        @BindView(R.id.tv_title)TextView tv_title;
        @BindView(R.id.tv_up)TextView tv_up;
        @BindView(R.id.tv_play)TextView tv_play;
        @BindView(R.id.tv_comment)TextView tv_comment;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public static VideoIntroductionFragment newInstance(Bundle args) {
        VideoIntroductionFragment fragment=new VideoIntroductionFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
