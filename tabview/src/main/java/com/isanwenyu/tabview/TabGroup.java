/*
 * Copyright (C) 2006 The Android Open Source Project
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

package com.isanwenyu.tabview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * <pre>
 * 仿RadioGroup 的底部tab布局
 * Copyright (c) 2016 isanwenyu@163.com. All rights reserved.
 * </pre>
 *
 * @see TabView
 * @see android.widget.RadioGroup
 */
public class TabGroup extends LinearLayout {
    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    // tracks children  tab views checked state
    private TabView.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;

    /**
     * {@inheritDoc}
     */
    public TabGroup(Context context) {
        super(context);
        init(null, 0);
    }

    public TabGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    /**
     * {@inheritDoc}
     */
    public TabGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // retrieve selected  tab view as requested by the user in the
        // XML layout file
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TabGroup, defStyle, 0);

        int value = attributes.getResourceId(R.styleable.TabGroup_checkedTab, View.NO_ID);
        if (value != View.NO_ID) {
            mCheckedId = value;
        }

        attributes.recycle();

        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);

        setOrientation(HORIZONTAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // checks the appropriate  tab view as requested in the XML file
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof TabView) {
            final TabView button = (TabView) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        }

        super.addView(child, index, params);
    }

    /**
     * <p>Sets the selection to the  tab view whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking {@link #clearCheck()}.</p>
     *
     * @param id the unique id of the  tab view to select in this group
     * @see #getCheckedTabViewId()
     * @see #clearCheck()
     */
    public void check(@IdRes int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedId(@IdRes int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof TabView) {
            ((TabView) checkedView).setChecked(checked);
        }
    }

    /**
     * <p>Returns the identifier of the selected  tab view in this group.
     * Upon empty selection, the returned value is -1.</p>
     *
     * @return the unique id of the selected  tab view in this group
     * @attr ref android.R.styleable#RadioGroup_checkedButton
     * @see #check(int)
     * @see #clearCheck()
     */
    @IdRes
    public int getCheckedTabViewId() {
        return mCheckedId;
    }

    /**
     * <p>Clears the selection. When the selection is cleared, no  tab view
     * in this group is selected and {@link #getCheckedTabViewId()} returns
     * null.</p>
     *
     * @see #check(int)
     * @see #getCheckedTabViewId()
     */
    public void clearCheck() {
        check(-1);
    }

    /**
     * <p>Register a callback to be invoked when the checked  tab view
     * changes in this group.</p>
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return TabGroup.class.getName();
    }

    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * tab view changed in this group.</p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>Called when the checked  tab view has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *
         * @param group     the group in which the checked tab view has changed
         * @param checkedId the unique identifier of the newly checked tab view
         */
        void onCheckedChanged(TabGroup group, @IdRes int checkedId);
    }

    private class CheckedStateTracker implements TabView.OnCheckedChangeListener {
        public void onCheckedChanged(TabView tabView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = tabView.getId();
            setCheckedId(id);
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        public void onChildViewAdded(View parent, View child) {
            if (parent == TabGroup.this && child instanceof TabView) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        id = View.generateViewId();
                    } else {
                        id = child.hashCode();
                    }
                    child.setId(id);
                }
                ((TabView) child).setOnCheckedChangeWidgetListener(
                        mChildOnCheckedChangeListener);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {
            if (parent == TabGroup.this && child instanceof TabView) {
                ((TabView) child).setOnCheckedChangeWidgetListener(null);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
