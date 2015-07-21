package de.techlung.android.mortalityday.greendao.migration;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import de.techlung.android.mortalityday.greendao.generated.DaoMaster;


public abstract class AbstractOpenHelper extends DaoMaster.OpenHelper {
    private static final String TAG = AbstractOpenHelper.class.getName();

    public AbstractOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // do not create Database
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            try {
                /* New instance of the class that migrates from i version to i++ version named DBMigratorHelper{version that the db has on this moment} */
                AbstractMigrationHelper migrationHelper = (AbstractMigrationHelper) Class.forName(getMigrationHelperClassName() + "V" + i + "toV" + (i+1)).newInstance();

                if (migrationHelper != null) {

                    /* Upgrade de db */
                    migrationHelper.onUpgrade(db);
                }

            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Could not migrate from schema from schema: " + i + " to " + i++);
                break;
            } catch (ClassCastException e) {
                Log.e(TAG, "Could not migrate from schema from schema: " + i + " to " + i++);
                break;
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Could not migrate from schema from schema: " + i + " to " + i++);
                break;
            } catch (InstantiationException e) {
                Log.e(TAG, "Could not migrate from schema from schema: " + i + " to " + i++);
                break;

            }
        }
    }

    public abstract String getMigrationHelperClassName();
}
