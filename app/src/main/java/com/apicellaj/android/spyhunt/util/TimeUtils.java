package com.apicellaj.android.spyhunt.util;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    private static final int SIXTY_SECONDS = 60;

    public static long getMillisecondsFromMinutes(int minutes) {
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    public static int getMinutesFromMilliseconds(long milliseconds) {
        return (int) TimeUnit.MILLISECONDS.toMinutes(milliseconds);
    }

    public static int getSeconds(long milliseconds) {
        return (int) TimeUnit.MILLISECONDS.toSeconds(milliseconds)
                % SIXTY_SECONDS;
    }
}
