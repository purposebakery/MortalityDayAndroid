package de.techlung.android.mortalityday.greendao.extended;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.files.FileHandler;
import de.techlung.android.mortalityday.greendao.generated.DaoMaster;
import de.techlung.android.mortalityday.greendao.generated.ThoughtDao;
import de.techlung.android.mortalityday.greendao.migration.mortalityday.MortalityDayOpenHelper;

public class DaoFactory {
    protected DaoMaster daoMaster;
    protected SQLiteDatabase db;

    private Context context;

    private ExtendedThoughtDao extendedThoughtDao;

    private static DaoFactory instance;

    // Bewegungsdaten
    public static DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
            instance.context = MainActivity.getInstance();
            instance.reinitialiseDb();
        }
        return instance;
    }


    public void reinitialiseDb() {
        if (db == null) {
            makeSureDbIsInitialised();
        } else {
            closeDb();
            makeSureDbIsInitialised();
        }
    }

    public void makeSureDbIsInitialised() {
        if (db == null) {

            String dbFilePath = getDbFilePath();
            boolean dbFileExisted = (new File(dbFilePath)).exists();

            if (dbFileExisted) {
                migrateIfNecessary();
            }

            db = context.openOrCreateDatabase(dbFilePath, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            daoMaster = new DaoMaster(db);

            if (!dbFileExisted) {
                recreateDb();
            }
        }
    }

    private void migrateIfNecessary() {
        MortalityDayOpenHelper openHelper = new MortalityDayOpenHelper(context, getDbFilePath(), null);
        SQLiteDatabase databaseTemp = openHelper.getWritableDatabase(); // trigger upgrade
        databaseTemp.close();
    }

    private String getDbFilePath() {

        return FileHandler.getDBFile(context).getAbsolutePath();
    }

    public void closeDb() {
        if (db != null) {
            db.close();
            db = null;
            daoMaster = null;

            extendedThoughtDao = null;
        }
    }

    public void clearDb() {
        makeSureDbIsInitialised();

        db.beginTransaction();

        try {
            getExtendedThoughtDao().deleteAll();

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void recreateDb() {
        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, true);
    }


    public ExtendedThoughtDao getExtendedThoughtDao() {
        if (extendedThoughtDao == null) {
            ThoughtDao thoughtDao = daoMaster.newSession().getThoughtDao();
            extendedThoughtDao = new ExtendedThoughtDao(thoughtDao);
        }
        return extendedThoughtDao;
    }

}
