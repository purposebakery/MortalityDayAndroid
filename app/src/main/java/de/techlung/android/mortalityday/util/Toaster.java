package de.techlung.android.mortalityday.util;

import android.widget.Toast;

import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.R;

public class Toaster {

    public static void show(String message) {
        Toast.makeText(MainActivity.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showNetworkError() {
        Toast.makeText(MainActivity.getInstance(), MainActivity.getInstance().getString(R.string.error_no_network), Toast.LENGTH_SHORT).show();
    }
}
