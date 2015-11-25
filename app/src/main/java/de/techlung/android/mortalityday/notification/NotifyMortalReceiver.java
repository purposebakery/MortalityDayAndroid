package de.techlung.android.mortalityday.notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import de.techlung.android.mortalityday.MessageActivity;
import de.techlung.android.mortalityday.R;
import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.settings.PreferencesActivity;
import de.techlung.android.mortalityday.util.MortalityDayUtil;

public class NotifyMortalReceiver extends BroadcastReceiver {

    public NotifyMortalReceiver() {

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Preferences.initPreferences(context);

        if (MortalityDayUtil.isMortalityDay()) {
            showNotification(context);
        }

        MortalityDayNotificationManager.setNextNotification(context);
    }

    private void showNotification(Context context) {

        if (!Preferences.isNotifyEnabled()) {
            return;
        }

        String[] messages = context.getResources().getStringArray(R.array.notification_messages);
        String message = messages[(int) (Math.random() * (messages.length))];

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(message);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MessageActivity.class);
        resultIntent.putExtra(MessageActivity.MESSAGE_EXTRA, message);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of

// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(PreferencesActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        resultIntent.putExtra(MessageActivity.MESSAGE_EXTRA, message);
        mBuilder.setContentIntent(resultPendingIntent);
        android.app.NotificationManager mNotificationManager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.


        mNotificationManager.notify(1000,  mBuilder.build());
    }

}
