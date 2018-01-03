package com.foodBasket.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.foodBasket.MyApplication;

/**
 * Created by "" on 2017/4/2
 */

public final class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = MyApplication.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = MyApplication.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
