package de.techlung.android.mortalityday.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;

import java.security.spec.ECField;

import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.R;

public class AlertUtil {

    public static void askUserConfirmation(int messageId, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());
        builder.setTitle(R.string.alert_warning);
        builder.setMessage(messageId);

        builder.setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void showInfoAlert() {
        Activity context = MainActivity.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.info_title);

        String message = context.getString(R.string.info_message);
        builder.setIcon(R.drawable.logo);

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            message = message.replace("#TAG#", version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        builder.setMessage(message);

        builder.setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }
}
