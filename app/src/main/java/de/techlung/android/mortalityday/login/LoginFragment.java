package de.techlung.android.mortalityday.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.RequestToken;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.techlung.android.mortalityday.R;
import de.techlung.android.mortalityday.settings.Preferences;

public class LoginFragment extends DialogFragment {
    public static final String TAG = LoginFragment.class.getName();

    @Bind(R.id.login_login) Button login;
    @Bind(R.id.login_register) Button register;
    @Bind(R.id.login_username) EditText username;
    @Bind(R.id.login_password) EditText password;

    private RequestToken mSignupOrLogin;
    private ProgressDialog progressDialog;

    String usernameString;
    String passwordString;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login, container, false);
        ButterKnife.bind(view);

        username.setText(Preferences.getUserName());
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    attemptLogin(false);
                    return true;
                }
                return false;
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(true);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(false);
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSignupOrLogin!=null){
            showProgress(false);
            mSignupOrLogin.suspend();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSignupOrLogin!=null){
            showProgress(true);
            mSignupOrLogin.resume(onComplete);
        }
    }

    private void completeLogin(boolean success){
        showProgress(false);
        mSignupOrLogin = null;
        if (success) {
            dismiss();
        } else {
            password.setError(getString(R.string.login_password_incorrect));
            password.requestFocus();
        }
    }


    public void attemptLogin(boolean newUser) {
        // Reset errors.
        username.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        usernameString = username.getText().toString();
        passwordString = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(passwordString)) {
            password.setError(getString(R.string.login_field_required));
            focusView = password;
            cancel = true;
        } else if (passwordString.length() < 4) {
            password.setError(getString(R.string.login_password_incorrect));
            focusView = password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(usernameString)) {
            username.setError(getString(R.string.login_field_required));
            focusView = username;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            signupWithBaasBox(newUser);
        }
    }

    private void signupWithBaasBox(boolean newUser){
        //todo 3.1
        BaasUser user = BaasUser.withUserName(usernameString);
        user.setPassword(passwordString);
        if (newUser) {
            mSignupOrLogin=user.signup(onComplete);
        } else {
            mSignupOrLogin=user.login(onComplete);
        }
    }

    //todo 3.2
    private final BaasHandler<BaasUser> onComplete =
            new BaasHandler<BaasUser>() {
                @Override
                public void handle(BaasResult<BaasUser> result) {

                    mSignupOrLogin = null;
                    if (result.isFailed()){
                        Log.d("ERROR", "ERROR", result.error());
                    }
                    completeLogin(result.isSuccess());
                }
            };

    private void showProgress(final boolean show) {
        if (show) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
            }
            progressDialog.show();
        } else {
            if (progressDialog != null) {
                progressDialog.hide();
            }
        }
    }
}
