package de.techlung.android.mortalityday;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.techlung.android.mortalityday.notification.MortalityDayNotificationManager;
import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.settings.PreferencesActivity;
import de.techlung.android.mortalityday.util.AlertUtil;


public class MainActivity extends BaseActivity {
    public static final String TAG = MainActivity.class.getName();
    public static final boolean DEBUG = false;

    public static final int TRANSITION_SPEED = 300;

    @Bind(R.id.main) DrawerLayout drawerLayout;

    @Bind(R.id.header_menu) View headerMenuButton;

    @Bind(R.id.drawer_settings) View drawerSettings;
    @Bind(R.id.drawer_info) View drawerInfo;

    private static MainActivity instance;
    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        setContentView(R.layout.main_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        checkFirstStart();

        ButterKnife.bind(this);

        initMenu();
        initDrawer();
    }

    private void checkFirstStart() {
        if (Preferences.getFirstStart()) {
            startActivity(new Intent(this, PreferencesActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MortalityDayNotificationManager.setNextNotification(this);
    }


    private void initMenu() {
        headerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void initDrawer() {
        drawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
            }
        });

        drawerSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
            }
        });

        drawerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtil.showInfoAlert();
            }
        });
    }

}
