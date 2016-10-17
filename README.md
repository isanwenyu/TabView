# Android TabView
[ ![Download](https://api.bintray.com/packages/isanwenyu/maven/TabView/images/download.svg) ](https://bintray.com/isanwenyu/maven/TabView/_latestVersion)
[![Platform](http://img.shields.io/badge/platform-android-brightgreen.svg?style=flat)](http://developer.android.com/index.html)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

Android首页底部常用tab切换控件
> TabGroup 参考 RadioGroup api基本一致 <br>
> TabView 参考 CompoundButton 实现 checkable 


## Quick Overview
![image](gif/tabview_demo.gif)

## Getting Started

 - Add the dependency to your build.gradle.
 
```
dependencies {
 	compile 'com.isanwenyu.tabview:tabview:0.2'
}
```
- Maven:

```
<dependency>
  <groupId>com.isanwenyu.tabview</groupId>
  <artifactId>tabview</artifactId>
  <version>0.2</version>
  <type>pom</type>
</dependency>
```


## Usage
- xml

```
    <com.isanwenyu.tabview.TabGroup
        android:id="@+id/tg_tab"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@android:color/white"
        android:elevation="10dp"
        app:checkedTab="@+id/tab_chat">

        <com.isanwenyu.tabview.TabView
            android:id="@id/tab_chat"
            style="@style/TabView"
            app:imgDimension="@dimen/tab_img_size"
            app:imgDrawable="@drawable/tab_chat"
            app:imgMargin="@dimen/tab_img_margin"
            app:textColor="@color/tab_chat_text_selector"
            app:textSize="@dimen/tab_view_text_size"
            app:textString="聊天" />

        <com.isanwenyu.tabview.TabView
            android:id="@+id/tb_app"
            style="@style/TabView"
            app:imgDimension="@dimen/tab_img_size"
            app:imgDrawable="@drawable/tab_app"
            app:imgMargin="@dimen/tab_img_margin"
            app:textColor="@color/tab_app_text_selector"
            app:textSize="@dimen/tab_view_text_size"
            app:textString="应用" />

        <com.isanwenyu.tabview.TabView
            android:id="@+id/tb_pic"
            style="@style/TabView"
            app:badge_color="@android:color/holo_green_dark"
            app:badge_count="9"
            app:badge_none_show="true"
            app:imgDrawable="@drawable/tab_pic"
            app:textColor="@color/tab_pic_text_selector"
            app:textSize="@dimen/tab_view_text_size"
            app:textString="图片" />

        <com.isanwenyu.tabview.TabView
            android:id="@+id/tb_user"
            style="@style/TabView"
            app:badge_color="@android:color/holo_orange_dark"
            app:badge_count="888"
            app:badge_none_show="true"
            app:badge_padding_right="@dimen/tab_img_margin"
            app:badge_padding_top="@dimen/tab_img_margin"
            app:imgDrawable="@drawable/tab_user"
            app:textColor="@color/tab_user_text_selector"
            app:textSize="@dimen/tab_view_text_size"
            app:textString="我" />
    </com.isanwenyu.tabview.TabGroup>     
```

- code

```
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

```
```
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
```
- BadgeView is from [https://github.com/liyanxi/BadgeView ](https://github.com/liyanxi/BadgeView ).All its attributes can also apply in the tabview.

```
        //init tab badge view,the others setted in activity_main.xml
        mChatTabView
                .setBadgeColor(getResources().getColor(android.R.color.holo_blue_dark))
                .setmDefaultTopPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()))
                .setShown(true);

        ((TabView) mTabGroup.getChildAt(1)).setBadgeCount(999)
                .setmDefaultTopPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()))
                .setShown(true);
```

## Todo

 ~~加入中央仓库~~ <br>
 ~~添加红点标识~~ <br>
 添加点击水波纹效果
 
## Dependencies
- [https://github.com/liyanxi/BadgeView](https://github.com/liyanxi/BadgeView) 徽章控件 显示数字提醒 小红点
- [https://github.com/isanwenyu/BintrayUploadGradle](https://github.com/isanwenyu/BintrayUploadGradle) 上传repo到bintray并添加到JCenter的gradle脚步库

## License

    Copyright 2016 isanwenyu@163.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
