package com.isanwenyu.tabview;

/**
 * <pre>
 * 红点view 需要实现接口 for {@link TabView}
 * Created by isanwenyu on 16/9/26.
 * Copyright (c) 2016 isanwenyu@163.com. All rights reserved.
 * </pre>
 * @see com.itingchunyu.badgeview.IBadgeView
 */
public interface BadgeViewControl {
    /**
     * 设置数值
     *
     * @param count
     * @return
     */
    TabView setBadgeCount(int count);

    /**
     * 设置是否显示
     *
     * @param isShowBadge
     * @return
     */
    TabView setBadgeShown(boolean isShowBadge);

    /**
     * 设置小点颜色
     *
     * @param color
     * @return
     */
    TabView setBadgeColor(int color);

    /**
     * 设置视图上边距
     *
     * @param mDefaultTopPadding
     * @return
     */
    TabView setmDefaultTopPadding(int mDefaultTopPadding);

    /**
     * 设置视图右边距
     *
     * @param mDefaultRightPadding
     * @return
     */
    TabView setmDefaultRightPadding(int mDefaultRightPadding);
}
