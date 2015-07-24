package de.techlung.android.mortalityday.greendao.extended;

import android.content.Context;

import java.io.File;

public class FileHandler {
    private FileHandler() {

    }

    public static File getDBFile(Context context) {
        File file = new File(context.getExternalFilesDir(null), "mortalityday.db");
        return file;
    }
}
