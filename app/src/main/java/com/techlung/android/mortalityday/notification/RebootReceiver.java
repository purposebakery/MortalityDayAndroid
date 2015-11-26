package com.techlung.android.mortalityday.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.techlung.android.mortalityday.settings.Preferences;

public class RebootReceiver extends BroadcastReceiver {

    public RebootReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Preferences.initPreferences(context);

        MortalityDayNotificationManager.setNextNotification(context, true);
    }
}