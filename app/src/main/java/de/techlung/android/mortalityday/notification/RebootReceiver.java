package de.techlung.android.mortalityday.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootReceiver extends BroadcastReceiver {

    public RebootReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MortalityDayNotificationManager.setNextNotification(context, false);
    }
}