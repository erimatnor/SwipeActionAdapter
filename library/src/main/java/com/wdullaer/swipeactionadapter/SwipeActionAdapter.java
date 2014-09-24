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

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.EnumMap;
import java.util.Map;

/**
 * Adapter that adds support for multiple swipe actions to your ListView
 *
 * Created by wdullaer on 04.06.14.
 */
public class SwipeActionAdapter extends DecoratorAdapter implements
        SwipeActionTouchListener.ActionCallbacks
{
    private ListView mListView;
    private SwipeActionTouchListener mTouchListener;
    protected SwipeActionListener mSwipeActionListener;
    private boolean mFadeOut = false;
    private boolean mFixedBackgrounds = false;
    private float mFarSwipeFraction = 0.5f; // Swipe half tile to go to "FAR" swipe
    private float mDismissSwipeFraction = 0.3f; // Swipe half tile to go to "FAR" swipe

    protected final EnumMap<SwipeDirection, Integer> mBackgroundResIds =
            new EnumMap<SwipeDirection, Integer>(SwipeDirection.class);

    public SwipeActionAdapter(BaseAdapter baseAdapter){
        super(baseAdapter);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent){
        // TODO: add our custom background images here (provide some good defaults)
        SwipeViewGroup output = (SwipeViewGroup)convertView;

        if(output == null) {
            output = new SwipeViewGroup(parent.getContext());
            for (Map.Entry<SwipeDirection, Integer> entry : mBackgroundResIds.entrySet()) {
                output.addBackground(View.inflate(parent.getContext(),
                                entry.getValue(), null), entry.getKey());
            }
        }

        output.setContentView(super.getView(position,output.getContentView(),parent));

        return output;
    }

    /**
     * SwipeActionTouchListener.ActionCallbacks callback
     * We just link it through to our own interface
     *
     * @param position the position of the item that was swiped
     * @return boolean indicating whether the item has actions
     */
    @Override
    public boolean hasActions(int position){
        return mSwipeActionListener != null && mSwipeActionListener.hasActions(position);
    }

    /**
     * SwipeActionTouchListener.ActionCallbacks callback
     * We just link it through to our own interface
     *
     * @param listView The originating {@link ListView}.
     * @param position The position to perform the action on, sorted in descending  order
     *                 for convenience.
     * @param direction The type of swipe that triggered the action
     * @return boolean indicating whether the item should be dismissed afterwards or not
     */
    @Override
    public boolean onAction(ListView listView, int position, SwipeDirection direction){
        return mSwipeActionListener != null && mSwipeActionListener.onSwipe(position,direction);
    }

    /**
     * Set whether items should have a fadeOut animation
     *
     * @param mFadeOut true makes items fade out with a swipe (opacity -> 0)
     * @return A reference to the current instance so that commands can be chained
     */
    public SwipeActionAdapter setFadeOut(boolean mFadeOut){
        this.mFadeOut = mFadeOut;
        if(mListView != null) mTouchListener.setFadeOut(mFadeOut);
        return this;
    }

    /**
     * Set fraction of view that needs to be swiped before switching
     * to "FAR" swipe
     *
     * @param farSwipeFraction the fraction of the width to swipe to switch to far swipe
     *                         (value between 0 and 1).
     * @return A reference to the current instance so that commands can be chained
     */
    public SwipeActionAdapter setFarSwipeFraction(float farSwipeFraction){

        if (farSwipeFraction < 0 || farSwipeFraction > 1) {
            throw new IllegalArgumentException("Must be a value between 0 and 1");
        }
        this.mFarSwipeFraction = farSwipeFraction;
        if(mListView != null) mTouchListener.setFarSwipeFraction(mFarSwipeFraction);
        return this;
    }


    /**
     * Set fraction of view that needs to be swiped before counted as dismissal of the view
     *
     * @param dismissSwipeFraction the fraction of the width to swipe to switch to far swipe
     *                         (value between 0 and 1).
     * @return A reference to the current instance so that commands can be chained
     */
    public SwipeActionAdapter setDismissSwipeFraction(float dismissSwipeFraction){

        if (dismissSwipeFraction < 0 || dismissSwipeFraction > 1) {
            throw new IllegalArgumentException("Must be a value between 0 and 1");
        }
        this.mDismissSwipeFraction = dismissSwipeFraction;
        if(mListView != null) mTouchListener.setDismissSwipeFraction(mDismissSwipeFraction);
        return this;
    }

    /**
     * Set whether the backgrounds should be fixed or swipe in from the side
     * The default value for this property is false: backgrounds will swipe in
     *
     * @param mFixedBackgrounds true for fixed backgrounds, false for swipe in
     */
    public SwipeActionAdapter setFixedBackgrounds(boolean mFixedBackgrounds){
        this.mFixedBackgrounds = mFixedBackgrounds;
        if(mListView != null) mTouchListener.setFixedBackgrounds(mFixedBackgrounds);
        return this;
    }

    /**
     * We need the ListView to be able to modify it's OnTouchListener
     *
     * @param mListView the ListView to which the adapter will be attached
     * @return A reference to the current instance so that commands can be chained
     */
    public SwipeActionAdapter setListView(ListView mListView){
        this.mListView = mListView;
        mTouchListener = new SwipeActionTouchListener(mListView,this);
        this.mListView.setOnTouchListener(mTouchListener);
        this.mListView.setOnScrollListener(mTouchListener.makeScrollListener());
        this.mListView.setClipChildren(false);
        mTouchListener.setFadeOut(mFadeOut);
        mTouchListener.setFixedBackgrounds(mFixedBackgrounds);
        return this;
    }

    /**
     * Getter that is just here for completeness
     *
     * @return the current ListView
     */
    public AbsListView getListView(){
        return mListView;
    }

    /**
     * Add a background image for a certain callback. The key for the background must be one of the
     * directions from the SwipeDirections class.
     *
     * @param direction the {@link SwipeDirection} of the callback for which this resource should be shown
     * @param resId the resource Id of the background to add
     * @return A reference to the current instance so that commands can be chained
     */
    public SwipeActionAdapter addBackground(SwipeDirection direction, int resId){
        mBackgroundResIds.put(direction,resId);
        return this;
    }

    /**
     * Set the listener for swipe events
     *
     * @param mSwipeActionListener class listening to swipe events
     * @return A reference to the current instance so that commands can be chained
     */
    public SwipeActionAdapter setSwipeActionListener(SwipeActionListener mSwipeActionListener){
        this.mSwipeActionListener = mSwipeActionListener;
        return this;
    }

    /**
     * Interface that listeners of swipe events should implement
     */
    public interface SwipeActionListener{
        public boolean hasActions(int position);
        public boolean onSwipe(int position, SwipeDirection direction);
    }
}
