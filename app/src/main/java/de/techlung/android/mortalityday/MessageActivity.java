package de.techlung.android.mortalityday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.techlung.android.mortalityday.settings.PreferencesActivity;
import de.techlung.android.mortalityday.util.MortalityDayUtil;

public class MessageActivity extends BaseActivity {
    public static final String MESSAGE_EXTRA = "MESSAGE_EXTRA";
    public static final String AUTHOR_EXTRA = "AUTHOR_EXTRA";

    // TODOS
    // start on boot error
    // not only random message but complete pool
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        String message = getIntent().getStringExtra(MESSAGE_EXTRA);
        String author = getIntent().getStringExtra(AUTHOR_EXTRA);

        if ((message == null || message.equals("")) && (author == null || author.equals(""))) {
            MortalityDayUtil.MortalityDayQuote quote = MortalityDayUtil.getQuote(this);
            message = quote.message;
            author = quote.author;
        }

        TextView messageView = (TextView) findViewById(R.id.message);
        if (author != null && !author.trim().equals("")) {
            message = "\"" + message + "\"";
        }
        messageView.setText(message);

        TextView authorView = (TextView) findViewById(R.id.author);
        if (author != null && !author.trim().equals("")) {
            authorView.setText(" - " + author);
        } else {
            authorView.setVisibility(View.GONE);
        }

        findViewById(R.id.message_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageActivity.this, PreferencesActivity.class);
                intent.putExtra(PreferencesActivity.CALLED_INTERNAL, true);
                startActivity(intent);
            }
        });
    }

}
