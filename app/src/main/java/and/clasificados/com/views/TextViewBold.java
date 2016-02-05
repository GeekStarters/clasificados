package and.clasificados.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import and.clasificados.com.common.TypefaceUtil;


public class TextViewBold extends TextView {
    public TextViewBold(Context context) {
        super(context);
    }

    public TextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTypeface(TypefaceUtil.getFontBold(getContext()));
    }
}