package com.techlung.android.mortalityday.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.techlung.android.mortalityday.R
import com.techlung.android.mortalityday.settings.Preferences
import com.techlung.android.mortalityday.util.MortalityDayUtil
import com.techlung.android.mortalityday.util.Toaster
import java.text.SimpleDateFormat
import java.util.*

object MortalityDayNotificationManager {

    private val nextNotificationTime: Long
        get() {
            val day = MortalityDayUtil.nextMortalityDay
            return day.timeInMillis
        }

    fun setNextNotification(context: Context, showToast: Boolean) {
        if (!Preferences.isNotifyEnabled) {
            return
        }

        val alarmIntent = Intent(context, NotifyMortalReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextNotificationTime = nextNotificationTime

        alarmManager.set(AlarmManager.RTC, nextNotificationTime, pendingIntent)
        if (showToast) {
            toastNextNotificationTime(nextNotificationTime, context)
        }
    }

    private fun toastNextNotificationTime(time: Long, context: Context) {
        val format = SimpleDateFormat("EEEE dd.MM.yyyy", Locale.ENGLISH)
        val date = Date()
        date.time = time

        Toaster.show(context.getString(R.string.next_day) + "\n" + format.format(date), context)
    }


}
