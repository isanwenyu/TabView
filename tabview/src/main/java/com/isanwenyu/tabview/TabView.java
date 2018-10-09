package com.isanwenyu.tabview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.itingchunyu.badgeview.BadgeTextView;

/**
 * 继承水波纹控件RippleView 支持其所有属性
 * Created by isanwenyu on 2016/5/21.
 * Copyright (c) 2016 isanwenyu@163.com. All rights reserved.
 */
public class TabView extends RippleView implements Checkable, BadgeViewControl, RippleViewControl, RippleView.OnRippleCompleteListener {

    public static final int IMG_DEFAULT_SIZE = 30;
    public static final int TEXT_DEFAULT_SIZE = 14;
    public static final boolean IS_TEXT_DEFAULT_SHOWN = true;
    private static final float IMG_DEFAULT_MARGIN = 0;
    private static final float IMG_CONTAINER_DEFAULT_PADDING = 2;
    ImageView mTabImgView;
    TextView mTabTextView;

    private String mTextString; // 文本颜色
    private ColorStateList mTextColor; // 文字颜色
    //文字大小 默认单位sp
    private float mTextSize = TEXT_DEFAULT_SIZE;
    //默认图片尺寸 默认单位dp
    private float mImgDimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, IMG_DEFAULT_SIZE, getResources().getDisplayMetrics());
    //图片外边距
    private float mImgMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, IMG_DEFAULT_MARGIN, getResources().getDisplayMetrics());
    //包含图片容器的内边距
    private float mImgContainerPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, IMG_CONTAINER_DEFAULT_PADDING, getResources().getDisplayMetrics());

    private Drawable mImgDrawable;//图片资源

    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    private boolean mBroadcasting;//是否调用checkedListener中
    private boolean mChecked;//是否checked状态
    private BadgeTextView mBadgeTextView;//徽章控件
    private FrameLayout mImgContainer;//图片控件容器
    private boolean mRippleEnable = true;//是否开启水波纹效果 默认为true
    private OnRippleCompleteListener mOnRippleCompleteListener;//用户设置的水波纹完成监听器
    private boolean mBadgeShow;//徽章控件是否显示
    private boolean isTextShown = IS_TEXT_DEFAULT_SHOWN;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TabView, defStyle, 0);

        isTextShown = a.getBoolean(
                R.styleable.TabView_textShown, IS_TEXT_DEFAULT_SHOWN);
        mTextString = a.getString(
                R.styleable.TabView_textString);
        mTextColor = a.getColorStateList(
                R.styleable.TabView_textColor);
        mTextSize = a.getDimensionPixelSize(R.styleable.TabView_textSize, -1);
        //文字大小 特殊处理
        if (mTextSize != -1) {
            mTextSize = Util.px2sp(getContext(), mTextSize);
        } else {
            mTextSize = TEXT_DEFAULT_SIZE;
        }
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mImgDimension = a.getDimension(
                R.styleable.TabView_imgDimension,
                mImgDimension);
        mImgMargin = a.getDimension(
                R.styleable.TabView_imgMargin,
                mImgMargin);
        mImgContainerPadding = a.getDimension(
                R.styleable.TabView_imgContainerPadding,
                mImgContainerPadding);
        mChecked = a.getBoolean(
                R.styleable.TabView_tabChecked,
                mChecked);
        mRippleEnable = a.getBoolean(
                R.styleable.TabView_tabRippleEnable,
                mRippleEnable);

        if (a.hasValue(R.styleable.TabView_imgDrawable)) {
            mImgDrawable = a.getDrawable(
                    R.styleable.TabView_imgDrawable);
        }

        a.recycle();

        //get the attrs from BadgeViewUtil
        final TypedArray b = getContext().obtainStyledAttributes(
                attrs, R.styleable.BadgeViewUtil, defStyle, 0);
        //reset badge none show attribute default false
        mBadgeShow = b.getBoolean(R.styleable.BadgeViewUtil_badge_none_show, false);
        b.recycle();

        mBadgeTextView = new BadgeTextView(getContext(), attrs);
        //初始化
        initView();

    }

    /**
     * 初始化界面
     */
    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_tab_view, this, true);
        mTabImgView = (ImageView) findViewById(R.id.iv_tab_img);
        mTabTextView = (TextView) findViewById(R.id.tv_tab_text);
        mImgContainer = (FrameLayout) findViewById(R.id.layout_img_container);
        setGravity(Gravity.CENTER);
        //设置自定义属性
        setTextShown(isTextShown);
        setTextString(mTextString);
        setTextColor(mTextColor);
        setTextSize(mTextSize);
        setImgDrawable(mImgDrawable);
        setImgDimension(mImgDimension);
        setImgMargin(mImgMargin);
        setImgContainerPadding((int) mImgContainerPadding);
        setChecked(mChecked);
        //初始化ripple相关属性
        setTabRippleEnable(mRippleEnable);
        //注册父类的OnRippleCompleteListener 响应#onComplete()方法 //修改为TabView直接响应点击事件
//        super.setOnRippleCompleteListener(this);
        //初始化徽章布局的目标布局
        mBadgeTextView.setTargetView(mImgContainer);
        setBadgeShown(mBadgeShow);
    }

    public void setTextShown(boolean textShown) {
        isTextShown = textShown;
        mTabTextView.setVisibility(textShown ? VISIBLE : GONE);
    }

    public String getTextString() {
        return mTextString;
    }

    public TabView setTextString(String mTextString) {
        this.mTextString = mTextString;
        mTabTextView.setText(mTextString);
        return this;
    }

    public ColorStateList getTextColor() {
        return mTextColor;
    }

    public TabView setTextColor(ColorStateList mTextColor) {
        if (mTextColor != null) {
            this.mTextColor = mTextColor;
            mTabTextView.setTextColor(mTextColor);
        }
        return this;
    }

    public float getImgMargin() {
        return mImgMargin;
    }

    public TabView setImgMargin(float mImgMargin) {
        this.mImgMargin = mImgMargin;
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) mTabImgView.getLayoutParams();
        marginLayoutParams.setMargins(((int) mImgMargin), ((int) mImgMargin), ((int) mImgMargin), ((int) mImgMargin));
        return this;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public TabView setTextSize(float mTextSize) {
        if (mTextSize > 0) {
            this.mTextSize = mTextSize;
            mTabTextView.setTextSize(mTextSize);
        }
        return this;
    }

    public float getImgDimension() {
        return mImgDimension;
    }

    public TabView setImgDimension(float mImgDimension) {
        if (mImgDimension > 0) {
            this.mImgDimension = mImgDimension;
            //如果自定义
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mTabImgView.getLayoutParams();
            layoutParams.height = (int) mImgDimension;
            layoutParams.width = (int) mImgDimension;
        }
        return this;
    }

    public Drawable getImgDrawable() {
        return mImgDrawable;
    }

    public TabView setImgDrawable(Drawable mImgDrawable) {
        if (mImgDrawable != null) {
            this.mImgDrawable = mImgDrawable;
            mTabImgView.setImageDrawable(mImgDrawable);
        }
        return this;
    }

    /**
     * 设置图片控件的父类容器的padding
     */
    public TabView setImgContainerPadding(int padding) {
        mImgContainer.setPadding(padding, padding, padding, padding);
        return this;
    }

    public ImageView getTabImgView() {
        return mTabImgView;
    }

    public FrameLayout getImgContainer() {
        return mImgContainer;
    }

    public TextView getTabTextView() {
        return mTabTextView;
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     * @hide
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * <p>Changes the checked state of this button.</p>
     *
     * @param checked true to check the button, false to uncheck it
     */
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            //刷新子控件布局选中状态
            mTabImgView.setSelected(checked);
            mTabTextView.setSelected(checked);
            refreshDrawableState();

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }


            mBroadcasting = false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the tab view is already checked, this method will not toggle the radio button.
     */
    @Override
    public void toggle() {
        // we override to prevent toggle when the radio is already
        // checked (as opposed to check boxes widgets)
        if (!isChecked()) {
            setChecked(!mChecked);
        }
    }


    @Override
    public boolean performClick() {
        toggle();
        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }

        return handled;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.checked = isChecked();
        return ss;
    }

    /**
     * @param state
     * @see android.support.v4.view.ViewPager#onRestoreInstanceState(Parcelable)
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // fix bug  java.lang.ClassCastException: android.view.AbsSavedState$1 cannot be cast to com.isanwenyu.tabview.TabView$SavedState

        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }

    /***************************
     * 设置徽章控件相关属性
     *******************************************************/
    @Override
    public TabView setBadgeCount(int count) {
        mBadgeTextView.setBadgeCount(count);
        return this;
    }

    @Override
    public TabView setBadgeShown(boolean isShowBadge) {
        mBadgeTextView.setBadgeShown(isShowBadge);
        return this;
    }

    @Override
    public TabView setBadgeColor(int color) {
        mBadgeTextView.setBadgeColor(color);
        return this;
    }

    @Override
    public TabView setmDefaultTopPadding(int mDefaultTopPadding) {
        mBadgeTextView.setmDefaultTopPadding(mDefaultTopPadding);
        return this;
    }

    @Override
    public TabView setmDefaultRightPadding(int mDefaultRightPadding) {
        mBadgeTextView.setmDefaultRightPadding(mDefaultRightPadding);
        return this;
    }

    /***************************
     * 设置水波纹效果相关属性
     *******************************************************/

    public TabView setTabRippleEnable(boolean rippleEnable) {
        this.mRippleEnable = rippleEnable;
        if (!rippleEnable) {
            //如果不可用设置水波纹动画时间为0
            setRippleDuration(0);
        }
        return this;
    }

    @Override
    public void onComplete(RippleView rippleView) {
        //水波纹动画完成后自动切换TabView状态
//        toggle();
        if (mOnRippleCompleteListener != null) {
            mOnRippleCompleteListener.onComplete(this);
        }
    }

    @Override
    public TabView setTabRippleFrameRate(int frameRate) {
        setFrameRate(frameRate);
        return this;
    }

    @Override
    public TabView setTabRippleDuration(int rippleDuration) {
        setRippleDuration(rippleDuration);
        return this;
    }

    @Override
    public TabView setTabRippleAlpha(int rippleAlpha) {
        setAlpha(rippleAlpha);
        return this;
    }

    @Override
    public TabView setTabRippleZoomDuration(int zoomDuration) {
        setZoomDuration(zoomDuration);
        return this;
    }

    @Override
    public TabView setTabRippleZoomScale(float zoomScale) {
        setZoomScale(zoomScale);
        return this;
    }

    @Override
    public TabView setTabRippleColor(@ColorRes int rippleColor) {
        setRippleColor(rippleColor);
        return this;
    }

    @Override
    public TabView setTabRipplePadding(int ripplePadding) {
        setRipplePadding(ripplePadding);
        return this;
    }

    @Override
    public TabView setTabRippleType(RippleType rippleType) {
        setRippleType(rippleType);
        return this;
    }

    @Override
    public TabView setTabRippleCentered(Boolean isCentered) {
        setCentered(isCentered);
        return this;
    }

    @Override
    public TabView setTabRippleZooming(Boolean hasToZoom) {
        setZooming(hasToZoom);
        return this;
    }

    @Override
    public TabView setOnTabRippleCompleteListener(OnRippleCompleteListener listener) {
        this.mOnRippleCompleteListener = listener;
        return this;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a tabview changed.
     */
    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a TabView has changed.
         *
         * @param tabView   The TabView whose state has changed.
         * @param isChecked The new checked state of TabView.
         */
        void onCheckedChanged(TabView tabView, boolean isChecked);
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean checked;

        /**
         * Constructor called from {@link TabView#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return "TabView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}";
        }
    }

}
