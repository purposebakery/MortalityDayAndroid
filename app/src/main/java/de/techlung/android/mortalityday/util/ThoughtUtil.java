package de.techlung.android.mortalityday.util;

import java.util.Date;

import de.techlung.android.mortalityday.settings.Preferences;

/**
 * Created by metz037 on 21.07.15.
 */
public class ThoughtUtil {
    public static String generateKey() {
        return Preferences.getDeviceId() + "-" + (new Date()).getTime();
    }
}
