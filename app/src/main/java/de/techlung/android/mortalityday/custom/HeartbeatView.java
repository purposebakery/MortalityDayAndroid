package de.techlung.android.mortalityday.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;

public class HeartbeatView extends View {

    int width = 0;
    int height = 0;

    boolean layouted = false;

    HashMap<Integer, Float> timeYTargets = new HashMap<>();

    public HeartbeatView(Context context) {
        super(context);
    }

    public HeartbeatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartbeatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeartbeatView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!layouted) {
            return;
        }

        if (timeYTargets.isEmpty()){
            initTimeYTargets();
        }



    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layouted = true;

        width = r - l;
        height = b - t;

        super.onLayout(changed, l, t, r, b);
    }

    private void initTimeYTargets() {
        timeYTargets.put(0, 0.0f);
        timeYTargets.put(50, .0f);
    }
}
