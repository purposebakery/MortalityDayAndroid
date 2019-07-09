package com.techlung.android.mortalityday.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build

import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat

import com.techlung.android.mortalityday.MessageActivity
import com.techlung.android.mortalityday.R
import com.techlung.android.mortalityday.settings.Preferences
import com.techlung.android.mortalityday.settings.PreferencesActivity
import com.techlung.android.mortalityday.util.MortalityDayUtil

class NotifyMortalReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Preferences.initPreferences(context)

        if (MortalityDayUtil.isMortalityDay) {
            showNotification(context)
        }

        MortalityDayNotificationManager.setNextNotification(context, false)
    }

    companion object {
        private const val ANDROID_CHANNEL_ID = "com.techlung.android.mortalityday.Notifications"
        private const val ANDROID_CHANNEL_NAME = "Regular Reminders"

        fun showNotification(context: Context) {

            if (!Preferences.isNotifyEnabled) {
                return
            }

            val notificationManager = getManager(context)

            val quote = MortalityDayUtil.getQuote(context)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(context)
            }

            val builder = NotificationCompat.Builder(context, ANDROID_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setContentTitle(context.getString(R.string.notification_title))
                    .setAutoCancel(true)
                    .setContentText(quote.message)

            val resultIntent = Intent(context, MessageActivity::class.java)
            resultIntent.putExtra(MessageActivity.MESSAGE_EXTRA, quote.message)
            resultIntent.putExtra(MessageActivity.AUTHOR_EXTRA, quote.author)

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(PreferencesActivity::class.java)
            stackBuilder.addNextIntent(resultIntent)
            val resultPendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.setContentIntent(resultPendingIntent)

            notificationManager.notify(1000, builder.build())
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun createChannel(context: Context) {
            val channel = NotificationChannel(ANDROID_CHANNEL_ID, ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lightColor = Color.GREEN
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            getManager(context).createNotificationChannel(channel)
        }

        private fun getManager(context: Context): NotificationManager {
            return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
    }

}
