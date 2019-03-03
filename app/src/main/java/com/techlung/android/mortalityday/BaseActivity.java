package com.techlung.android.mortalityday;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.techlung.android.mortalityday.settings.Preferences;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPreferences();
    }

    private void initPreferences() {
        Preferences.initPreferences(this);
    }

}
