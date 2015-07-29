package de.techlung.android.mortalityday.util;

import android.widget.Toast;

import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.R;

public class Toaster {

    public static void show(String message) {
        if (MainActivity.getInstance() != null) {
            Toast.makeText(MainActivity.getInstance(), message, Toast.LENGTH_LONG).show();
        }
    }

    public static void showNetworkError() {
        if (MainActivity.getInstance() != null) {
            Toast.makeText(MainActivity.getInstance(), MainActivity.getInstance().getString(R.string.error_no_network), Toast.LENGTH_SHORT).show();
        }
    }
}
