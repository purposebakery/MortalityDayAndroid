package de.techlung.android.mortalityday;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import de.techlung.android.mortalityday.logger.ExceptionLogger;


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

        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initDrawer();

    }

    private void initExceptionLogger() {
        logger = new ExceptionLogger(this);
        Thread.setDefaultUncaughtExceptionHandler(logger);
        logger.handlePastExceptions();
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main);

        drawerLayout.findViewById(R.id.drawer_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MortalityDayActivity.this, "Settings", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
