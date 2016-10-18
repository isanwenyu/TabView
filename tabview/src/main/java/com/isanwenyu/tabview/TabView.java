package com.isanwenyu.tabview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.itingchunyu.badgeview.IBadgeTextView;
import com.itingchunyu.badgeview.IBadgeView;
import com.itingchunyu.badgeview.IBadgeViewImpl;

/**
 * Created by zhuyuanbao on 2016/5/21.
 * Copyright (c) 2016 isanwenyu@163.com. All rights reserved.
 */
public class TabView extends FrameLayout implements Checkable, View.OnClickListener, IBadgeViewImpl {

    public static final int IMG_DEFAULT_SIZE = 30;
    public static final int TEXT_DEFAULT_SIZE = 14;
    private static final float IMG_DEFAULT_MARGIN = 5;
    ImageView mTabImgView;
    TextView mTabTextView;

    private String mTextString; // 文本颜色
    private ColorStateList mTextColor; // 文字颜色
    //文字大小 默认单位sp
    private float mTextSize = TEXT_DEFAULT_SIZE;
    //默认图片尺寸 默认单位dp
    private float mImgDimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, IMG_DEFAULT_SIZE, getResources().getDisplayMetrics());
    private float mImgMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, IMG_DEFAULT_MARGIN, getResources().getDisplayMetrics());
    //图片外边距
    private Drawable mImgDrawable;//图片资源

    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    private boolean mBroadcasting;//是否调用checkedListener中
    private boolean mChecked;//是否checked状态
    private IBadgeTextView mBadgeTextView;//徽章控件
    private FrameLayout mImgContainer;//图片控件容器

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
        mChecked = a.getBoolean(
                R.styleable.TabView_tabChecked,
                mChecked);

        if (a.hasValue(R.styleable.TabView_imgDrawable)) {
            mImgDrawable = a.getDrawable(
                    R.styleable.TabView_imgDrawable);
        }

        a.recycle();

        mBadgeTextView = new IBadgeTextView(getContext(), attrs);
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

        //设置自定义属性
        setTextString(mTextString);
        setTextColor(mTextColor);
        setTextSize(mTextSize);
        setImgDrawable(mImgDrawable);
        setImgDimension(mImgDimension);
        setImgMargin(mImgMargin);
        setChecked(mChecked);
        setClickable(true);
        //注册点击事件后 performClick响应
        setOnClickListener(this);

        //初始化徽章布局的目标布局
        mBadgeTextView.setTargetView(mImgContainer);
    }

    public String getTextString() {
        return mTextString;
    }

    public void setTextString(String mTextString) {
        this.mTextString = mTextString;
        mTabTextView.setText(mTextString);
    }

    public ColorStateList getTextColor() {
        return mTextColor;
    }

    public void setTextColor(ColorStateList mTextColor) {
        if (mTextColor != null) {
            this.mTextColor = mTextColor;
            mTabTextView.setTextColor(mTextColor);
        }
    }

    public float getImgMargin() {
        return mImgMargin;
    }

    public void setImgMargin(float mImgMargin) {
        this.mImgMargin = mImgMargin;
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) mTabImgView.getLayoutParams();
        marginLayoutParams.setMargins(((int) mImgMargin), ((int) mImgMargin), ((int) mImgMargin), ((int) mImgMargin));
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        if (mTextSize > 0) {
            this.mTextSize = mTextSize;
            mTabTextView.setTextSize(mTextSize);
        }
    }

    public float getImgDimension() {
        return mImgDimension;
    }

    public void setImgDimension(float mImgDimension) {
        if (mImgDimension > 0) {
            this.mImgDimension = mImgDimension;
            //如果自定义
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mTabImgView.getLayoutParams();
            layoutParams.height = (int) mImgDimension;
        }
    }

    public Drawable getImgDrawable() {
        return mImgDrawable;
    }

    public void setImgDrawable(Drawable mImgDrawable) {
        if (mImgDrawable != null) {
            this.mImgDrawable = mImgDrawable;
            mTabImgView.setImageDrawable(mImgDrawable);
        }
    }

    /**
     * 设置图片控件的父类容器的padding
     */
    public void setImgContainerPadding(int padding) {
        mImgContainer.setPadding(padding, padding, padding, padding);
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
    public void onClick(View v) {

    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }

    @Override
    public IBadgeView setBadgeCount(int count) {
        return mBadgeTextView.setBadgeCount(count);
    }

    @Override
    public IBadgeView setBadgeShown(boolean isShowBadge) {
        return mBadgeTextView.setBadgeShown(isShowBadge);
    }

    @Override
    public IBadgeView setBadgeColor(int color) {
        return mBadgeTextView.setBadgeColor(color);
    }

    @Override
    public IBadgeView setmDefaultTopPadding(int mDefaultTopPadding) {
        return mBadgeTextView.setmDefaultTopPadding(mDefaultTopPadding);
    }

    @Override
    public IBadgeView setmDefaultRightPadding(int mDefaultRightPadding) {
        return mBadgeTextView.setmDefaultRightPadding(mDefaultRightPadding);
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
