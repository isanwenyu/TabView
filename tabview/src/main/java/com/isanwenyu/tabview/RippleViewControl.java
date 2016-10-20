package com.isanwenyu.tabview;

import android.support.annotation.ColorRes;

import com.andexert.library.RippleView;

/**
 * <pre>
 * 水波纹配置控制类
 * Created by isanwenyu on 2016/10/20.
 * Copyright (c) 2016 isanwenyu@163.com. All rights reserved.
 * </pre>
 */
public interface RippleViewControl {
    /**
     * @see com.andexert.library.RippleView#setFrameRate(int)
     */
    TabView setTabRippleFrameRate(int frameRate);

    /**
     * @see com.andexert.library.RippleView#setRippleDuration(int)
     */
    TabView setTabRippleDuration(int rippleDuration);

    /**
     * @see com.andexert.library.RippleView#setRippleAlpha(int)
     */
    TabView setTabRippleAlpha(int rippleAlpha);

    /**
     * @see com.andexert.library.RippleView#setZoomDuration(int)
     */
    TabView setTabRippleZoomDuration(int zoomDuration);

    /**
     * @see com.andexert.library.RippleView#setZoomScale(float)
     */
    TabView setTabRippleZoomScale(float zoomScale);

    /**
     * @see com.andexert.library.RippleView#setRippleColor(int)
     */
    TabView setTabRippleColor(@ColorRes int rippleColor);

    /**
     * @see com.andexert.library.RippleView#setRipplePadding(int)
     */
    TabView setTabRipplePadding(int ripplePadding);

    /**
     * @see com.andexert.library.RippleView#setRippleType(RippleView.RippleType)
     */
    TabView setTabRippleType(final RippleView.RippleType rippleType);

    /**
     * @see com.andexert.library.RippleView#setCentered(Boolean)
     */
    TabView setTabRippleCentered(final Boolean isCentered);

    /**
     * @see com.andexert.library.RippleView#setZooming(Boolean)
     */
    TabView setTabRippleZooming(Boolean hasToZoom);

    /**
     * @see com.andexert.library.RippleView#setOnRippleCompleteListener(RippleView.OnRippleCompleteListener)
     */
    TabView setOnTabRippleCompleteListener(RippleView.OnRippleCompleteListener listener);
}
