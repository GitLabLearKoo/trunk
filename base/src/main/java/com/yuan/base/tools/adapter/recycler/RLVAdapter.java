package com.yuan.base.tools.adapter.recycler;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuan.base.R;
import com.yuan.base.tools.layout.Views;

import java.util.List;

/**
 * Created by YuanYe on 2017/12/18.
 * 简化RecyclerView的Adapter代码
 */
public abstract class RLVAdapter<T> extends RecyclerView.Adapter<RLVAdapter.ViewHolder> implements View.OnClickListener {

    protected Context mContext;

    /**
     * item点击事件监听
     */
    private OnItemClickListener listener;

    public RLVAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = Views.inflate(parent, getItemLayout(parent, viewType));
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    public abstract
    @LayoutRes
    int getItemLayout(ViewGroup parent, int viewType);


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(R.id.item_position, position);
        holder.itemView.setTag(R.id.item_holder, holder);
        holder.itemView.setOnClickListener(this);
        onBindHolder(holder, position);
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    public abstract void onBindHolder(ViewHolder holder, int position);

    /**
     * item的点击事件
     */
    protected void onItemClick(ViewHolder holder, View view, int position) {

    }

    public void setOnItemClick(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (view.getTag(R.id.item_position) != null) {
            int position = (int) view.getTag(R.id.item_position);
            if (view.getTag(R.id.item_holder) != null) {
                ViewHolder holder = (ViewHolder) view.getTag(R.id.item_holder);
                if (holder.itemView.getId() == view.getId()) {
                    onItemClick(holder, view, position);
                    if (listener != null) listener.onItemClick(holder, view, position);
                }
            }
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;

        public ViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
        }

        /**
         * 获取View
         */
        public <k extends View> k getView(@IdRes int resId) {
            k k = (k) mViews.get(resId);
            if (k == null) {
                k = Views.find(itemView, resId);
                mViews.put(resId, k);
            }
            return k;
        }

        /**
         * TextView设置文字
         */
        public void setText(@IdRes int resId, CharSequence text) {
            View view = getView(resId);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
        }

        /**
         * 添加事件监听
         */
        public void setOnclick(@IdRes int resId, View.OnClickListener listener) {
            getView(resId).setOnClickListener(listener);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, View view, int position);
    }
}
