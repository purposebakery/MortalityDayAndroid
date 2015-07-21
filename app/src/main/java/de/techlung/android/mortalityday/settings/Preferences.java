package de.techlung.android.mortalityday.settings;

import com.pixplicity.easyprefs.library.Prefs;

import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.R;
import de.techlung.android.mortalityday.enums.Frequency;
import de.techlung.android.mortalityday.enums.WeekDay;

public class Preferences {
    private static final String FREQUENCY = "KEY_FREQENCY";
    private static final String DAY1 = "KEY_DAY1";
    private static final String DAY2 = "KEY_DAY2";
    private static final String USER_NAME = "KEY_USER_NAME";
    private static final String DEVICE_ID = "KEY_DEVICE_ID";

    public static String getDeviceId() {
        return Prefs.getString(DEVICE_ID, null);
    }

    public static void setDeviceId(String deviceId) {
        Prefs.putString(DEVICE_ID, deviceId);
    }

    public static Frequency getFrequency() {
        return Frequency.valueOf(Prefs.getString(FREQUENCY, Frequency.ONCE_A_WEEK.name()));
    }

    public static WeekDay getDay1() {
        return WeekDay.valueOf(Prefs.getString(DAY1, WeekDay.SATURDAY.name()));
    }

    public static WeekDay getDay2() {
        return WeekDay.valueOf(Prefs.getString(DAY2, WeekDay.THURSDAY.name()));
    }

    public static String getUserName() {
        return Prefs.getString(USER_NAME, "");
    }


}
