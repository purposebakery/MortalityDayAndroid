package de.techlung.android.mortalityday.greendao.migration.mortalityday;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import de.techlung.android.mortalityday.greendao.migration.AbstractMigrationHelper;


public class MigrationHelperV1toV2 extends AbstractMigrationHelper {
    @Override
    public void onUpgrade(SQLiteDatabase db) {
        // Added colum Hole to Partfield
        //String command1 = "ALTER TABLE " + PartfieldPointDao.TABLENAME + " ADD COLUMN '" + PartfieldPointDao.Properties.Collection.columnName + "' " + INTEGER;
        //Log.d(MigrationHelperV1toV2.class.getName(), command1);
        //db.execSQL(command1);
    }
}
