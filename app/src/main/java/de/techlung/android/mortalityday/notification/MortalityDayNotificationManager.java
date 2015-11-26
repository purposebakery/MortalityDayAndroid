package de.techlung.android.mortalityday.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.techlung.android.mortalityday.R;
import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.util.MortalityDayUtil;
import de.techlung.android.mortalityday.util.Toaster;

public class MortalityDayNotificationManager {

    public static void setNextNotification(Context context, boolean showToast) {
        if (!Preferences.isNotifyEnabled()) {
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

        Toaster.show(context.getString(R.string.next_day) + "\n" + format.format(date), context);
    }

    private static long getNextNotificationTime() {
        Calendar day = MortalityDayUtil.getNextMortalityDay();
        return day.getTimeInMillis();
    }





}
