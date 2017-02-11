package com.aminiam.moviekade.utility;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.aminiam.moviekade.R;

public class Utility {
    public static int calculateNoOfColumns(Context context) {
        if(context == null) {
            return 2;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return  (int) (dpWidth / 180);
    }

    public static float convertPixelsToDp(Context context, float px){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static int convertDpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String convertMinToHour(Context context, int timeInMin) {
        int hour = timeInMin / 60;
        int min = timeInMin % 60;

        String strHour = String.valueOf(hour);
        String strMin = String.valueOf(min);

        if(hour < 10) {
            strHour = "0" + strHour;
        }
        if(min < 10) {
            strMin = "0" + min;
        }

        return String.format(context.getString(R.string.time), strHour, strMin);
    }
}
