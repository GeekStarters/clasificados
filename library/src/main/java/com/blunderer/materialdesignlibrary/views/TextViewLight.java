package com.blunderer.materialdesignlibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.common.TypefaceUtil;


public class TextViewLight extends TextView {
    public TextViewLight(Context context) {
        super(context);
    }

    public TextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTypeface(TypefaceUtil.getFontLight(getContext()));
    }
}