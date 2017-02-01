package com.aminiam.moviekade.utility;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Utility {
    public static int calculateNoOfColumns(Context context) {
        if(context == null) {
            return 2;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return  (int) (dpWidth / 180);
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
