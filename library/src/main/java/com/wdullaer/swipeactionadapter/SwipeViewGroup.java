/*
 * Copyright 2014 Wouter Dullaert
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wdullaer.swipeactionadapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;

import java.util.EnumMap;
import java.util.Map;

/**
 * Class to hold a ListView item and the swipe backgrounds
 *
 * Created by wdullaer on 22.06.14.
 */
public class SwipeViewGroup extends FrameLayout {
    private View contentView = null;

    private SwipeDirection visibleView = SwipeDirection.DIRECTION_NEUTRAL;
    private final EnumMap<SwipeDirection, View> mBackgroundMap =
            new EnumMap<SwipeDirection, View>(SwipeDirection.class);

    /**
     * Standard android View constructor
     *
     * @param context
     */
    public SwipeViewGroup(Context context) {
        super(context);
    }

    /**
     * Standard android View constructor
     *
     * @param context
     * @param attrs
     */
    public SwipeViewGroup(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    /**
     * Standard android View constructor
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SwipeViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
    }

    /**
     * Add a View to the background of the Layout. The background should have the same height
     * as the contentView
     *
     * @param background The View to be added to the Layout
     * @param direction The key to be used to find it again
     * @return A reference to the a layout so commands can be chained
     */
    public SwipeViewGroup addBackground(View background, SwipeDirection direction){
        if(mBackgroundMap.get(direction) != null) removeView(mBackgroundMap.get(direction));

        background.setVisibility(View.INVISIBLE);
        mBackgroundMap.put(direction,background);
        addView(background);
        return this;
    }

    /**
     * Show the View linked to a key. Don't do anything if the key is not found
     *
     * @param direction The key of the View to be shown
     */
    public void showBackground(SwipeDirection direction){
        if(mBackgroundMap.get(direction) == null) return;

        if(visibleView != SwipeDirection.DIRECTION_NEUTRAL)
            mBackgroundMap.get(visibleView).setVisibility(View.INVISIBLE);
        mBackgroundMap.get(direction).setVisibility(View.VISIBLE);
        visibleView = direction;
    }

    /**
     * Add a contentView to the Layout
     *
     * @param contentView The View to be added
     * @return A reference to the layout so commands can be chained
     */
    public SwipeViewGroup setContentView(View contentView){
        if(this.contentView != null) removeView(contentView);
        addView(contentView);
        this.contentView = contentView;

        return this;
    }

    /**
     * Returns the current contentView of the Layout
     *
     * @return contentView of the Layout
     */
    public View getContentView(){
        return contentView;
    }

    /**
     * Move all backgrounds to the edge of the Layout so they can be swiped in
     */
    public void translateBackgrounds(){
        this.setClipChildren(false);

        for (Map.Entry<SwipeDirection, View> entry : mBackgroundMap.entrySet()) {
            int key = entry.getKey().getValue();
            View value = entry.getValue();
            value.setTranslationX(-Integer.signum(key)*value.getWidth());
        }
    }

    @Override
    public Object getTag() {
        return contentView.getTag();
    }

    @Override
    public void setTag(Object tag) {
        contentView.setTag(tag);
    }

    @Override
    public Object getTag(int key) {
        return contentView.getTag(key);
    }

    @Override
    public void setTag(int key, Object tag) {
        contentView.setTag(key, tag);
    }
}
