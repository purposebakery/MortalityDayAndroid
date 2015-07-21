package de.techlung.android.mortalityday.util;

import android.widget.Toast;

import de.techlung.android.mortalityday.MainActivity;

public class Toaster {

    public static void show(String message) {
        Toast.makeText(MainActivity.getInstance(), message, Toast.LENGTH_SHORT).show();
    }
}
