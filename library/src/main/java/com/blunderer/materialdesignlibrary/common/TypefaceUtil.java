package com.blunderer.materialdesignlibrary.common;

import android.content.Context;
import android.graphics.Typeface;

/**
 * This class provides ...
 */
public final class TypefaceUtil {
    private TypefaceUtil() {
        throw new RuntimeException("TypefaceUtil constructor is not allowed");
    }

    public static Typeface getFontLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/opensans_light.ttf");
    }

    public static Typeface getFontBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/opensans_bold.ttf");
    }

    public static Typeface getFontRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/opensans_regular.ttf");
    }

}