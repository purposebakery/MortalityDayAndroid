package de.techlung.android.mortalityday.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.util.Toaster;

public class MortalityDayNotificationManager {

    public static void setNextNotification(Context context) {
        if (!Preferences.isNotifyEnabled()) {
            // TODO Display message -> will not notify because disabled
            return;
        }

        Intent alarmIntent = new Intent(context, NotifyMortalReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long nextNotificationTime = getNextNotificationTime();

        alarmManager.set(AlarmManager.RTC, nextNotificationTime, pendingIntent);
        toastNextNotificationTime(nextNotificationTime, context);
    }

    private static void toastNextNotificationTime(long time, Context context) {
        DateFormat format = new SimpleDateFormat("EEEE dd.MM.yyyy", Locale.getDefault());
        Date date = new Date();
        date.setTime(time);

        Toaster.show("Next Mortality Day:\n" + format.format(date), context);
    }

    private static long getNextNotificationTime() {
        /*
        Calendar day = MortalityDayUtil.getNextMortalityDay();
        return day.getTimeInMillis();*/

        // TODO Replace with original code
        return (new Date().getTime()) + 10000;
    }





}
