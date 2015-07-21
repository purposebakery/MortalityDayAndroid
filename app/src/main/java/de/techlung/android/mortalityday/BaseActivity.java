package de.techlung.android.mortalityday;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.baasbox.android.BaasBox;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.RequestToken;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import de.techlung.android.mortalityday.baasbox.Constants;
import de.techlung.android.mortalityday.logger.ExceptionLogger;
import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.util.DeviceUtil;
import de.techlung.android.mortalityday.util.Toaster;

public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getName();

    protected ExceptionLogger logger;
    protected BaasBox baasbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initExceptionLogger();
        initPreferences();
        initBackend();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopBackend();
    }

    private void initExceptionLogger() {
        logger = new ExceptionLogger(this);
        Thread.setDefaultUncaughtExceptionHandler(logger);
        logger.handlePastExceptions();
    }

    private void initPreferences() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        if (Preferences.getDeviceId() == null) {
            Preferences.setDeviceId(DeviceUtil.getDeviceId(this));
        }
    }

    private void initBackend() {
        BaasBox.Builder b = new BaasBox.Builder(this);
        baasbox = b
                .setAuthentication(BaasBox.Config.AuthType.SESSION_TOKEN)
                .setApiDomain(Constants.BAASBOX_URL)
                .setPort(9000)
                .setAppCode(Constants.BAASBOX_APP_CODE)
                .init();

//        BaasBox.Config config = new BaasBox.Config();
//        config.authenticationType= BaasBox.Config.AuthType.SESSION_TOKEN;
//        config.apiDomain = "192.168.56.1"; // the host address
//        config.httpPort=9000;
//        BaasBox.initDefault(this,config);

    }

    private void stopBackend() {/*
        BaasUser user = BaasUser.current();
        if (user != null) {
            user.logout(new BaasHandler<Void>() {
                @Override
                public void handle(BaasResult<Void> baasResult) {
                    Log.d(TAG, baasResult.toString());
                }
            });
        }*/
    }
}
