package com.blunderer.materialdesignlibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.blunderer.materialdesignlibrary.common.TypefaceUtil;


public class EditTextLight extends EditText {
    public EditTextLight(Context context) {
        super(context);
    }

    public EditTextLight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTypeface(TypefaceUtil.getFontLight(getContext()));
    }
}
