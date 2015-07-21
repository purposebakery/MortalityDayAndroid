package de.techlung.android.mortalityday.gathering;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.R;

public class GatheringViewController {
    public static final String TAG = GatheringViewController.class.getName();

    Activity activity;
    View view;

    public GatheringViewController(ViewGroup container) {
        activity = MainActivity.getInstance();
        view = createView(LayoutInflater.from(activity), container);
        ButterKnife.bind(view);
    }

    public View getView() {
        return view;
    }

    private View createView(LayoutInflater inflater, final ViewGroup container) {

        view = inflater.inflate(R.layout.gathering_main, container, false);


        return view;
    }

}
