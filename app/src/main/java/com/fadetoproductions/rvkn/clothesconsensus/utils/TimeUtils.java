package com.fadetoproductions.rvkn.clothesconsensus.utils;

import java.text.DecimalFormat;

/**
 * Created by rnewton on 9/17/16.
 */
public class TimeUtils {

    public static String minutesToString(int minutes) {
        if (minutes < 60) {
            return Integer.toString(minutes) + "m";
        }
        int hours = minutes / 60;
        int minutesLeft = minutes % 60;
        DecimalFormat formatter = new DecimalFormat("00");
        return Integer.toString(hours) + ":" + formatter.format(minutesLeft) + "h";
    }
}
