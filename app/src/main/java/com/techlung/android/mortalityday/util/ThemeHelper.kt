package com.techlung.android.mortalityday.util

import com.techlung.android.mortalityday.enums.Theme
import com.techlung.android.mortalityday.settings.Preferences

object ThemeHelper {
    fun applyTheme(executeLight: () -> Unit, executeDark: () -> Unit) {
        when (Preferences.theme) {
            Theme.LIGHT -> executeLight.invoke()
            Theme.DARK -> executeDark.invoke()
        }
    }
}