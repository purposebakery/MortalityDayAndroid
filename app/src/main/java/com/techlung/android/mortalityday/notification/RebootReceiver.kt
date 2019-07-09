package com.techlung.android.mortalityday.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.techlung.android.mortalityday.settings.Preferences

class RebootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Preferences.initPreferences(context)

        MortalityDayNotificationManager.setNextNotification(context, true)
    }
}