package de.techlung.android.mortalityday.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import de.techlung.android.mortalityday.R;
import de.techlung.android.mortalityday.notification.MortalityDayNotificationManager;

public class PreferencesActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.preferences_activity);

        checkFirstStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void checkFirstStart() {
        if (Preferences.getFirstStart()) {
            Preferences.setFirstStart(false);

            showFirstStartMessage();
        }
    }

    private void showFirstStartMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.first_start_title);
        builder.setMessage(R.string.first_start_message);
        builder.setPositiveButton(R.string.alert_thanks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
