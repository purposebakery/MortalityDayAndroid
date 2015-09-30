package de.techlung.android.mortalityday.baasbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasException;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

import java.util.ArrayList;
import java.util.List;

import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.R;
import de.techlung.android.mortalityday.greendao.generated.Thought;
import de.techlung.android.mortalityday.util.Toaster;

public class BaasBoxMortalityDay {
    private static final String TAG = BaasBoxMortalityDay.class.getName();

    private static List<String> sendingThoughtKeys = new ArrayList<String>();

    public static void getAllThoughts(@Nullable final GetAllThoughtsListener listener) {
        checkUserAuthenticatedAndDisplayErrorMessage();

        BaasDocument.fetchAll(Constants.THOUGHTS, new BaasHandler<List<BaasDocument>>() {
            @Override
            public void handle(BaasResult<List<BaasDocument>> baasResult) {
                if (baasResult.isSuccess()) {
                    try {
                        baasResult.get();
                        //ThoughtManager.clearSharedThoughts();
                        //ThoughtManager.addSharedThoughts(baasResult.get());

                        //ThoughtManager.processSharedThoughtsChanged();
                    } catch (BaasException e) {
                        Toaster.showNetworkError();
                        Log.e(TAG, e.toString());
                    }
                } else {
                    Toaster.showNetworkError();
                    Log.e(TAG, baasResult.toString());
                }

                if (listener != null) {
                    listener.onGetAllThoughts(baasResult.isSuccess());
                }
            }
        });
    }

    public interface GetAllThoughtsListener {
        void onGetAllThoughts(boolean success);
    }

    public static void sendThought(final Thought thought) {
        checkUserAuthenticatedAndDisplayErrorMessage();

        if (sendingThoughtKeys.contains(thought.getKey())) {
            return;
        }

        sendingThoughtKeys.add(thought.getKey());

        BaasDocument document = thought.createSubmitDocument();

        document.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> baasResult) {
                if (baasResult.isSuccess()) {
                    // TODO display message thought submitted
                } else {
                    Toaster.showNetworkError();
                    Log.e(TAG, baasResult.toString());
                }

                sendingThoughtKeys.remove(thought.getKey());
            }
        });
    }

    private static void checkUserAuthenticatedAndDisplayErrorMessage() {
        if (!BaasUser.isAuthentcated()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());

            builder.setTitle(R.string.cloud_error_title);
            builder.setMessage(R.string.cloud_error_message);

            builder.setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();

        }
    }

}
