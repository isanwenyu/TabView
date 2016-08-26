# Android TabView
Android首页底部常用tab切换控件
> TabGroup 参考 RadioGroup api基本一致 <br>
> TabView 参考 CompoundButton 实现 checkable 


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
            android:id="@+id/tab_chat"
            style="@style/TabView"
            app:imgDrawable="@drawable/tab_chat"
            app:imgDimension="@dimen/tab_img_size"
            app:imgMargin="@dimen/tab_img_margin"
            app:textColor="@color/tab_chat_text_selector"
            app:textSize="@dimen/tab_view_text_size"
            app:textString="聊天" />

        <com.isanwenyu.tabview.TabView
            android:id="@+id/tb_app"
            style="@style/TabView"
            app:imgDrawable="@drawable/tab_app"
            app:imgDimension="@dimen/tab_img_size"
            app:imgMargin="@dimen/tab_img_margin"
            app:textColor="@color/tab_app_text_selector"
            app:textSize="@dimen/tab_view_text_size"
            app:textString="应用" />
            
        .......
     
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

## Gif
![image](https://raw.githubusercontent.com/isanwenyu/TabView/master/gif/tabview_demo.gif)

## Todo
 1. 加入中央仓库
 2. 添加红点标识

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
