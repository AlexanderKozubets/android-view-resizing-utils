package com.alexkozubets.sample;


import android.content.res.Resources;

public final class DimenUtil {

    private static final float density = Resources.getSystem().getDisplayMetrics().density;

    public static int dpToPx(float dp) {
        return Math.round(dp * density);
    }

    public static float pxToDp(int px) {
        return Float.valueOf(px) / density;
    }

    private DimenUtil() {
        throw new IllegalAccessError();
    }
}
