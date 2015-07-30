package de.techlung.android.mortalityday;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baasbox.android.BaasBox;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.LogLevel;

import de.techlung.android.mortalityday.baasbox.Constants;
import de.techlung.android.mortalityday.logger.ExceptionLogger;
import de.techlung.android.mortalityday.settings.Preferences;

import static com.orhanobut.hawk.HawkBuilder.EncryptionMethod;
import static com.orhanobut.hawk.HawkBuilder.newSqliteStorage;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getName();

    protected ExceptionLogger logger;
    protected BaasBox baasbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initExceptionLogger();
        initPreferences();
        initSafePreferences();
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
        Preferences.initPreferences(this);
    }

    private void initSafePreferences() {
        Hawk.init(this)
                .setEncryptionMethod(EncryptionMethod.HIGHEST)
                .setStorage(newSqliteStorage(this))
                .setLogLevel(LogLevel.FULL)
                .build();
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
