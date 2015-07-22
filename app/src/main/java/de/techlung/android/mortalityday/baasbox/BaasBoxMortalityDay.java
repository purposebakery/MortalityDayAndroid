package de.techlung.android.mortalityday.baasbox;

import android.util.Log;

import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasException;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;

import java.util.ArrayList;
import java.util.List;

import de.techlung.android.mortalityday.BaseActivity;
import de.techlung.android.mortalityday.greendao.extended.DaoFactory;
import de.techlung.android.mortalityday.greendao.extended.ExtendedThoughtDao;
import de.techlung.android.mortalityday.greendao.generated.Thought;
import de.techlung.android.mortalityday.greendao.generated.ThoughtDao;
import de.techlung.android.mortalityday.greendao.generated.ThoughtMeta;
import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.thoughts.ThoughtManager;
import de.techlung.android.mortalityday.util.Toaster;

public class BaasBoxMortalityDay {
    private static final String TAG = BaasBoxMortalityDay.class.getName();

    private static List<String> sendingThoughtKeys = new ArrayList<String>();
    private static List<String> deletingThoughtKeys = new ArrayList<String>();

    private static int VOTE_UP = 1;
    private static int VOTE_DOWN = -1;

    public static void getAllThoughts() {
        BaasDocument.fetchAll(Constants.COLLECTION_THOUGHTS, new BaasHandler<List<BaasDocument>>() {
            @Override
            public void handle(BaasResult<List<BaasDocument>> baasResult) {
                if (baasResult.isSuccess()) {
                    try {
                        ThoughtManager.clearSharedThoughts();
                        ThoughtManager.addSharedThoughts(baasResult.get());

                        ThoughtManager.processSharedThoughtsChanged();
                    } catch (BaasException e) {
                        Toaster.showNetworkError();
                        Log.e(TAG, e.toString());
                    }
                } else {
                    Toaster.showNetworkError();
                    Log.e(TAG, baasResult.toString());
                }
            }
        });
    }

    public static void sendThought(final Thought thought) {
        if (sendingThoughtKeys.contains(thought.getKey())) {
            return;
        }

        sendingThoughtKeys.add(thought.getKey());

        BaasDocument document = thought.createDocument();

        document.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> baasResult) {
                if (baasResult.isSuccess()) {
                    thought.setShared(true);
                    DaoFactory.getInstanceLocal().getExtendedThoughtDao().insertOrReplace(thought);
                } else {
                    Toaster.showNetworkError();
                    Log.e(TAG, baasResult.toString());
                }

                sendingThoughtKeys.remove(thought.getKey());
            }
        });
    }

    public static void deleteThought(final BaasDocument thought) {
        thought.delete(new BaasHandler<Void>() {
            @Override
            public void handle(BaasResult<Void> baasResult) {
                if (baasResult.isSuccess()) {
                    ThoughtManager.processSharedThoughtsChanged();
                } else {
                    Toaster.showNetworkError();
                    Log.e(TAG, baasResult.toString());
                }
            }
        });
    }

    public static void restoreThoughts() {
        BaasDocument.fetchAll(Constants.COLLECTION_THOUGHTS,
                new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> baasResult) {
                        try {
                            if (baasResult.isSuccess()) {
                                ExtendedThoughtDao extendedThoughtDao = DaoFactory.getInstanceLocal().getExtendedThoughtDao();
                                extendedThoughtDao.deleteAll();

                                for (BaasDocument document : baasResult.get()) {
                                    Thought thought = new Thought();
                                    thought.setDocument(document);
                                    thought.setShared(true);

                                    if (thought.getKey().startsWith(Preferences.getDeviceId())) {
                                        extendedThoughtDao.insertOrReplace(thought);
                                    }
                                }
                            } else {
                                Toaster.showNetworkError();
                                Log.e(TAG, baasResult.toString());
                            }

                        } catch (BaasException exeption) {
                            Toaster.showNetworkError();
                            Log.e(TAG, exeption.toString());
                            Log.e(TAG, baasResult.toString());
                        }
                    }
                });
    }

    public static void voteUpThought(BaasDocument thought) {
        voteThought(thought, VOTE_UP);
    }

    public static void voteDownThought(BaasDocument thought) {
        voteThought(thought, VOTE_DOWN);
    }

    private static void voteThought(BaasDocument thought, final int vote) {
        thought.refresh(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> baasResult) {
                try {
                    if (baasResult.isSuccess()) {
                        BaasDocument document = baasResult.get();
                        document.put(ThoughtDao.Properties.Rating.name, vote + document.getInt(ThoughtDao.Properties.Rating.name));
                        document.save(new BaasHandler<BaasDocument>() {
                            @Override
                            public void handle(BaasResult<BaasDocument> baasResult) {
                                try {
                                    if (baasResult.isSuccess()) {
                                        ThoughtMeta meta = new ThoughtMeta();
                                        meta.setKey(baasResult.get().getString(ThoughtDao.Properties.Key.name));
                                        meta.setWasVoted(true);
                                        DaoFactory.getInstanceLocal().getExtendedThoughtMetaDao().insertOrReplace(meta);

                                        ThoughtManager.processSharedThoughtsChanged();
                                    } else {
                                        Toaster.showNetworkError();
                                        Log.e(TAG, baasResult.toString());
                                    }
                                } catch (BaasException e) {
                                    Toaster.showNetworkError();
                                    Log.e(TAG, e.toString());
                                    Log.e(TAG, baasResult.toString());
                                }
                            }
                        });
                    } else {
                        Toaster.showNetworkError();
                        Log.e(TAG, baasResult.toString());
                    }
                } catch (BaasException e) {
                    Toaster.showNetworkError();
                    Log.e(TAG, e.toString());
                    Log.e(TAG, baasResult.toString());
                }
            }
        });
    }
}
