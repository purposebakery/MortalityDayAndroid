package de.techlung.android.mortalityday.settings;

import com.orhanobut.hawk.Hawk;
import com.pixplicity.easyprefs.library.Prefs;

import de.techlung.android.mortalityday.enums.Frequency;

public class Preferences {
    private static final String FIRST_START = "FIRST_START";

    private static final String FREQUENCY = "KEY_FREQENCY";
    private static final String DAY1 = "KEY_DAY1";
    private static final String DAY2 = "KEY_DAY2";
    private static final String DEVICE_ID = "KEY_DEVICE_ID";

    private static final String USER_NAME = "USER_NAME";
    private static final String USER_NAME_DEFAULT = "Anonymous";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String USER_PASSWORD_DEFAULT = "Anonymous";

    private static final String KEY_AUTOMATIC_SHARING = "KEY_AUTOMATIC_SHARING";

    public static String getDeviceId() {
        return Prefs.getString(DEVICE_ID, null);
    }

    public static void setDeviceId(String deviceId) {
        Prefs.putString(DEVICE_ID, deviceId);
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

    public static boolean isAutomaticSharing() {
        return Prefs.getBoolean(KEY_AUTOMATIC_SHARING, false);
    }

    public static boolean isAdmin() {
        if (getUserName() != null && getUserName().equals("Oliver")) {
            return true;
        }
        return false;
    }

    public static void setUserName(String userName) {
        Hawk.put(USER_NAME, userName);
    }

    public static String getUserName() {
        return Hawk.get(USER_NAME, USER_NAME_DEFAULT);
    }

    public static void setUserPassword(String userPassword) {
        Hawk.put(USER_PASSWORD, userPassword);
    }

    public static String getUserPassword() {
        return Hawk.get(USER_PASSWORD, USER_PASSWORD_DEFAULT);
    }

    public static void setFirstStart(boolean firstStart) {
        Prefs.putBoolean(FIRST_START, firstStart);
    }

    public static boolean getFirstStart() {
        return Prefs.getBoolean(FIRST_START, true);
    }

}
