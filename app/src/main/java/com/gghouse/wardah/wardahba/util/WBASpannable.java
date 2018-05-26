package com.gghouse.wardah.wardahba.util;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by michaelhalim on 2/19/17.
 */

public class WBASpannable extends ClickableSpan {
    private boolean isUnderline = true;

    /**
     * Constructor
     */
    public WBASpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#1b76d3"));
    }

    @Override
    public void onClick(View widget) {


    }
}
