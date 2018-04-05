package com.example.teachergradebook.util;

import android.text.format.DateUtils;

/**
 * Created by Денис on 19.03.2018.
 */

public class DateTimeUtils {

    public static String formatRelativeTime(long time) {
        return DateUtils.getRelativeTimeSpanString(time * 1000, System.currentTimeMillis(),
                android.text.format.DateUtils.MINUTE_IN_MILLIS).toString();
    }
}
