package de.techlung.android.mortalityday.greendao.migration;

import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractMigrationHelper {

    public static final String STRING = "TEXT";
    public static final String INTEGER = "INTEGER";
    public static final String DOUBLE = "REAL";
    public static final String BOOLEAN = "INTEGER";

    public abstract void onUpgrade(SQLiteDatabase db);
}