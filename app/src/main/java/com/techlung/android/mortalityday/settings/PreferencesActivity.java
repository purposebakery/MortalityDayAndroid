package com.techlung.android.mortalityday.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.techlung.android.mortalityday.BaseActivity;
import com.techlung.android.mortalityday.MessageActivity;
import com.techlung.android.mortalityday.R;
import com.techlung.android.mortalityday.notification.MortalityDayNotificationManager;
import com.techlung.android.mortalityday.util.MortalityDayUtil;

public class PreferencesActivity extends BaseActivity {
    public static final String CALLED_INTERNAL = "CALLED_INTERNAL";

    boolean skipped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.preferences_activity);

        skipped = false;

        checkFirstStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!skipped) {
            MortalityDayNotificationManager.setNextNotification(this, true);
        }
    }

    private void checkFirstStart() {
        if (Preferences.getFirstStart()) {
            Preferences.setFirstStart(false);

            showFirstStartMessage();
        } else if (MortalityDayUtil.isMortalityDay() && !getIntent().getBooleanExtra(CALLED_INTERNAL, false)) {
            skipped = true;
            Intent messageStart = new Intent(this, MessageActivity.class);
            startActivity(messageStart);
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
