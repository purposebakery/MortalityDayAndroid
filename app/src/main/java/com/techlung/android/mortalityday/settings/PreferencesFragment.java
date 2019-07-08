package com.techlung.android.mortalityday.settings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.techlung.android.mortalityday.R;
import com.techlung.android.mortalityday.enums.Frequency;

public class PreferencesFragment extends PreferenceFragment {
    public static final String TAG = PreferencesFragment.class.getName();

    ListPreference frequencyPreference;
    ListPreference day1Preference;
    ListPreference day2Preference;
    ListPreference themePreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        frequencyPreference = (ListPreference) findPreference(getString(R.string.prefs_day_fequency_key));
        day1Preference = (ListPreference) findPreference(getString(R.string.prefs_day_day1_key));
        day2Preference = (ListPreference) findPreference(getString(R.string.prefs_day_day2_key));
        themePreference = (ListPreference) findPreference(getString(R.string.prefs_theme_key));

        frequencyPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                updateDayVisibility(Frequency.valueOf(newValue.toString()));
                return true;
            }
        });

        themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ((PreferencesActivity) getActivity()).updateTheme();
                return true;
            }
        });

        updateDayVisibility(Frequency.valueOf(frequencyPreference.getValue()));

    }

    private void updateDayVisibility(Frequency newFrequency) {
        if (newFrequency == Frequency.ONCE_A_WEEK) {
            day1Preference.setEnabled(true);
            day2Preference.setEnabled(false);
        } else if (newFrequency == Frequency.TWICE_A_WEEK) {
            day1Preference.setEnabled(true);
            day2Preference.setEnabled(true);
        } else {
            day1Preference.setEnabled(false);
            day2Preference.setEnabled(false);
        }
    }
}