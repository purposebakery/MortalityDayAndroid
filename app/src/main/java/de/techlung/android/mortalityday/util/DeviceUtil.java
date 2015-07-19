package de.techlung.android.mortalityday.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceUtil {
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
