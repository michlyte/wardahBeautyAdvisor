package com.gghouse.wardah.wardahba.util;

import android.graphics.Color;

import com.gghouse.wardah.wardahba.common.WBAProperties;

/**
 * Created by michaelhalim on 2/25/17.
 */

public abstract class WBAColor {
    public static int getContainerColor(Integer score) {
        int red = (255 * (WBAProperties.MAX_SCORE - score)) / WBAProperties.MAX_SCORE;
        int green = (255 * score) / WBAProperties.MAX_SCORE;
        int blue = 0;
        return Color.rgb(red, green, blue);
    }
}
