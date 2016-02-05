package com.blunderer.materialdesignlibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

import com.blunderer.materialdesignlibrary.common.TypefaceUtil;

public class CheckedTextViewLight extends CheckedTextView {
    public CheckedTextViewLight(Context context) {
        super(context);
    }

    public CheckedTextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckedTextViewLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTypeface(TypefaceUtil.getFontLight(getContext()));
    }
}
