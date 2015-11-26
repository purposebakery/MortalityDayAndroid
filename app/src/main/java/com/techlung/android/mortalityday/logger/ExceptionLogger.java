package com.techlung.android.mortalityday.logger;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ExceptionLogger implements Thread.UncaughtExceptionHandler{
    private static final String EXCEPTION_LOG_NAME = "mortalityday_exception";

    private Activity activity;
    private Thread.UncaughtExceptionHandler originalHandler;

    public ExceptionLogger(Activity activity) {
        this.activity = activity;

        originalHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        File trgDir = activity.getExternalFilesDir(null);

        if (trgDir == null) {
            originalHandler.uncaughtException(thread, ex);
            return;
        }

        if (!trgDir.exists()) {
            trgDir.mkdirs();
        }

        SimpleDateFormat s = new SimpleDateFormat("yy.MM.dd-hh.mm.ss", Locale.US);
        String time = s.format(new Date());

        File logFile = new File(trgDir.getAbsolutePath() + "/" + time + "_"+EXCEPTION_LOG_NAME+".txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String model = android.os.Build.MODEL;
        String version = android.os.Build.VERSION.RELEASE;
        PrintWriter out;
        try {
            out = new PrintWriter(logFile.getAbsolutePath());
            out.println("model=" + model);
            out.println("version=" + version);

            ex.printStackTrace(out);

            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ex.printStackTrace();

        final String errorLogMessage = "Error log written to: " + logFile.getAbsolutePath();
        Log.e(ExceptionLogger.class.getName(), errorLogMessage);

        originalHandler.uncaughtException(thread, ex);
    }

    public static void createNullpointerExeption () {
        String name = "";
        name = null;
        Log.d(ExceptionLogger.class.getName(), "" + name.length());
    }
}
