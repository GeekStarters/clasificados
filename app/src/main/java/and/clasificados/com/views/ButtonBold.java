package and.clasificados.com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import and.clasificados.com.common.TypefaceUtil;

public class ButtonBold extends Button {
    public ButtonBold(Context context) {
        super(context);
    }

    public ButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTypeface(TypefaceUtil.getFontBold(getContext()));
    }
}