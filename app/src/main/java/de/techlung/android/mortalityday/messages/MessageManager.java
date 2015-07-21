package de.techlung.android.mortalityday.messages;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.techlung.android.mortalityday.R;

public class MessageManager {
    private static final int ANIMATE_NONE = 0;
    private static final int ANIMATE_SHORT = 300;
    private static final int ANIMATE_NORMAL = 600;
    private static final int ANIMATE_LONG = 900;

    @Bind(R.id.message_text) TextView message;

    RelativeLayout container;
    Context context;

    public MessageManager(Context context, RelativeLayout container) {
        this.container = container;
        ButterKnife.bind(this, container);
    }

    public void displayNoMortalityDayMessage() {
        showContainer(false, 0);
        showText(1000);
        setText(R.string.no_mortality_day);
        //hideText(7000);
    }

    public void displayFirstMessage() {
        showContainer(false, 0);
        showText(2000);
        setText(R.string.welcome_message);
        hideText(5000);
        hideContainer(6000);
    }

    private void showContainer(boolean animate, int delay) {
        if (animate) {
            YoYo.with(Techniques.FadeIn).duration(ANIMATE_SHORT).delay(delay).playOn(container);
        } else {
            YoYo.with(Techniques.FadeIn).duration(ANIMATE_NONE).delay(delay).playOn(container);
        }
    }

    private void hideContainer(int delay) {
        YoYo.with(Techniques.FadeOut).duration(ANIMATE_SHORT).delay(delay).playOn(container);
    }

    private void showText(int delay) {
       YoYo.with(Techniques.FadeIn).duration(ANIMATE_LONG).delay(delay).playOn(message);
    }

    private void hideText(int delay) {
        YoYo.with(Techniques.FadeOut).duration(ANIMATE_LONG).delay(delay).playOn(message);
    }

    private void setText(int resourceId) {
        message.setText(context.getString(resourceId));
    }
}
