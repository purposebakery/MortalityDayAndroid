package de.techlung.android.mortalityday.notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.Calendar;

import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.R;
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
        WeekDay weekDay = MortalityDayUtil.getNextMortalityDay();
        // TODO get time of next Mortality Day!

        return Calendar.getInstance().getTimeInMillis();
    }

    public class NotifyMortalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            // TODO check whether we're in a Mortality day now!

            if (MortalityDayUtil.isMortalityDay()) {
                showNotification(context);
            }


            setNextNotification(context);

            // do the thing in here
            // including figuring out the next time you want to run
            // and scheduling another PendingIntent with the AlarmManager
        }
    }

    private void showNotification(Context context) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1000, mBuilder.build());
    }

}
