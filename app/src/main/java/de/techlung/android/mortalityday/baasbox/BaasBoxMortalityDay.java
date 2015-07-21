package de.techlung.android.mortalityday.baasbox;

import android.util.Log;

import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;

import de.techlung.android.mortalityday.greendao.extended.DaoFactory;
import de.techlung.android.mortalityday.greendao.generated.Thought;
import de.techlung.android.mortalityday.greendao.generated.ThoughtDao;
import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.util.Toaster;

public class BaasBoxMortalityDay {
    private static final String TAG = BaasBoxMortalityDay.class.getName();

    public static void sendThought(final Thought thought, final OnBassResultListener listener) {
        BaasDocument document = new BaasDocument(Constants.COLLECTION_THOUGHTS);

        document.put(ThoughtDao.Properties.Key.name, thought.getKey());
        document.put(ThoughtDao.Properties.Category.name, thought.getCategory());
        document.put(ThoughtDao.Properties.Text.name, thought.getText());
        document.put(ThoughtDao.Properties.Rating.name, thought.getRating());

        document.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> baasResult) {
                if (baasResult.isSuccess()) {
                    thought.setShared(true);
                    DaoFactory.getInstance().getExtendedThoughtDao().insertOrReplace(thought);
                }

                if (listener != null) {
                    listener.onBaasResult(baasResult.isSuccess());
                }

                Log.e(TAG, baasResult.toString());
                Toaster.show(baasResult.toString());
            }
        });
    }

    public interface OnBassResultListener {
        void onBaasResult(boolean success);
    }
}
