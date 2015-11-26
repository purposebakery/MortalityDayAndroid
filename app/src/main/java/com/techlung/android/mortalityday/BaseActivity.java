package com.techlung.android.mortalityday;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.techlung.android.mortalityday.logger.ExceptionLogger;
import com.techlung.android.mortalityday.settings.Preferences;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getName();

    protected ExceptionLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initExceptionLogger();
        initPreferences();
    }

    private void initExceptionLogger() {
        logger = new ExceptionLogger(this);
        Thread.setDefaultUncaughtExceptionHandler(logger);
    }

    private void initPreferences() {
        Preferences.initPreferences(this);
    }

}
