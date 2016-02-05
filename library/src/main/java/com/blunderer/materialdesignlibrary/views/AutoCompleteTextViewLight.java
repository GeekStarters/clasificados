package com.blunderer.materialdesignlibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.blunderer.materialdesignlibrary.common.TypefaceUtil;


public class AutoCompleteTextViewLight extends AutoCompleteTextView {
    public AutoCompleteTextViewLight(Context context) {
        super(context);
    }

    public AutoCompleteTextViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCompleteTextViewLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTypeface(TypefaceUtil.getFontLight(getContext()));
    }
}
