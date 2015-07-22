package de.techlung.android.mortalityday.thoughts;

import com.baasbox.android.BaasDocument;

import java.util.ArrayList;
import java.util.List;

import de.techlung.android.mortalityday.baasbox.BaasBoxMortalityDay;
import de.techlung.android.mortalityday.greendao.extended.DaoFactory;
import de.techlung.android.mortalityday.greendao.generated.Thought;
import de.techlung.android.mortalityday.settings.Preferences;

public class ThoughtManager {
    private static List<LocalThoughtsChangedListener> localListeners = new ArrayList<LocalThoughtsChangedListener>();
    private static List<SharedThoughtsChangedListener> sharedListeners = new ArrayList<SharedThoughtsChangedListener>();

    private static List<BaasDocument> sharedThoughts = new ArrayList<BaasDocument>();

    public static void clearSharedThoughts() {
        sharedThoughts.clear();
    }

    public static List<BaasDocument> getSharedThoughts() {
        return sharedThoughts;
    }

    public static void addSharedThoughts(List<BaasDocument> thoughts) {
        sharedThoughts.addAll(thoughts);
    }

    public static void processLocalThoughtsChanged() {
        triggerLocalThoughtsChangedListener();
        shareThoughtsAutomatically();
    }

    public static void processSharedThoughtsChanged() {
        triggerSharedThoughtsChangedListener();
    }

    private static void shareThoughtsAutomatically() {
        if (Preferences.isAutomaticSharing()) {
            for (Thought thought : DaoFactory.getInstanceLocal().getExtendedThoughtDao().getAllThoughts()) {
                if (thought.getShared() == false) {
                    BaasBoxMortalityDay.sendThought(thought);
                }
            }
        }
    }

    private static void triggerLocalThoughtsChangedListener() {
        for (LocalThoughtsChangedListener listener : localListeners) {
            listener.onLocalThoughtsChanged();
        }
    }

    public static void clearLocalThoughtsChangedListeners() {
        localListeners.clear();
    }

    public static void addLocalThoughtsChangedListener(LocalThoughtsChangedListener listener) {
        localListeners.add(listener);
    }

    public static void removeLocalThoughtsChangedListener(LocalThoughtsChangedListener listener) {
        localListeners.remove(listener);
    }




    private static void triggerSharedThoughtsChangedListener() {
        for (SharedThoughtsChangedListener listener : sharedListeners) {
            listener.onSharedThoughtsChanged();
        }
    }

    public static void clearSharedThoughtsChangedListeners() {
        sharedListeners.clear();
    }

    public static void addSharedThoughtsChangedListener(SharedThoughtsChangedListener listener) {
        sharedListeners.add(listener);
    }

    public static void removeSharedThoughtsChangedListener(SharedThoughtsChangedListener listener) {
        sharedListeners.remove(listener);
    }



    public interface LocalThoughtsChangedListener {
        void onLocalThoughtsChanged();
    }
    public interface SharedThoughtsChangedListener {
        void onSharedThoughtsChanged();
    }
}
