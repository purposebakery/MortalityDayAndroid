package com.techlung.android.mortalityday.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.techlung.android.mortalityday.R;
import com.techlung.android.mortalityday.settings.Preferences;
import com.techlung.android.mortalityday.util.MortalityDayUtil;
import com.techlung.android.mortalityday.util.Toaster;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MortalityDayNotificationManager {

    public static void setNextNotification(Context context, boolean showToast) {
        if (!Preferences.INSTANCE.isNotifyEnabled()) {
            return;
        }

        Intent alarmIntent = new Intent(context, NotifyMortalReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long nextNotificationTime = getNextNotificationTime();

        alarmManager.set(AlarmManager.RTC, nextNotificationTime, pendingIntent);
        if (showToast) {
            toastNextNotificationTime(nextNotificationTime, context);
        }
    }

    private static void toastNextNotificationTime(long time, Context context) {
        DateFormat format = new SimpleDateFormat("EEEE dd.MM.yyyy", Locale.ENGLISH);
        Date date = new Date();
        date.setTime(time);

        Toaster.INSTANCE.show(context.getString(R.string.next_day) + "\n" + format.format(date), context);
    }

    private static long getNextNotificationTime() {
        Calendar day = MortalityDayUtil.INSTANCE.getNextMortalityDay();
        return day.getTimeInMillis();
        //return (new Date()).getTime() + 1000*60*2;
    }


}
