package com.xuan.myframework.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.xuan.myframework.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * com.xuan.myframework.base.adapter
 * Created by xuan on 2017/6/27.
 * version
 * desc
 */
public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{

    public ArrayList<Object> itemList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public BaseRecyclerViewAdapter() {
    }

    public <T> void addItem(List<T> itemList) {
        if (this.itemList.addAll(itemList)) {
            notifyItemRangeInserted(this.itemList.size(), itemList.size());
        }
    }

    public <T> void addItem(T t) {
        if (this.itemList.add(t)) {
            notifyItemRangeInserted(this.itemList.size(), 1);
        }
    }

    public void remove(int position) {
        this.itemList.remove(position);
        notifyItemRemoved(position);

        if(position != itemList.size()){ // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position,this.itemList.size()-position);
        }
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBaseBindViewHolder(holder,position);
    }

    public abstract void onBindItemViewHolder(VH holder, int position);

    public void onBaseBindViewHolder(final VH holder, final int position){
        if(onItemClickListener!=null){
            RxView.clicks(holder.itemView)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            onItemClickListener.onItemClick(holder.itemView,position);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                            SnackbarUtils.showShortSnackbar(holder.itemView,"出错了~(≧▽≦)~啦啦啦.");
                        }
                    });
        }

        if(onItemLongClickListener!=null){
            RxView.longClicks(holder.itemView)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            onItemLongClickListener.onItemLongClick(holder.itemView,position);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                            SnackbarUtils.showShortSnackbar(holder.itemView,"出错了~(≧▽≦)~啦啦啦.");
                        }
                    });
        }

        onBindItemViewHolder(holder,position);

    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onBaseCreateViewHolder(parent,viewType);
    }

    public abstract int getLayoutResId();
    public abstract VH onCreateViewHolder(View view);

    public VH onBaseCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(), parent, false);
        return onCreateViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (itemList == null || itemList.size() == 0) {
            return 0;
        } else {
            return itemList.size();
        }
    }


    public void addOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public void addOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener=onItemLongClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(View view , int position);
    }
}

