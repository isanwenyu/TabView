package com.isanwenyu.tabview.demo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;

import com.isanwenyu.tabview.TabGroup;
import com.isanwenyu.tabview.TabView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 主界面<br>
 * Copyright (c) 2016 isanwenyu@163.com. All rights reserved.
 */
public class MainActivity extends AppCompatActivity {
    public static final int TAB_CHAT = 0x00;
    public static final int TAB_APP = 0x01;
    public static final int TAB_PIC = 0x02;
    public static final int TAB_USER = 0x03;
    private static final String TAG = MainActivity.class.getSimpleName();
    @InjectView(R.id.vp_main)
    ViewPager mViewPager;
    @InjectView(R.id.tg_tab)
    TabGroup mTabGroup;
    @InjectView(R.id.tab_chat)
    TabView mChatTabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定当前界面
        ButterKnife.inject(this);

        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        initViewPager();
        mTabGroup.setOnCheckedChangeListener(new TabGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TabGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_chat:
                        setCurrentFragment(TAB_CHAT);
                        break;
                    case R.id.tb_pic:
                        setCurrentFragment(TAB_PIC);
                        break;
                    case R.id.tb_app:
                        setCurrentFragment(TAB_APP);
                        break;
                    case R.id.tb_user:
                        setCurrentFragment(TAB_USER);
                        break;
                }
            }
        });

        //init tab badge view,the others setted in activity_main.xml
        mChatTabView
                .setBadgeColor(getResources().getColor(android.R.color.holo_blue_dark))
                .setmDefaultTopPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()))
                .setShown(true);

        ((TabView) mTabGroup.getChildAt(1)).setBadgeCount(999)
                .setmDefaultTopPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()))
                .setShown(true);
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {

        //缓存3页避免切换时出现空指针
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new ContentFragmentAdapter.Holder(getSupportFragmentManager())
                .add(MainTabFragment.newInstance(android.R.color.holo_blue_dark))
                .add(MainTabFragment.newInstance(android.R.color.holo_red_dark))
                .add(MainTabFragment.newInstance(android.R.color.holo_green_dark))
                .add(MainTabFragment.newInstance(android.R.color.holo_orange_dark))
                .set());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((TabView) mTabGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 改变fragment状态
     *
     * @param position
     */
    public void setCurrentFragment(final int position) {
        Log.i(TAG, "position:" + position);
        //不使用切换动画 避免与自定义动画冲突
        mViewPager.setCurrentItem(position, false);
    }
}
