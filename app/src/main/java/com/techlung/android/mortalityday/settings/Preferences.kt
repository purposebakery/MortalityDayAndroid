package com.techlung.android.mortalityday.settings

import android.content.Context
import android.content.ContextWrapper

import com.pixplicity.easyprefs.library.Prefs
import com.techlung.android.mortalityday.enums.Frequency
import com.techlung.android.mortalityday.enums.Theme

object Preferences {
    private const val FIRST_START = "FIRST_START"

    private const val NOTIFY = "KEY_NOTIFY"
    private const val THEME = "KEY_THEME"

    private const val FREQUENCY = "KEY_FREQENCY"
    private const val DAY1 = "KEY_DAY1"
    private const val DAY2 = "KEY_DAY2"

    private var isInited = false


    val isNotifyEnabled: Boolean
        get() = Prefs.getBoolean(NOTIFY, true)

    val frequency: Frequency
        get() = Frequency.valueOf(Prefs.getString(FREQUENCY, Frequency.ONCE_A_WEEK.name))

    val theme: Theme
        get() = Theme.valueOf(Prefs.getString(THEME, Theme.LIGHT.name))

    val day1: Int
        get() = Integer.parseInt(Prefs.getString(DAY1, "7"))

    val day2: Int
        get() = Integer.parseInt(Prefs.getString(DAY2, "5"))

    var firstStart: Boolean
        get() = Prefs.getBoolean(FIRST_START, true)
        set(firstStart) = Prefs.putBoolean(FIRST_START, firstStart)

    fun initPreferences(context: Context) {
        if (isInited) {
            return
        }

        isInited = true

        Prefs.Builder()
                .setContext(context)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(context.packageName)
                .setUseDefaultSharedPreference(true)
                .build()
    }
}
