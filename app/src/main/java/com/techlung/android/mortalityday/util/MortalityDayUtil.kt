package com.techlung.android.mortalityday.util

import android.content.Context
import com.techlung.android.mortalityday.R
import com.techlung.android.mortalityday.enums.Frequency
import com.techlung.android.mortalityday.settings.Preferences
import java.util.*

object MortalityDayUtil {
    val isMortalityDay: Boolean
        get() {
            val day = GregorianCalendar()
            day.time = Date()
            return isMortalityDay(day)
        }

    /**
     * Get next mortality day. skip current day if today is a mortality day.
     */
    // add one Day
    val nextMortalityDay: Calendar
        get() {
            val day = GregorianCalendar()
            day.time = Date()

            day.add(Calendar.MINUTE, 1440)

            if (Preferences.frequency != Frequency.EVERY_DAY) {
                while (!isMortalityDay(day)) {
                    day.add(Calendar.MINUTE, 1440)
                }
            }

            day.set(Calendar.HOUR_OF_DAY, 0)
            day.set(Calendar.MINUTE, 0)
            day.set(Calendar.SECOND, 0)
            day.set(Calendar.MILLISECOND, 0)
            return day
        }

    private fun isMortalityDay(day: Calendar): Boolean {
        val weekDay = day.get(Calendar.DAY_OF_WEEK)

        return if (Preferences.frequency == Frequency.ONCE_A_WEEK) {
            weekDay == Preferences.day1
        } else if (Preferences.frequency == Frequency.TWICE_A_WEEK) {
            weekDay == Preferences.day1 || weekDay == Preferences.day2
        } else {
            true
        }

    }

    fun getQuote(context: Context): MortalityDayQuote {
        val messages = context.resources.getStringArray(R.array.notification_messages)
        val authors = context.resources.getStringArray(R.array.notification_authors)

        val index = (Math.random() * messages.size).toInt()
        val quote = MortalityDayQuote()

        quote.message = messages[index]
        quote.author = authors[index]

        return quote
    }

    class MortalityDayQuote {
        var message: String? = null
        var author: String? = null
    }
}
