/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuan.simple.core.ui.title;

import yuan.core.list.BaseViewHolder;
import yuan.core.list.GridDivider;
import yuan.core.list.RecyclerAdapter;
import yuan.core.title.StatusUtil;
import yuan.core.title.TitleBar;
import yuan.core.tool.RouteUtil;
import yuan.core.tool.ToastUtil;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yuan.simple.R;
import com.yuan.simple.core.module.SubjectBean;
import com.yuan.simple.core.presenter.TitleBarPresenter;
import com.yuan.simple.main.contract.MainContract;

import yuan.core.widget.StateLayout;
import yuan.depends.glide.GlideUtil;
import yuan.depends.ui.RecyclerViewFragment;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

/**
 * create by Yuan ye.
 * download truck before use this
 *
 * @author YuanYe
 * @date 2019/7/19  23:55
 */
public class TitleBarFragment extends RecyclerViewFragment<TitleBarPresenter, SubjectBean>
        implements MainContract {

    private TitleBar titleBar;

    @Override
    public int getLayoutId() {
        return yuan.depends.R.layout.base_title_bar_recycler_refresh_layout;
    }

    @Override
    protected void init(RecyclerView recyclerView, SmartRefreshLayout smartRefreshLayout, StateLayout mStateLayout) {
        titleBar = findViewById(R.id.title_bar);
        titleBar.setLeftIcon(R.drawable.ic_base_back_white)
                .setTitleText("TitleBar")
                .setTextColor(getColor2(R.color.white))
                .setLeftClickFinish()
                .setBackgroundColor(getColor2(R.color.colorPrimary));

        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        //动态更改列数
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int code = mData.get(position).getType();
                if (code == 1000 || code == 2000 || code == 3000
                        || code == 4000) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(manager);
        //add divider.
        recyclerView.addItemDecoration(new GridDivider());

        mStateLayout.showLoading();
        getPresenter().loadData(mData);

    }

    @Override
    protected int getItemLayoutId(int position) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, SubjectBean item, int position) {
        int code = item.getType();
        if (code == 1000 || code == 2000 || code == 3000
                || code == 4000) {
            holder.setBackgroundColor(android.R.id.text1, getColor2(R.color.lightblue100));
        } else {
            holder.setBackgroundColor(android.R.id.text1,getColor2(R.color.white));
        }
        holder.setText(android.R.id.text1, mData.get(position).getName());
    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClick(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder holder, View view, int position) {
                switch (mData.get(position).getType()) {
                    case 1001:
                        StatusUtil.darkMode(mContext, true);
                        break;
                    case 1002:
                        StatusUtil.darkMode(mContext, false);
                        break;
                    case 1003:
                        StatusUtil.setStatusBarColor(mContext, ContextCompat.getColor(mContext, R.color.colorPrimary));
                        break;
                    case 1004:
                        StatusUtil.hideBar(mContext);
                        break;
                    case 10041:
                        StatusUtil.showBar(mContext);
                        break;
                    case 1006:
                        StatusUtil.setFloat(mContext);
                        break;
                    case 2001:
                        GlideUtil.create().showImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563715324851&di=c04d94030276f397232c05563d256ab7&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F8%2F59a61753a5a93.jpg",
                                titleBar.getBackgroundView());
                        break;
                    case 2002:
                        titleBar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                        break;
                    case 2003:
                        titleBar.setLeftText("取消");
                        break;
                    case 2004:
                        titleBar.setLeftIcon(R.drawable.ic_base_back_black);
                        break;
                    case 2005:
                        titleBar.setTitleText("Title");
                        break;
                    case 20051:
                        titleBar.setSubtitleText("副标题");
                        break;
                    case 2006:
                        titleBar.setRightText("菜单");
                        break;
                    case 2007:
                        titleBar.setRightIcon(R.drawable.ic_base_menu_more_black);
                        break;
                    case 2008:
                        final ArrayList<String> menuData = new ArrayList<String>();
                        menuData.add("选项一");
                        menuData.add("选项二");
                        menuData.add("选项三");
                        titleBar.setRightMenu(menuData, new TitleBar.OnMenuItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                ToastUtil.showShort(mContext, menuData.get(position));
                            }
                        });
                        break;
                    case 2009:
                        titleBar.setAnimationIn();
                        break;
                    case 2010:
                        titleBar.setAnimationOut();
                        break;
                    case 2011:
                        titleBar.setLeftClickFinish();
                        break;
                    case 4001:
                        RouteUtil.open(mContext, TitleFullScreenActivity.class);
                        break;
                }
            }
        });

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getPresenter().loadData(mData);
            }
        });
    }

    @Override
    public void notifyDataChange(boolean isSuccess) {
        mAdapter.notifyDataSetChanged();
        //状态显示控制
        if (isSuccess) mStateLayout.showContent();
        else mStateLayout.showEmpty();
    }
}