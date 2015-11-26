package com.techlung.android.mortalityday.settings;

import android.content.Context;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;

import com.techlung.android.mortalityday.enums.Frequency;

public class Preferences {
    private static final String FIRST_START = "FIRST_START";

    private static final String NOTIFY = "KEY_NOTIFY";

    private static final String FREQUENCY = "KEY_FREQENCY";
    private static final String DAY1 = "KEY_DAY1";
    private static final String DAY2 = "KEY_DAY2";

    private static final String USER_NAME = "USER_NAME";
    private static final String USER_NAME_DEFAULT = "Anonymous";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String USER_PASSWORD_DEFAULT = "Anonymous";

    private static final String KEY_AUTOMATIC_SHARING = "KEY_AUTOMATIC_SHARING";

    private static boolean isInited = false;


    public static boolean isNotifyEnabled() {
        return Prefs.getBoolean(NOTIFY, true);
    }

    public static Frequency getFrequency() {
        return Frequency.valueOf(Prefs.getString(FREQUENCY, Frequency.ONCE_A_WEEK.name()));
    }

    public static int getDay1() {
        return Integer.parseInt(Prefs.getString(DAY1, "7"));
    }

    public static int getDay2() {
        return Integer.parseInt(Prefs.getString(DAY2, "5"));
    }


    public static void setFirstStart(boolean firstStart) {
        Prefs.putBoolean(FIRST_START, firstStart);
    }

    public static boolean getFirstStart() {
        return Prefs.getBoolean(FIRST_START, true);
    }

    public static void initPreferences(Context context) {
        if (isInited) {
            return;
        }

        isInited = true;

        new Prefs.Builder()
                .setContext(context)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(context.getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
