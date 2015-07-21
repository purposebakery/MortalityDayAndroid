package de.techlung.android.mortalityday.thoughts;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import de.techlung.android.mortalityday.MainActivity;
import de.techlung.android.mortalityday.R;

public class ThoughtsViewController {
    public static final String TAG = ThoughtsViewController.class.getName();

    Activity activity;
    View view;

    public ThoughtsViewController(ViewGroup container) {
        activity = MainActivity.getInstance();
        view = createView(LayoutInflater.from(activity), container);
        ButterKnife.bind(view);
    }

    public View getView() {
        return view;
    }

    private View createView(LayoutInflater inflater, final ViewGroup container) {

        view = inflater.inflate(R.layout.thoughts_main, container, false);

        return view;
    }
}
