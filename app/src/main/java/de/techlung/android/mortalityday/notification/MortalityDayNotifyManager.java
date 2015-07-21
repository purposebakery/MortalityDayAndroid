package de.techlung.android.mortalityday.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.techlung.android.mortalityday.enums.WeekDay;
import de.techlung.android.mortalityday.util.MortalityDayUtil;

public class MortalityDayNotifyManager {

    public void setNextNotification(Context context) {
        Intent alarmIntent = new Intent(context, NotifyMortalReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, getNextNotificationTime(), pendingIntent);
    }

    private long getNextNotificationTime() {
        WeekDay weekDay = MortalityDayUtil.getWeekDay();
        // TODO get time of next Mortality Day!

        return Calendar.getInstance().getTimeInMillis();
    }

    public class NotifyMortalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            // TODO check whether we're in a Mortality day now!
            // / Check if w


            // do the thing in here
            // including figuring out the next time you want to run
            // and scheduling another PendingIntent with the AlarmManager
        }
    }
}
