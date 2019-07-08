package com.techlung.android.mortalityday.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.techlung.android.mortalityday.MessageActivity;
import com.techlung.android.mortalityday.R;
import com.techlung.android.mortalityday.settings.Preferences;
import com.techlung.android.mortalityday.settings.PreferencesActivity;
import com.techlung.android.mortalityday.util.MortalityDayUtil;

public class NotifyMortalReceiver extends BroadcastReceiver {
    public static final String ANDROID_CHANNEL_ID = "com.techlung.android.mortalityday.Notifications";
    public static final String ANDROID_CHANNEL_NAME = "Regular Reminders";

    public NotifyMortalReceiver() {

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Preferences.initPreferences(context);

        if (MortalityDayUtil.isMortalityDay()) {
            showNotification(context);
        }

        MortalityDayNotificationManager.setNextNotification(context, false);
    }

    public static void showNotification(Context context) {

        if (!Preferences.isNotifyEnabled()) {
            return;
        }

        NotificationManager notificationManager = getManager(context);

        MortalityDayUtil.MortalityDayQuote quote = MortalityDayUtil.getQuote(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(context);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setAutoCancel(true)
                        .setContentText(quote.message);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MessageActivity.class);
        resultIntent.putExtra(MessageActivity.MESSAGE_EXTRA, quote.message);
        resultIntent.putExtra(MessageActivity.AUTHOR_EXTRA, quote.author);

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
        mBuilder.setContentIntent(resultPendingIntent);



        notificationManager.notify(1000, mBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(ANDROID_CHANNEL_ID, ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.GREEN);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        getManager(context).createNotificationChannel(channel);
    }

    private static NotificationManager getManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

}
