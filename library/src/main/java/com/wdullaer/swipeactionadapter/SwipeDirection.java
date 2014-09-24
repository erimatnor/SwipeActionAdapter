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

/**
 * Class containing a set of constant directions used throughout the package
 *
 * Created by wdullaer on 02.07.14.
 */
public enum SwipeDirection {
    // Constants
    DIRECTION_FAR_LEFT(-2),
    DIRECTION_NORMAL_LEFT(-1),
    DIRECTION_NEUTRAL(0),
    DIRECTION_NORMAL_RIGHT(1),
    DIRECTION_FAR_RIGHT(2);

    private int direction;

    private SwipeDirection(final int direction) {
        this.direction = direction;
    }

    public int getValue() {
        return direction;
    }

    public static boolean isDirection(final int direction) {
        for (SwipeDirection dir : SwipeDirection.values()) {
            if (dir.getValue() == direction) {
                return true;
            }
        }
        return false;
    }

    public static SwipeDirection fromValue(final int direction) {
        for (SwipeDirection dir : SwipeDirection.values()) {
            if (dir.getValue() == direction) {
                return dir;
            }
        }
        throw new IllegalArgumentException("No matching SwipeDirection");
    }
}
