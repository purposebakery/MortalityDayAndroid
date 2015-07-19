package de.techlung.android.mortalityday;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pixplicity.easyprefs.library.Prefs;

import de.techlung.android.mortalityday.logger.ExceptionLogger;
import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.settings.PreferencesActivity;
import de.techlung.android.mortalityday.util.DeviceUtil;


public class MortalityDayActivity  extends AppCompatActivity {
    public static final String TAG = MortalityDayActivity.class.getName();
    public static final boolean DEBUG = false;

    private ExceptionLogger logger;

    private DrawerLayout drawerLayout;

    private static MortalityDayActivity instance;
    public static MortalityDayActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        initExceptionLogger();
        initPreferences();

        setContentView(R.layout.main_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initDrawer();

    }

    private void initExceptionLogger() {
        logger = new ExceptionLogger(this);
        Thread.setDefaultUncaughtExceptionHandler(logger);
        logger.handlePastExceptions();
    }

    private void initPreferences() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        if (Preferences.getDeviceId() == null) {
            Preferences.setDeviceId(DeviceUtil.getDeviceId(this));
        }
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main);

        drawerLayout.findViewById(R.id.drawer_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MortalityDayActivity.this, PreferencesActivity.class));
            }
        });
    }


}
