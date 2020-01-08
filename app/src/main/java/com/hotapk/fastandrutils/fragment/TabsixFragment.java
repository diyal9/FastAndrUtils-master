package com.hotapk.fastandrutils.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hotapk.fastandrutils.R;

import cn.hotapk.fastandrutils.fragment.FLazyFragment;
import cn.hotapk.fastandrutils.utils.FLogUtils;

/**
 * @author laijian
 * @Copyright (C)2018-06-06 21:04:18 , www.hotapk.cn
 */
public class TabsixFragment extends FLazyFragment {

    private TextView tv_text;

    public TabsixFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static TabsixFragment newInstance() {
        TabsixFragment fragment = new TabsixFragment();
        return fragment;
    }

    @Override
    public int setLayoutRes() {
        return R.layout.fragment_tab_six;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void initLoad() {
        tv_text.setText("加载第六个数据");
    }

    @Override
    public void initView(View view) {
        tv_text = view.findViewById(R.id.tv_text);
        FLogUtils.getInstance().e("第六个frame加载数据");
        tv_text.setText("加载第六个数据");

    }

}
