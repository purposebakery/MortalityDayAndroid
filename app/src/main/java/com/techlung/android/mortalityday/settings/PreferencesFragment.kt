package com.techlung.android.mortalityday.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import com.techlung.android.mortalityday.R
import com.techlung.android.mortalityday.enums.Frequency

class PreferencesFragment : PreferenceFragmentCompat() {

    private var frequencyPreference: ListPreference? = null
    private var day1Preference: ListPreference? = null
    private var day2Preference: ListPreference? = null
    private var themePreference: ListPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences)

        frequencyPreference = findPreference<ListPreference>(getString(R.string.prefs_day_fequency_key))
        day1Preference = findPreference<ListPreference>(getString(R.string.prefs_day_day1_key))
        day2Preference = findPreference<ListPreference>(getString(R.string.prefs_day_day2_key))
        themePreference = findPreference<ListPreference>(getString(R.string.prefs_theme_key))

        frequencyPreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            updateDayVisibility(Frequency.valueOf(newValue.toString()))
            true
        }

        themePreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
            (activity as PreferencesActivity).updateTheme()
            true
        }

        frequencyPreference?.run {
            updateDayVisibility(Frequency.valueOf(this.value))
        }
    }

    private fun updateDayVisibility(newFrequency: Frequency?) {
        when (newFrequency) {
            Frequency.ONCE_A_WEEK -> {
                day1Preference?.isEnabled = true
                day2Preference?.isEnabled = false
            }
            Frequency.TWICE_A_WEEK -> {
                day1Preference?.isEnabled = true
                day2Preference?.isEnabled = true
            }
            else -> {
                day1Preference?.isEnabled = false
                day2Preference?.isEnabled = false
            }
        }
    }
}