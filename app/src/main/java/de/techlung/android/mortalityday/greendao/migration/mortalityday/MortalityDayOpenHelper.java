package de.techlung.android.mortalityday.greendao.migration.mortalityday;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import de.techlung.android.mortalityday.greendao.migration.AbstractOpenHelper;


public class MortalityDayOpenHelper extends AbstractOpenHelper {

    public MortalityDayOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public String getMigrationHelperClassName() {
        return "de.techlung.android.mortalityday.greendao.migration.mortalityday";
    }
}